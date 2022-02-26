package org.dbs24.tik.assist.service.scheduled.resolver;

import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.OrderAction;
import org.dbs24.tik.assist.entity.dto.action.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Log4j2
@Component
public class TiktokActionInteractor {

    private WebClient webClient;

    @Value("${config.tik-connector.action.uri}")
    private String taskExecutorUri;

    @Value("${config.tik-connector.action.single.create-action}")
    private String createActionTaskPath;
    @Value("${config.tik-connector.action.single.get-action-status}")
    private String getActionExecutionStatusPath;

    @Value("${config.tik-connector.action.multiple.create-actions}")
    private String createActionTasksPath;
    @Value("${config.tik-connector.action.multiple.get-actions-statuses}")
    private String getActionsExecutionStatusesPath;

    public TiktokActionInteractor() {}

    @PostConstruct
    public void webClientInit() {

        webClient = WebClient
                .builder()
                .baseUrl(taskExecutorUri)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build();
    }

    public void sendSingleTaskToExecute(OrderAction orderActionToSend) {

        webClient
                .post()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(createActionTaskPath)
                        .build())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(Mono.just(ActionTask.toActionTask(orderActionToSend)), ActionTask.class)
                .retrieve()
                .bodyToMono(ActionTaskConfirm.class)
                .subscribe(confirm -> log.info("CONFIRM {}", confirm));
    }

    public void sendMultipleTaskToExecute(List<OrderAction> orderActionsToSend) {

        webClient
                .post()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(createActionTasksPath)
                        .build())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(Mono.just(MultipleActionTask.toDto(orderActionsToSend)), MultipleActionTask.class)
                .retrieve()
                .bodyToMono(MultipleActionTaskConfirm.class)
                .onErrorReturn(
                        StmtProcessor.create(
                                MultipleActionTaskConfirm.class,
                                multipleActionTaskConfirm -> {
                                    multipleActionTaskConfirm.setMultipleActionTaskConfirm(List.of());
                                    multipleActionTaskConfirm.setAllFailed(true);
                                }
                        )
                )
                .subscribe(confirm -> log.info("CONFIRM {}", confirm));
    }

    public ActionTaskResult getSingleTaskResult(OrderAction orderAction) {

        return webClient
                .get()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(getActionExecutionStatusPath + "/" + orderAction.getOrderActionId())
                        .build())
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.empty())
                .bodyToMono(ActionTaskResult.class)
                .toProcessor().block();
    }

    public MultipleActionTaskResult getMultipleTaskResult(List<OrderAction> activeOrderActions) {

        return webClient
                .post()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(getActionsExecutionStatusesPath)
                        .build())
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(Mono.just(OrderActionIdList.toDto(activeOrderActions)), OrderActionIdList.class)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.empty())
                .bodyToMono(MultipleActionTaskResult.class)
                .onErrorReturn(
                        StmtProcessor.create(
                                MultipleActionTaskResult.class,
                                multipleActionTaskResult -> {
                                    multipleActionTaskResult.setActionTaskResults(List.of());
                                    multipleActionTaskResult.setAllFailed(true);
                                }
                        )
                )
                .toProcessor().block();
    }
}
