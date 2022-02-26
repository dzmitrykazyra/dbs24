package org.dbs24.tik.mobile.service.impl;

import com.google.common.base.Strings;
import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.dao.*;
import org.dbs24.tik.mobile.entity.domain.*;
import org.dbs24.tik.mobile.entity.dto.action.ExecutedActionResponseDto;
import org.dbs24.tik.mobile.entity.dto.action.OrderActionDto;
import org.dbs24.tik.mobile.entity.dto.action.OrderActionTypeDtoList;
import org.dbs24.tik.mobile.entity.dto.order.OrderDto;
import org.dbs24.tik.mobile.entity.dto.order.OrderIdDto;
import org.dbs24.tik.mobile.entity.dto.order.actual.ActualOrderDto;
import org.dbs24.tik.mobile.entity.dto.order.actual.ActualOrderDtoList;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.SearchSinglePostDto;
import org.dbs24.tik.mobile.service.LinkExpirationService;
import org.dbs24.tik.mobile.service.OrderPriceService;
import org.dbs24.tik.mobile.service.OrderService;
import org.dbs24.tik.mobile.service.UserTiktokAccountService;
import org.dbs24.tik.mobile.service.exception.http.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    @Value("${constraint.orders.fetching.rows}")
    private Integer amountFetchingRows;

    @Value("${constraint.orders.action.lead-time-minutes}")
    private Integer minutesToPerformAction;

    private final OrderDao orderDao;
    private final OrderActionDao orderActionDao;
    private final OrderStatusDao orderStatusDao;
    private final OrderActionTypeDao orderActionTypeDao;
    private final UserDepositDao userDepositDao;
    private final OrderExecutionProgressDao orderExecutionProgressDao;
    private final UserProfileDetailDao userProfileDetailDao;
    private final UserTiktokAccountService userTiktokAccountService;
    private final OrderPriceService orderPriceService;
    private final UserDao userDao;
    private final LinkExpirationService linkExpirationService;

    @Autowired
    public OrderServiceImpl(
            OrderDao orderDao,
            OrderActionDao orderActionDao,
            OrderStatusDao orderStatusDao,
            OrderActionTypeDao orderActionTypeDao,
            UserProfileDetailDao userProfileDetailDao,
            UserDao userDao,
            UserDepositDao userDepositDao,
            OrderExecutionProgressDao orderExecutionProgressDao,
            UserTiktokAccountService userTiktokAccountService,
            OrderPriceService orderPriceService,
            LinkExpirationService linkExpirationService) {

        this.orderDao = orderDao;
        this.orderActionDao = orderActionDao;
        this.orderStatusDao = orderStatusDao;
        this.orderActionTypeDao = orderActionTypeDao;
        this.userProfileDetailDao = userProfileDetailDao;
        this.userDao = userDao;
        this.userDepositDao = userDepositDao;
        this.orderExecutionProgressDao = orderExecutionProgressDao;
        this.userTiktokAccountService = userTiktokAccountService;
        this.orderPriceService = orderPriceService;
        this.linkExpirationService = linkExpirationService;
    }

    @Override
    @Transactional
    public Mono<ActualOrderDtoList> getActualOrders(Integer userId, Optional<String> actionTypeId) {
        List<ActualOrderDto> availableOrders = getActualOderByActionType(userId, actionTypeId, amountFetchingRows);
        if(availableOrders.isEmpty()){
            ActiveOrdersStorage.removeOrders();
            availableOrders = getActualOderByActionType(userId, actionTypeId, amountFetchingRows);
        }else if(availableOrders.size() < amountFetchingRows){
            ActiveOrdersStorage.removeOrders();
            ActiveOrdersStorage.addOrders(availableOrders);
            availableOrders.addAll(getActualOderByActionType(userId, actionTypeId, amountFetchingRows - availableOrders.size()));
            ActiveOrdersStorage.removeOrders();
        }
        ActiveOrdersStorage.addOrders(availableOrders);

        for (ActualOrderDto availableOrder : availableOrders) {
            verifyCoverLink(availableOrder);
            verifyAvatarLink(availableOrder, userId);
        }

        return Mono.just(ActualOrderDtoList.of(availableOrders));
    }

    @Override
    @Transactional
    public Mono<ExecutedActionResponseDto> completeOrderAction(Mono<OrderActionDto> orderActionDtoMono, Integer userId) {

        return orderActionDtoMono.map(orderActionDto -> {

            Order order = orderDao.findOrderById(orderActionDto.getOrderId());
            User user = userDao.findById(userId);

            Boolean isActionCompleted = isActionSuccessCompleted(orderActionDto);

            orderActionDao.saveOrderAction(OrderAction.builder()
                    .withOrder(order)
                    .withUser(user)
                    .withStartDate(orderActionDto.getStartDate())
                    .withFinishDate(orderActionDto.getFinishDate())
                    .withIsSuccess(isActionCompleted)
                    .withMetricsAmountBefore(orderActionDto.getMetricsAmountBefore())
                    .withMetricsAmountAfter(orderActionDto.getMetricsAmountAfter())
                    .build()
            );

            StmtProcessor.ifTrue(isActionCompleted, () -> {
                userDepositDao.increaseUserDeposit(user, orderPriceService.getActionCost(order.getOrderActionType()));

                OrderExecutionProgress orderProgress = orderExecutionProgressDao.incrementOrderProgress(order);

                StmtProcessor.ifTrue(orderProgress.getDoneActionsQuantity().equals(order.getActionsAmount()),
                        () -> orderDao.updateOrderStatusToComplete(order));
            });

            ActiveOrdersStorage.removeOrder(orderActionDto.getOrderId());

            return ExecutedActionResponseDto.of(order.getOrderId(), isActionCompleted);
        });
    }

    @Override
    @Transactional
    public Mono<OrderIdDto> createOrder(Integer userId, Mono<OrderDto> orderDtoMono) {
        OrderDto orderDto = orderDtoMono.toProcessor().block();
        User user = userDao.findById(userId);

        return userTiktokAccountService.findVideoByLink(orderDto.getTiktokUri()).map(
                postDto -> {

                    Integer orderCost = orderPriceService.getOrderCostByActionsAmountAndActionType(
                            orderDto.getActionsAmount(),
                            orderActionTypeDao.findActionTypeById(orderDto.getOrderTypeId())
                    );

                    userDepositDao.decreaseUserDeposit(user, orderCost);

                    if (Strings.isNullOrEmpty(orderDto.getOrderName())) {
                        orderDto.setOrderName("Your order #" + orderDao.findUserOrdersAmount(user) + 1);
                    }

                    Order savedOrder = orderDao.saveNewOrder(
                            createActiveOrder(orderDto, user, postDto.getTiktokUserPostDto().getAwemeId(), orderCost, postDto.getTiktokUserPostDto().getCover())
                    );

                    return OrderIdDto.of(savedOrder);
                }
        );
    }

    @Override
    public Mono<OrderIdDto> skipOrder(Mono<OrderIdDto> orderIdDtoMono) {

        return orderIdDtoMono.map(orderIdDto -> {
            ActiveOrdersStorage.removeOrder(orderIdDto.getOrderId());

            return StmtProcessor.create(OrderIdDto.class,
                    orderId -> orderId.setOrderId(orderIdDto.getOrderId())
            );
        });
    }

    @Override
    public Mono<OrderActionTypeDtoList> getAllOrderActionTypes() {
        return Mono.just(OrderActionTypeDtoList.actionTypesToList(orderActionTypeDao.findAll()));
    }

    @Override
    public void removeExpiredOrders() {
        ActiveOrdersStorage.removeExpiredOrders(minutesToPerformAction);
    }

    private boolean isActionSuccessCompleted(OrderActionDto orderActionDto) {
        long leadTimeMinutes = ChronoUnit.MINUTES.between(orderActionDto.getStartDate(), orderActionDto.getFinishDate());

        return orderActionDto.getMetricsAmountAfter() > orderActionDto.getMetricsAmountBefore() & leadTimeMinutes <= minutesToPerformAction;
    }

    private Order createActiveOrder(OrderDto orderDto, User user, String awemeId, Integer orderCost, String cover) {
        return Order.builder()
                .withOrderStatus(orderStatusDao.findInProgressOrderStatus())
                .withOrderActionType(orderActionTypeDao.findActionTypeById(orderDto.getOrderTypeId()))
                .withActualDate(LocalDateTime.now())
                .withStartDate(LocalDateTime.now())
                .withEndDate(LocalDateTime.now().plusDays(orderDto.getOrderDuration()))
                .withActionsAmount(orderDto.getActionsAmount())
                .withOrderSum(orderCost)
                .withTiktokUri(orderDto.getTiktokUri())
                .withAwemeId(awemeId)
                .withUser(user)
                .withOrderName(orderDto.getOrderName())
                .withCover(cover)
                .build();
    }

    private boolean checkedActionType(Integer actionTypeIdFromOrder, Optional<String> actionTypeIdFromUser){
        return actionTypeIdFromUser.map(s -> actionTypeIdFromOrder.equals(Integer.parseInt(s))).orElse(true);
    }

    private List<ActualOrderDto> getActualOderByActionType(Integer userId, Optional<String> actionTypeId, Integer limit){
        List<ActualOrderDto> actualOrders = new ArrayList<>();
        orderDao.findAllAvailableOrders(userId)
                .stream()
                .filter(not(ActiveOrdersStorage::isOrderExecute))
                .filter(order -> checkedActionType(order.getOrderActionType().getOrderActionTypeId(),actionTypeId))
                .limit(limit)
                .forEach(order -> {
                    UserProfileDetail userProfile = userProfileDetailDao.findByUserId(order.getUser().getId());
                    actualOrders.add(ActualOrderDto.of(order, userProfile,
                            orderPriceService.getActionCost(order.getOrderActionType()),order.getCover())
                    );
                });
        return actualOrders;
    }

    private void verifyAvatarLink(ActualOrderDto availableOrder, int userId) {
        String avatarUrl = availableOrder.getAvatarUrl();
        boolean avatarNeedUpdate = linkExpirationService.checkExpiration(avatarUrl);
        if (avatarNeedUpdate) {
            String updatedAvatarLink = userTiktokAccountService.updateAvatarLink(userId);
            availableOrder.setAvatarUrl(updatedAvatarLink);
        }
    }

    private void verifyCoverLink(ActualOrderDto availableOrder) {
        String coverUrl = availableOrder.getCover();
        boolean coverNeedUpdate = linkExpirationService.checkExpiration(coverUrl);
        if (coverNeedUpdate) {
            updateCoverLink(availableOrder);
        }
    }

    private void updateCoverLink(ActualOrderDto actualOrder) {
        Mono<SearchSinglePostDto> videoByLink = userTiktokAccountService.findVideoByLink(actualOrder.getTiktokUri());
        SearchSinglePostDto searchSinglePostDto = videoByLink.toProcessor().block();
        if (searchSinglePostDto == null) {
            throw new BadRequestException();
        }
        String updatedCover = searchSinglePostDto.getTiktokUserPostDto().getCover();
        actualOrder.setCover(updatedCover);
        Order order = orderDao.findOrderById(actualOrder.getOrderId());
        order.setCover(updatedCover);
        orderDao.update(order);
    }
}

@Log4j2
final class ActiveOrdersStorage {
    private static final Map<Integer, LocalDateTime> activeOrdersMap = new HashMap<>();

    public static boolean isOrderExecute(Order order) {

        return activeOrdersMap.containsKey(order.getOrderId());
    }

    public static void removeOrder(int orderId) {

        synchronized (activeOrdersMap) {
            activeOrdersMap.remove(orderId);
        }
    }

    public static void removeOrders(){
        synchronized (activeOrdersMap){
            activeOrdersMap.clear();
        }
    }

    public static void addOrders(List<ActualOrderDto> actualOrders) {

        Map<Integer, LocalDateTime> allOrders = actualOrders.stream()
                .map(ActualOrderDto::getOrderId)
                .collect(Collectors.toMap(Function.identity(), date -> LocalDateTime.now()));

        synchronized (activeOrdersMap) {
            activeOrdersMap.putAll(allOrders);
        }
    }

    public static void removeExpiredOrders(Integer timeToPerformOrder) {
        log.info("START CLEAR ALL EXPIRED ORDERS. AMOUNT ORDER IN CACHE = {}", activeOrdersMap.size());

        activeOrdersMap
                .entrySet()
                .removeIf(entry -> entry.getValue().compareTo(
                        entry.getValue().plusMinutes(timeToPerformOrder)) < 0);

        log.info("FINISH CLEAR ALL EXPIRED ORDERS. AMOUNT ORDER IN CACHE = {}", activeOrdersMap.size());
    }
}