/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.fs.component;

import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.insta.fs.repo.*;
import org.dbs24.insta.fs.entity.*;
import static org.dbs24.insta.fs.consts.IfsConst.References.AccountStatuses.*;
import static org.dbs24.insta.fs.consts.IfsConst.References.PostStatuses.*;
import static org.dbs24.insta.fs.consts.IfsConst.References.PostTypes.*;
import static org.dbs24.insta.fs.consts.IfsConst.References.SourceStatuses.*;
import static org.dbs24.insta.fs.consts.IfsConst.References.ActionResult.*;
import static org.dbs24.insta.fs.consts.IfsConst.References.ActionType.*;
import static org.dbs24.insta.fs.consts.IfsConst.References.TaskStatuses.*;
import static org.dbs24.insta.fs.consts.IfsConst.Caches.*;
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
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-fs")
public class RefsService extends AbstractApplicationService {

    final AccountStatusRepo accountStatusRepo;
    final PostStatusRepo postStatusRepo;
    final PostTypeRepo postTypeRepo;
    final SourceStatusRepo sourceStatusRepo;
    final ActionResultRepo actionResultRepo;
    final ActionTypeRepo actionTypeRepo;
    final TaskStatusRepo taskStatusRepo;

    public RefsService(AccountStatusRepo accountStatusRepo, PostStatusRepo postStatusRepo, PostTypeRepo postTypeRepo, SourceStatusRepo sourceStatusRepo, ActionResultRepo actionResultRepo, ActionTypeRepo actionTypeRepo, TaskStatusRepo taskStatusRepo) {
        this.accountStatusRepo = accountStatusRepo;
        this.postStatusRepo = postStatusRepo;
        this.postTypeRepo = postTypeRepo;
        this.sourceStatusRepo = sourceStatusRepo;
        this.actionResultRepo = actionResultRepo;
        this.actionTypeRepo = actionTypeRepo;
        this.taskStatusRepo = taskStatusRepo;
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
    @Transactional
    public void synchronizeRefs() {

        log.info("synchronize system references");

        actionResultRepo.saveAll(Arrays.stream(ALL_ACTION_RESULTS)
                .map(stringRow -> StmtProcessor.create(ActionResult.class, record -> {
            record.setActionResultId(Integer.valueOf(stringRow[0]));
            record.setActionResultName(stringRow[1]);
        })).collect(Collectors.toList()));

        actionTypeRepo.saveAll(Arrays.stream(ALL_ACTION_TYPES)
                .map(stringRow -> StmtProcessor.create(ActionType.class, record -> {
            record.setActionTypeId(Integer.valueOf(stringRow[0]));
            record.setActionTypeName(stringRow[1]);
        })).collect(Collectors.toList()));

        taskStatusRepo.saveAll(Arrays.stream(ALL_TASK_STATUSES)
                .map(stringRow -> StmtProcessor.create(TaskStatus.class, record -> {
            record.setTaskStatusId(Integer.valueOf(stringRow[0]));
            record.setTaskStatusName(stringRow[1]);
        })).collect(Collectors.toList()));

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
    }
}
