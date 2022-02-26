/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.component;

import java.time.LocalDateTime;
import org.dbs24.insta.reg.repo.*;
import org.dbs24.insta.reg.entity.*;
import java.util.Map;
import java.util.Collection;
import java.util.Random;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;
import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.Transactional;
import static org.dbs24.insta.reg.consts.InstaConsts.ProxyStatuses.*;
import static org.dbs24.insta.reg.consts.InstaConsts.ProxyTypes.*;

@Data
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "refs")
@EnableScheduling
@EnableAsync
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-reg")
public class RefsService extends AbstractApplicationService {

    private Map<Integer, String> accountStatuses;
    private Map<Integer, String> actionsRefs;
    private Map<Integer, String> proxyStatuses;
    private Map<Integer, String> proxyTypes;
    private Map<Integer, String> actionBatchesRef;
    private Map<Integer, String> actionBatches;
    private Map<Integer, String> emailStatusesRef;
    private Map<Integer, String> proxiesList;
    private Map<Integer, String> userAgents;

    final AccountStatusRepo accountStatusRepo;
    final ProxyStatusRepo proxyStatusRepo;
    final ProxyTypeRepo proxyTypeRepo;
    final ProxyRepo proxyRepo;
    final ActionRepo actionRepo;
    final BatchRepo batchesRepo;
    final BatchActionRepo batchActionRepo;
    final EmailStatusesRepo emailStatusesRepo;
    final Random random = new Random();

    public RefsService(AccountStatusRepo accountStatusRepo, ActionRepo actionRepo, ProxyStatusRepo proxyStatusRepo, ProxyTypeRepo proxyTypeRepo, BatchRepo batchesRepo, EmailStatusesRepo emailStatusesRepo, ProxyRepo proxyRepo, BatchActionRepo batchActionRepo) {
        this.accountStatusRepo = accountStatusRepo;
        this.actionRepo = actionRepo;
        this.proxyStatusRepo = proxyStatusRepo;
        this.proxyTypeRepo = proxyTypeRepo;
        this.batchesRepo = batchesRepo;
        this.emailStatusesRepo = emailStatusesRepo;
        this.proxyRepo = proxyRepo;
        this.batchActionRepo = batchActionRepo;
    }

    //==========================================================================
    @Cacheable("actionsRef")
    public Action findAction(Integer actionRefId) {

        return actionRepo.findById(actionRefId).orElseThrow();
    }

    //==========================================================================
    @Cacheable("emailStatusesRef")
    public EmailStatus findEmailStatus(Byte emailStatusId) {

        return emailStatusesRepo.getOne(emailStatusId);
    }

    //==========================================================================
    @Cacheable("accountStatusesRef")
    public AccountStatus findAccountStatus(Byte accountStatusId) {

        return accountStatusRepo.getOne(accountStatusId);
    }

    //==========================================================================
    @Cacheable("batchesRef")
    public Batch findBatch(Integer batchId) {

        return batchesRepo.getOne(batchId);
    }

    //==========================================================================
    @Cacheable("proxyStatusesRef")
    public ProxyStatus findProxyStatus(Byte proxyStatusId) {

        return proxyStatusRepo.getOne(proxyStatusId);
    }

    //==========================================================================
    @Cacheable("batchActionsRef")
    public Collection<BatchAction> findBatchActions(Integer batchId) {

        return batchActionRepo.findByBatchOrderByActionAsc(findBatch(batchId));
    }

    //==========================================================================
    @Cacheable("proxyTypesRef")
    public ProxyType findProxyType(Byte proxyTypeId) {

        return proxyTypeRepo.getOne(proxyTypeId);
    }

    //==========================================================================
    public String getUserAgent() {
        return userAgents.entrySet().stream().skip(random.nextInt(userAgents.size() - 1)).findAny().orElseThrow().getValue();
    }

    @Transactional
    public void synchronizeRefs() {

        log.info("synchronize system references");

        //======================================================================
        StmtProcessor.assertNotNull(Map.class, accountStatuses, "accountStatuses is not initialized");

        accountStatusRepo.saveAll(
                accountStatuses
                        .entrySet()
                        .stream()
                        .map(entry -> StmtProcessor.create(AccountStatus.class, record -> {
                    record.setAccountStatusId(Byte.valueOf(entry.getKey().byteValue()));
                    record.setAccountStatusName(entry.getValue());
                })).collect(Collectors.toList()));

        log.info("initialize accountStatuses: {} record(s)", accountStatuses.size());
        //======================================================================
        StmtProcessor.assertNotNull(Map.class, proxyStatuses, "proxyStatuses is not initialized");

        proxyStatusRepo.saveAll(
                proxyStatuses
                        .entrySet()
                        .stream()
                        .map(entry -> StmtProcessor.create(ProxyStatus.class, record -> {
                    record.setProxyStatusId(Byte.valueOf(entry.getKey().byteValue()));
                    record.setProxyStatusName(entry.getValue());
                })).collect(Collectors.toList()));

        log.info("initialize proxyStatuses: {} record(s)", proxyStatuses.size());
        //======================================================================
        StmtProcessor.assertNotNull(Map.class, proxyTypes, "proxyTypes is not initialized");

        proxyTypeRepo.saveAll(
                proxyTypes
                        .entrySet()
                        .stream()
                        .map(entry -> StmtProcessor.create(ProxyType.class, record -> {
                    record.setProxyTypeId(Byte.valueOf(entry.getKey().byteValue()));
                    record.setProxyTypeName(entry.getValue());
                })).collect(Collectors.toList()));

        log.info("initialize proxyTypes: {} record(s)", proxyStatuses.size());

        //======================================================================
        StmtProcessor.assertNotNull(Map.class, actionsRefs, "actionsRefs is not initialized");

        actionRepo.saveAll(
                actionsRefs
                        .entrySet()
                        .stream()
                        .map(entry -> StmtProcessor.create(Action.class, record -> {
                    record.setActionRefId(entry.getKey());
                    record.setActtionRefName(entry.getValue());
                })).collect(Collectors.toList()));

        log.info("initialize actionsRefs: {} record(s)", actionsRefs.size());
        //======================================================================
        StmtProcessor.assertNotNull(Map.class, actionBatchesRef, "actionBatchesRef is not initialized");

        batchesRepo.saveAll(
                actionBatchesRef
                        .entrySet()
                        .stream()
                        .map(entry -> StmtProcessor.create(Batch.class, record -> {
                    record.setBatchId(entry.getKey());
                    record.setBatchName(entry.getValue());
                })).collect(Collectors.toList()));

        log.info("initialize actionBatchesRef: {} record(s)", actionBatchesRef.size());
        //======================================================================
        StmtProcessor.assertNotNull(Map.class, actionBatches, "actionBatches is not initialized");

        batchActionRepo.saveAll(
                actionBatches
                        .entrySet()
                        .stream()
                        .map(entry -> StmtProcessor.create(BatchAction.class, record -> {

                    final String batchInfo = entry.getValue();

                    final String[] recs = batchInfo.split(";");

                    final Integer batchNum = Integer.valueOf(recs[0]);
                    final Integer actionNum = Integer.valueOf(recs[1]);
                    final Integer actionDelay = Integer.valueOf(recs[2]);

                    final Batch batch = batchesRepo.findById(batchNum).orElseThrow(() -> new RuntimeException(String.format("Unknown batch - %d", batchNum)));
                    final Action action = actionRepo.findById(actionNum).orElseThrow(() -> new RuntimeException(String.format("Unknown action code - %d", actionNum)));

                    //log.info("register new batch = {}, action = {} ", batch, action);
                    record.setBatch(batch);
                    record.setAction(action);
                    record.setActionDelay(actionDelay);

                })).collect(Collectors.toList()));

        log.info("initialize actionBatches: {} record(s)", actionBatches.size());

        //======================================================================
        StmtProcessor.assertNotNull(Map.class, emailStatusesRef, "emailStatusesRef is not initialized");

        emailStatusesRepo.saveAll(
                emailStatusesRef
                        .entrySet()
                        .stream()
                        .map(entry -> StmtProcessor.create(EmailStatus.class, record -> {
                    record.setEmailStatusId(Byte.valueOf(entry.getKey().byteValue()));
                    record.setEmailStatusName(entry.getValue());
                })).collect(Collectors.toList()));

        log.info("initialize emailStatusesRef: {} record(s)", emailStatusesRef.size());

        //======================================================================
        StmtProcessor.assertNotNull(Map.class, proxiesList, "proxiesList is not initialized");

        proxyRepo.saveAll(
                proxiesList
                        .entrySet()
                        .stream()
                        .map(entry -> {

                            final String proxyInfo = entry.getValue();

                            final String[] recs = proxyInfo.split(";");

                            final String address = recs[0];
                            final String credit = recs[1];

                            final Proxy proxy = proxyRepo.findByAddress(address)
                                    .orElseGet(() -> StmtProcessor.create(Proxy.class, record -> {
                                record.setCredit(credit);
                                record.setAddress(address);
                                record.setCreateDate(LocalDateTime.now());
                                record.setActualDate(LocalDateTime.now());
                                record.setProxyStatus(findProxyStatus(PS_ACTUAL));
                                record.setProxyType(findProxyType(PT_MOBILE));
                                record.setProxyNotes(proxyInfo);

                                log.info("register new proxy: {}, {}", address, credit);

                            }));

                            proxy.setCredit(credit);

                            return proxy;

                        }).collect(Collectors.toList()));

        log.info("initialize proxiesList: {} record(s)", proxiesList.size());
    }

}
