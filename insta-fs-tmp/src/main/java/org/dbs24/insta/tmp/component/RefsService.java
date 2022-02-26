/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.component;

import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.insta.tmp.repo.*;
import org.dbs24.insta.tmp.entity.*;
import static org.dbs24.insta.tmp.consts.IfsConst.References.AccountStatuses.*;
import static org.dbs24.insta.tmp.consts.IfsConst.References.PostStatuses.*;
import static org.dbs24.insta.tmp.consts.IfsConst.References.PostTypes.*;
import static org.dbs24.insta.tmp.consts.IfsConst.References.SourceStatuses.*;
import static org.dbs24.insta.tmp.consts.IfsConst.References.BotStatuses.*;
import static org.dbs24.insta.tmp.consts.IfsConst.References.TaskTypes.*;
import static org.dbs24.insta.tmp.consts.IfsConst.References.TaskResult.*;
import static org.dbs24.insta.tmp.consts.IfsConst.Caches.*;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Data
@EnableScheduling
@EnableAsync
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-tmp")
public class RefsService extends AbstractApplicationService {

    final AccountStatusRepo accountStatusRepo;
    final PostStatusRepo postStatusRepo;
    final PostTypeRepo postTypeRepo;
    final SourceStatusRepo sourceStatusRepo;
    final BotStatusRepo botStatusRepo;
    final TaskTypeRepo taskTypeRepo;
    final TaskResultRepo taskResultRepo;

    public RefsService(AccountStatusRepo accountStatusRepo, PostStatusRepo postStatusRepo, PostTypeRepo postTypeRepo, SourceStatusRepo sourceStatusRepo, BotStatusRepo botStatusRepo, TaskTypeRepo taskTypeRepo, TaskResultRepo taskResultRepo) {
        this.accountStatusRepo = accountStatusRepo;
        this.postStatusRepo = postStatusRepo;
        this.postTypeRepo = postTypeRepo;
        this.sourceStatusRepo = sourceStatusRepo;
        this.botStatusRepo = botStatusRepo;
        this.taskTypeRepo = taskTypeRepo;
        this.taskResultRepo = taskResultRepo;
    }

//
    //==========================================================================
    @Cacheable(CACHE_ACCOUNT_STATUS)
    public AccountStatus findAccountStatus(Integer accountStatusId) {
        return accountStatusRepo.findById(accountStatusId).orElseThrow(() -> new RuntimeException("AccountStatus is unknown or not found - " + accountStatusId.toString()));
    }

    //==========================================================================
    @Cacheable(CACHE_POST_STATUS)
    public PostStatus findPostStatus(Integer postStatusId) {
        return postStatusRepo.findById(postStatusId).orElseThrow(() -> new RuntimeException("PostStatus is unknown or not found - " + postStatusId.toString()));
    }

    //==========================================================================
    @Cacheable(CACHE_POST_TYPE)
    public PostType findPostType(Integer postTypeId) {
        return postTypeRepo.findById(postTypeId).orElseThrow(() -> new RuntimeException("PostType is unknown or not found - " + postTypeId.toString()));
    }

    //==========================================================================
    @Cacheable(CACHE_SOURCE_STATUS)
    public SourceStatus findSourceStatus(Integer sourceStatusId) {
        return sourceStatusRepo.findById(sourceStatusId).orElseThrow(() -> new RuntimeException("Source is unknown or not found - " + sourceStatusId.toString()));
    }

    //==========================================================================
    @Cacheable(CACHE_BOT_STATUS)
    public BotStatus findBotStatus(Integer BotStatusId) {
        return botStatusRepo.findById(BotStatusId).orElseThrow(() -> new RuntimeException("BotStatus is unknown or not found - " + BotStatusId.toString()));
    }

    //==========================================================================
    @Cacheable(CACHE_TASK_TYPE)
    public TaskType findTaskType(Integer TaskTypeId) {
        return taskTypeRepo.findById(TaskTypeId).orElseThrow(() -> new RuntimeException("TaskType is unknown or not found - " + TaskTypeId.toString()));
    }

    //==========================================================================
    @Cacheable(CACHE_TASK_RESULT)
    public TaskResult findTaskResult(Integer taskResultId) {
        return taskResultRepo.findById(taskResultId).orElseThrow(() -> new RuntimeException("TaskResult is unknown or not found - " + taskResultId.toString()));
    }
    
    //==========================================================================
    @Transactional
    public void synchronizeRefs() {

        log.info("synchronize system references");

        accountStatusRepo.saveAll(Arrays.stream(ALL_ACCOUNT_STATUSES)
                .map(stringRow -> StmtProcessor.create(AccountStatus.class, record -> {
            record.setAccountStatusId(Integer.valueOf(stringRow[0]));
            record.setAccountStatusName(stringRow[1]);
        })).collect(Collectors.toList()));

        postStatusRepo.saveAll(Arrays.stream(ALL_POST_STATUSES)
                .map(stringRow -> StmtProcessor.create(PostStatus.class, record -> {
            record.setPostStatusId(Integer.valueOf(stringRow[0]));
            record.setPostStatusName(stringRow[1]);
        })).collect(Collectors.toList()));

        postTypeRepo.saveAll(Arrays.stream(ALL_POST_TYPES)
                .map(stringRow -> StmtProcessor.create(PostType.class, record -> {
            record.setPostTypeId(Integer.valueOf(stringRow[0]));
            record.setPostTypeName(stringRow[1]);
        })).collect(Collectors.toList()));

        sourceStatusRepo.saveAll(Arrays.stream(ALL_SOURCE_STATUSES)
                .map(stringRow -> StmtProcessor.create(SourceStatus.class, record -> {
            record.setSourceStatusId(Integer.valueOf(stringRow[0]));
            record.setSourceStatusName(stringRow[1]);
        })).collect(Collectors.toList()));

        botStatusRepo.saveAll(Arrays.stream(ALL_BOT_STATUSES)
                .map(stringRow -> StmtProcessor.create(BotStatus.class, record -> {
            record.setBotSStatusId(Integer.valueOf(stringRow[0]));
            record.setBotStstuaName(stringRow[1]);
        })).collect(Collectors.toList()));        
        
        taskTypeRepo.saveAll(Arrays.stream(ALL_TASK_TYPES)
                .map(stringRow -> StmtProcessor.create(TaskType.class, record -> {
            record.setTaskTypeId(Integer.valueOf(stringRow[0]));
            record.setTaskTypeName(stringRow[1]);
        })).collect(Collectors.toList()));
        
        taskResultRepo.saveAll(Arrays.stream(ALL_TASK_RESULTS)
                .map(stringRow -> StmtProcessor.create(TaskResult.class, record -> {
            record.setTaskResultId(Integer.valueOf(stringRow[0]));
            record.setTaskResultName(stringRow[1]);
        })).collect(Collectors.toList()));        
    }
}
