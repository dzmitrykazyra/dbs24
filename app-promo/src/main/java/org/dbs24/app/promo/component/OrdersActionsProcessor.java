package org.dbs24.app.promo.component;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.app.promo.entity.AppPackage;
import org.dbs24.app.promo.entity.BotDetail;
import org.dbs24.app.promo.entity.Order;
import org.dbs24.app.promo.entity.OrderAction;
import org.dbs24.app.promo.exec.TaskProcessor;
import org.dbs24.app.promo.exec.ThreadExecutorService;
import org.dbs24.app.promo.kafka.KafkaService;
import org.dbs24.app.promo.rest.dto.proxy.refs.Proxy;
import org.dbs24.application.core.service.funcs.TestFuncs;
import org.dbs24.google.api.ExecOrderAction;
import org.dbs24.google.api.OrderActionResult;
import org.dbs24.google.api.OrderActionsConsts.ActionResultEnum;
import org.dbs24.google.api.dto.GmailAccountInfo;
import org.dbs24.google.api.dto.ProxyInfo;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.stmt.VoidStmt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.dbs24.app.promo.component.OrdersActionsService.createNewOrderAction;
import static org.dbs24.app.promo.component.OrdersActionsService.isReadyToExecute;
import static org.dbs24.application.core.locale.NLS.localDateTime2long;
import static org.dbs24.application.core.service.funcs.ServiceFuncs.createConcurencyCollection;
import static org.dbs24.consts.SysConst.BOOLEAN_FALSE;
import static org.dbs24.consts.SysConst.BOOLEAN_TRUE;
import static org.dbs24.google.api.OrderActionsConsts.ActionResultEnum.AR_CREATED;
import static org.dbs24.google.api.OrderActionsConsts.ActionResultEnum.OK_FINISHED;
import static org.dbs24.stmt.StmtProcessor.*;

@Getter
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "app-promo")
@EnableScheduling
public class OrdersActionsProcessor extends AbstractApplicationService {

    final KafkaService kafkaService;
    final OrdersActionsService ordersActionsService;
    final OrdersService ordersService;
    final BotsService botsService;
    final PackagesService packagesService;
    final RefsService refsService;
    final Collection<Order> actualOrders = createConcurencyCollection();
    final Collection<OrderAction> futureActions = createConcurencyCollection();
    final ThreadExecutorService threadExecutorService;
    private Boolean serviceIsReady = BOOLEAN_FALSE;
    private final ProxyService proxyService;

    @Value("${config.orders.processing.tasks-per-action:1}")
    private Integer tasksPerActions;

    public final Predicate<ActionResultEnum> isFinishedOk = test -> test.equals(OK_FINISHED);

    public OrdersActionsProcessor(KafkaService kafkaService,
                                  OrdersActionsService ordersActionsService,
                                  OrdersService ordersService,
                                  RefsService refsService,
                                  PackagesService packagesService,
                                  BotsService botsService,
                                  ProxyService proxyService) {

        this.botsService = botsService;
        this.kafkaService = kafkaService;
        this.ordersActionsService = ordersActionsService;
        this.ordersService = ordersService;
        this.refsService = refsService;
        this.packagesService = packagesService;
        this.proxyService = proxyService;
        this.threadExecutorService = ThreadExecutorService.create("FutureTaskProcessor", 10, 10000, getTaskProcessor());
    }

    final TaskProcessor taskProcessor = () -> sendFuturedTask();

    final Consumer<OrderAction> actionSender = action -> {
        sendTask(action);
        futureActions.remove(action);
    };

    final VoidStmt futureTask = () -> {
        synchronized (futureActions) {
            processCollection(futureActions, fa -> fa.stream().filter(isReadyToExecute).limit(tasksPerActions).forEach(actionSender));
        }
    };

    private void sendFuturedTask() {
        ifTrue(serviceIsReady && (!futureActions.isEmpty()), getFutureTask());
    }

    @Transactional
    public void processTaskResult(OrderActionResult orderActionResult) {

        final Consumer<OrderAction> orderActionConsumer = orderAction -> {

            final Order order = orderAction.getOrder();

            log.debug("process action: {}, actionResult: {}, orderId: {} ",
                    orderAction.getActionId(),
                    orderActionResult.getActionResultEnum(),
                    order.getOrderId());

            // update action
            getOrdersActionsService().saveOrderActionHist(orderAction);
            orderAction.setActionResult(getRefsService().findActionResult(orderActionResult.getActionResultEnum()));
            getOrdersActionsService().saveOrderAction(orderAction);

            Mono.just(orderActionResult)
                    .flatMap(createNextAction)
                    .flatMap(recreateOrder)
                    .flatMap(cancelOrder)
                    .doOnError(processErrorIfAny)
                    .subscribe();
        };

        //actualOrdersActions
        orderActionConsumer.accept(
                getOrdersActionsService()
                        .findOrderAction(orderActionResult.getActionId()));

    }

    final Consumer<Throwable> processErrorIfAny = throwable -> log.error("processEvent<OrderActionEvent> Error: {}", throwable.getLocalizedMessage());
    //==================================================================================================================
    final Function<OrderActionResult, Mono<OrderActionResult>> createNextAction = orderActionEvent -> {

        ifTrue(isFinishedOk.test(orderActionEvent.getActionResultEnum()), () ->
                // next action
                startOrderProcessing(getOrdersService().findOrder(orderActionEvent.getOrderId())));

        return Mono.just(orderActionEvent);
    };

    //==================================================================================================================
    public void startOrderProcessing(Order order) {

        log.debug("startOrderProcessing: orderId = {}", order.getOrderId());

        getOrdersActionsService()
                .getNextBatchSetup(order)
                .ifPresentOrElse(batchSetup -> {

                    // new action
                    final OrderAction orderAction = createNewOrderAction.get();
                    // properties
                    orderAction.setExecutionOrder(batchSetup.getExecutionOrder());
                    orderAction.setBatchSetup(batchSetup);
                    orderAction.setActionResult(getRefsService().findActionResult(AR_CREATED));
                    orderAction.setOrder(order);
                    // temporary constant bot
                    orderAction.setBot(getBotsService().findBot(153128));
                    orderAction.setAction(batchSetup.getAction());
                    orderAction.setActionStartDate(LocalDateTime.now());
                    // save
                    getOrdersActionsService().saveOrderAction(orderAction);

                    // to processing
                    futureActions.add(orderAction);

                }, () -> getOrdersService().closeOrder(order));
    }

    //==================================================================================================================
    final Function<OrderActionResult, Mono<OrderActionResult>> recreateOrder = orderActionEvent -> {

        return Mono.just(orderActionEvent);
    };
    //==================================================================================================================
    final Function<OrderActionResult, Mono<OrderActionResult>> cancelOrder = orderActionEvent -> {

//        final ActionEventEnum actionEventEnum = AE_FINISH_ACTION;
//
//        StmtProcessor.ifTrue(orderActionEvent.getActionEventEnum().equals(actionEventEnum), () -> {
//
//            Assert.notNull(orderActionEvent.getOrderAction(), "orderAction not defined");
//
//            log.info("process {}, actionId: {}", actionEventEnum, orderActionEvent.getOrderAction().getActionId());

        // prepare new action from loop

//        });

        return Mono.just(orderActionEvent);
    };

    //==================================================================================================================
    public void sendTask(OrderAction orderAction) {

        kafkaService.sendAction2Google(create(ExecOrderAction.class, execOrderAction -> {

            final Consumer<OrderAction> execOrderActionConsumer = action -> {

                final Order order = ordersService.findOrder(action.getOrder().getOrderId());
                final AppPackage appPackage = packagesService.findPackage(order.getAppPackage().getPackageId());

                execOrderAction.setActionId(action.getActionId());
                execOrderAction.setOrderId(action.getOrder().getOrderId());
                execOrderAction.setActRefId(action.getAction().getActRefId());
                execOrderAction.setProviderId(appPackage.getProvider().getProviderId());
                execOrderAction.setAppPackage(appPackage.getPackageName());
                execOrderAction.setStartExecution(localDateTime2long(action.getActionStartDate()));
                execOrderAction.setComment(TestFuncs.generateTestString20());

//                todo: fill pr_bot_details table & remove large code
//                execOrderAction.setGmailAccountInfo(StmtProcessor.create(GmailAccountInfo.class, info -> {
//                    BotDetail details = botsService.findDetailsByBotId(action.getBot().getBotId());
//
//                    info.setEmail(action.getBot().getLogin());
//                    info.setPass(action.getBot().getPass());
//                    info.setGmailToken(details.getYa29GmailToken());
//                    info.setGplayToken(details.getYa29GPlayToken());
//                    info.setAasToken(details.getAasToken());
//                    info.setGsfId(details.getGsfId());
//                    info.setPhoneConfigName(details.getPhoneConfig().getPhoneConfigName());
//
//                }));
//
//                execOrderAction.setProxyInfo(StmtProcessor.create(ProxyInfo.class, info -> {
//                    Proxy proxy = proxyService.retrieveProxyByBot(action.getBot());
//
//                    info.setUrl(proxy.getUrl());
//                    info.setPort(proxy.getPort());
//                    info.setLogin(proxy.getLogin());
//                    info.setPass(proxy.getPass());
//                }));


                log.debug("send task [actionId: {}, orderId: {}, action: {}]",
                        execOrderAction.getActionId(),
                        execOrderAction.getOrderId(),
                        execOrderAction);

            };

            execOrderActionConsumer.accept(orderAction);

        }));
    }

//    @Scheduled(fixedRateString = "${config.kafka.processing-interval:60000}", cron = "${config.kafka.processing-cron:}")
//    public void sendTestsTasks() {
//        sendTask(getOrdersActionsService().findOrderAction(153344));
//    }

    //==================================================================================================================
    @Override
    public void initialize() {
//        actualOrders.addAll(getOrdersService()
//                .loadActualOrders());

        // processing invalid orders
        ordersService
                .findInvalidOrders()
                .stream()
                .limit(1)
                //.parallel()
                .forEach(this::startOrderProcessing);

        futureActions.addAll(getOrdersActionsService()
                .loadFuturedActions());

        serviceIsReady = BOOLEAN_TRUE;

        log.info("{}: service is ready", getClass().getSimpleName());
    }

    @Override
    public void destroy() {
        threadExecutorService.destroy();
    }
}