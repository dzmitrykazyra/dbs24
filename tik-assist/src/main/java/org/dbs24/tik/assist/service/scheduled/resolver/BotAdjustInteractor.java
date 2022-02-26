package org.dbs24.tik.assist.service.scheduled.resolver;

import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.constant.reference.RepairTaskResultDefine;
import org.dbs24.tik.assist.entity.domain.Bot;
import org.dbs24.tik.assist.entity.dto.bot.RepairBotTask;
import org.dbs24.tik.assist.entity.dto.bot.RepairBotTaskConfirm;
import org.dbs24.tik.assist.entity.dto.bot.RepairBotTaskResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

import java.util.Optional;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Log4j2
@Component
public class BotAdjustInteractor {

    private WebClient webClient;

    @Value("${config.tik-connector.agent.uri}")
    private String connectorUri;

    @Value("${config.tik-connector.agent.repairAgent}")
    private String repairAgent;

    @Value("${config.tik-connector.agent.getAgentStatus}")
    private String getAgentStatus;

    public BotAdjustInteractor() {}

    @PostConstruct
    public void webClientInit() {

        webClient = WebClient.builder()
                .baseUrl(connectorUri)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build();
    }

    public Optional<RepairBotTaskResult> addRepairTask(Bot task, RepairBotTask repairBotTask) {

        Optional<RepairBotTaskResult> repairBotTaskResultOptional = Optional.empty();
        final Mono<RepairBotTask> monoActionTask = Mono.just(repairBotTask);

        RepairBotTaskConfirm confirm = webClient
                .post()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(repairAgent)
                        .build())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(monoActionTask, RepairBotTask.class)
                .retrieve()
                .bodyToMono(RepairBotTaskConfirm.class)
                .toProcessor().block();

        if (confirm.getIsConfirmed()) {

            repairBotTaskResultOptional = Optional.of(StmtProcessor.create(
                    RepairBotTaskResult.class,
                    result -> {
                        result.setAgentId(task.getBotId());
                        result.setTaskResult(RepairTaskResultDefine.TR_CREATED.getId());
                        result.setTaskId(repairBotTask.getTaskId());

                        log.info("add new repair task (taskId={}, agentId={}): {}", result.getTaskId(), result.getAgentId(), result);
                    }));
        }

        if (StmtProcessor.notNull(confirm.getError())) {

            log.error("Cannot create repair task. Error:'{}'. Task id:{}", confirm.getError(), confirm.getTaskId());
        }

        return repairBotTaskResultOptional;
    }

    public RepairBotTaskResult updateRepairTaskStatus(RepairBotTaskResult task) {

        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(getAgentStatus + "/" + String.valueOf(task.getTaskId()))
                        //.queryParam(QP_TASK_ID, String.valueOf(task.getTaskId()))
                        .build())
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.empty())
                .bodyToMono(RepairBotTaskResult.class)
                .toProcessor().block();
    }
}
