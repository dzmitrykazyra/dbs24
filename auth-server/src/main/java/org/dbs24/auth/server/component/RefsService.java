/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.auth.server.component;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.auth.server.consts.AuthConsts.ServerEventEnum;
import org.dbs24.auth.server.consts.AuthConsts.ServerStatusEnum;
import org.dbs24.auth.server.entity.Application;
import org.dbs24.auth.server.entity.ServerEvent;
import org.dbs24.auth.server.entity.ServerStatus;
import org.dbs24.auth.server.repo.ApplicationRepo;
import org.dbs24.auth.server.repo.ServerEventRepo;
import org.dbs24.auth.server.repo.ServerStatusRepo;
import org.dbs24.auth.server.repo.TokenCardRepo;
import org.dbs24.service.AbstractReferencesService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.toList;
import static org.dbs24.auth.server.consts.AuthConsts.Caches.*;
import static org.dbs24.auth.server.consts.AuthConsts.ServerEventEnum.SERVER_EVENTS_LIST;
import static org.dbs24.auth.server.consts.AuthConsts.ServerStatusEnum.SERVER_STATUSES_LIST;
import static org.dbs24.stmt.StmtProcessor.create;

@Getter
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "refs")
@Log4j2
@Component
@EqualsAndHashCode(callSuper = true)
public class RefsService extends AbstractReferencesService {

    @Value("${config.issue-card.shelf-life.term:30}")
    private Integer issueCardShelfLifeTerm;

    @Setter
    private Map<Integer, String> applications;

    final ApplicationRepo applicationRepo;
    final ServerStatusRepo serverStatusRepo;
    final ServerEventRepo serverEventRepo;
    final TokenCardRepo tokenCardRepo;

    public RefsService(ApplicationRepo applicationRepo, ServerStatusRepo serverStatusRepo, ServerEventRepo serverEventRepo, TokenCardRepo tokenCardRepo) {
        this.applicationRepo = applicationRepo;
        this.serverStatusRepo = serverStatusRepo;
        this.serverEventRepo = serverEventRepo;
        this.tokenCardRepo = tokenCardRepo;
    }

    //==========================================================================
    @Cacheable(CACHE_APPLICATION)
    public Application findApplication(Integer applicationId) {

        return applicationRepo.findById(applicationId).orElseThrow();
    }

    @Cacheable(CACHE_SERVER_STATUS)
    public ServerStatus findServerStatus(ServerStatusEnum serverStatusEnum) {
        return findServerStatus(serverStatusEnum.getCode());
    }

    private ServerStatus findServerStatus(Integer serverStatusId) {
        return serverStatusRepo.findById(serverStatusId).orElseThrow();
    }

    @Cacheable(CACHE_SERVER_EVENTS)
    public ServerEvent findServerEvent(ServerEventEnum serverEventEnum) {

        return serverEventRepo.findById(serverEventEnum.getCode()).orElseThrow();
    }

    public ServerEvent findServerEvent(Integer serverEventId) {

        return serverEventRepo.findById(serverEventId).orElseThrow();
    }

    @Transactional
    public void synchronizeRefs() {

        log.info("synchronize system references");

        //======================================================================
        StmtProcessor.assertNotNull(Map.class, applications, "accountStatuses is not initialized");

        applicationRepo.saveAll(
                applications
                        .entrySet()
                        .stream()
                        .map(entry -> create(Application.class, record -> {
                            record.setApplicationId(Integer.valueOf(entry.getKey()));

                            final String value = entry.getValue();

                            final String[] recs = value.split(";");

                            final String code = recs[0];
                            final String name = recs[1];

                            record.setApplicationCode(code);
                            record.setApplicationName(name);
                        })).collect(toList()));

        log.info("initialize aplications refs: {} record(s)", applications.size());


        serverStatusRepo.saveAll(SERVER_STATUSES_LIST);
        serverEventRepo.saveAll(SERVER_EVENTS_LIST);

    }

    @Scheduled(fixedRateString = "${config.issue-card.period:3600000}", cron = "${config.issue-card.period.processing-cron:}")
    public void deleteDeprecatedIssueCards() {

        final var deprecateDate = now().minusDays(issueCardShelfLifeTerm);
        log.debug("process deprecated issue cards (before {}, {} days)", deprecateDate, issueCardShelfLifeTerm);

        tokenCardRepo.deleteDeprecatedIssueCards(deprecateDate);

    }
}
