/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.serv.component;

import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.insta.api.IfsAccountIsCreated;
import org.dbs24.insta.api.IfsGetFollowersTask;
import org.dbs24.insta.api.ifsBuilderTask;
import org.dbs24.insta.api.rest.AccountInfo;
import org.dbs24.insta.api.rest.CreatedAccount;
import org.dbs24.insta.serv.kafka.InstaServKafkaService;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Optional.ofNullable;
import static org.dbs24.insta.api.consts.InstaConsts.Consumers.CON_GROUP_ID;
import static org.dbs24.insta.api.consts.InstaConsts.Topics.IFS_ACCOUNT_IS_CREATED;
import static org.dbs24.insta.api.consts.InstaConsts.Topics.IFS_TASK_BUILDER;
import static org.dbs24.stmt.StmtProcessor.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Log4j2
@Service
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-serv")
public class AccountBuilderService extends AbstractApplicationService {

    @Value("${config.limit.followers:10}")
    private Integer followerLimit;

    @Value("${config.db-rest-api.address:127.0.0.1}")
    private String uriBase;

    @Value("${config.db-rest-api.store-account:/api/cre}")
    private String apiStoreInstaAccount;

    final InstaServKafkaService instaServKafkaService;

    public AccountBuilderService(InstaServKafkaService instaServKafkaService) {
        this.instaServKafkaService = instaServKafkaService;
    }

    @KafkaListener(id = IFS_TASK_BUILDER, groupId = CON_GROUP_ID, topics = IFS_TASK_BUILDER)
    public void receiveTask(Collection<ifsBuilderTask> serviceTasks) {
//        log.info("{}: receive kaffka msg: {}",
//                IFS_SERVICE_TASK, serviceTasks.size());

        log.debug("{}: receive kaffka msg: {}", IFS_TASK_BUILDER, serviceTasks.size());

    }

    @KafkaListener(id = IFS_ACCOUNT_IS_CREATED, groupId = CON_GROUP_ID, topics = IFS_ACCOUNT_IS_CREATED)
    public void confirmCreateAccount(Collection<IfsAccountIsCreated> serviceTasks) {
//        log.info("{}: receive kaffka msg: {}",
//                IFS_SERVICE_TASK, serviceTasks.size());

        log.debug("{}: receive kaffka msg: {}", IFS_ACCOUNT_IS_CREATED, serviceTasks.size());

        final Long startPoint = NLS.localDateTime2long(LocalDateTime.now());
        final Long interval = Long.valueOf(30000);

        final AtomicInteger accounts = new AtomicInteger();

        accounts.set(1);

        serviceTasks
                .stream()
                .filter(p -> (p.getMediacount() > 0) && (p.getFollowers() > followerLimit))
                .forEach(ifsAccount -> {

                    instaServKafkaService.sendGetFollwersTask(StmtProcessor.create(IfsGetFollowersTask.class, igf -> {
                        igf.setInstaId(ifsAccount.getInstaId());
                        igf.setExecuteDate(startPoint + (interval * accounts.getAndIncrement()));
                        igf.setNewMaxId(null);
                    }));

                    final Mono<AccountInfo> mono = Mono.just(StmtProcessor.create(AccountInfo.class, accountInfo -> accountInfo.assign(ifsAccount)));

                    // store 2 db
                    log.debug("{}/{}: store to db: [media: {}, followers: {}, following: {}]",
                            ifsAccount.getInstaUserName(),
                            ifsAccount.getInstaFullName(),
                            ifsAccount.getMediacount(),
                            ifsAccount.getFollowers(),
                            ifsAccount.getFollowees());

                    getWebClient()
                            .post()
                            .uri(uriBuilder
                                    -> uriBuilder
                                    .path(apiStoreInstaAccount)
                                    .build())
                            .contentType(APPLICATION_JSON)
                            .accept(APPLICATION_JSON)
                            .body(mono, AccountInfo.class)
                            .retrieve()
                            .bodyToMono(CreatedAccount.class)
                            .subscribe(createdAccount -> {

                                log.debug(" createdAccount = {}", createdAccount);

                            }, throwable -> log.error("ifsAccount: {} / {}", ifsAccount, throwable.getMessage()));

                });
    }
    //==========================================================================

    @Override
    public WebClient getWebClient() {

        return ofNullable(super.getWebClient())
                .orElseGet(() -> {
                    assertNotNull(String.class,
                            uriBase,
                            "parameter ${config.db-rest-api.address}'");
                    setWebClient(WebClient.builder()
                            .baseUrl(uriBase)
                            .exchangeStrategies(ExchangeStrategies.builder()
                                    .codecs(configurer -> configurer
                                    .defaultCodecs()
                                    .maxInMemorySize(16 * 1024 * 1024))
                                    .build())
                            .build());
                    log.debug("webClient: successfull created ({})", uriBase);
                    return super.getWebClient();
                });
    }

}
