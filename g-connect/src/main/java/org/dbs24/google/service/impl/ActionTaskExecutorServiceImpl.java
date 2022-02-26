package org.dbs24.google.service.impl;

import com.github.yeriomin.playstoreapi.AppDetails;
import com.github.yeriomin.playstoreapi.DetailsResponse;
import com.github.yeriomin.playstoreapi.GooglePlayAPIExtended;
import com.github.yeriomin.playstoreapi.clog.ActivityLogRequest;
import com.github.yeriomin.playstoreapi.exception.account.ApiBuilderException;
import lombok.extern.log4j.Log4j2;
import org.dbs24.google.api.ExecOrderAction;
import org.dbs24.google.api.OrderActionResult;
import org.dbs24.google.api.OrderActionsConsts;
import org.dbs24.google.service.ActionTaskExecutorService;
import org.dbs24.google.service.ApiInteractService;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static org.dbs24.google.constant.ActivityInfo.*;

@Service
@Log4j2
public class ActionTaskExecutorServiceImpl implements ActionTaskExecutorService {
    private final ApiInteractService apiInteractService;

    public ActionTaskExecutorServiceImpl(ApiInteractService apiInteractService) {
        this.apiInteractService = apiInteractService;
    }

    @Override
    public OrderActionResult install(ExecOrderAction taskToExecute) throws IOException, ApiBuilderException {
        log.info("Started install task executing with id {}", taskToExecute.getActionId());

        GooglePlayAPIExtended api = apiInteractService.getGPlayApi(taskToExecute);
        api.simulateDownloading(taskToExecute.getAppPackage());
        apiInteractService.updateTokenIfExpired(api, taskToExecute.getGmailAccountInfo());

        return OrderActionResult.of(taskToExecute, OrderActionsConsts.ActionResultEnum.OK_FINISHED);
    }

    @Override
    public OrderActionResult rateComment(ExecOrderAction taskToExecute) throws IOException, ApiBuilderException {
        log.info("Started rate comment task executing with id {}", taskToExecute.getActionId());

        GooglePlayAPIExtended api = apiInteractService.getGPlayApi(taskToExecute);
        DetailsResponse details = api.details(taskToExecute.getAppPackage());
        api.rateReview(
                taskToExecute.getAppPackage(), taskToExecute.getCommentIdToRate(), taskToExecute.getIsCommentHelpful()
        );
        apiInteractService.updateTokenIfExpired(api, taskToExecute.getGmailAccountInfo());

        return OrderActionResult.of(taskToExecute, OrderActionsConsts.ActionResultEnum.OK_FINISHED);
    }

    @Override
    public OrderActionResult review(ExecOrderAction taskToExecute) throws IOException, ApiBuilderException {
        log.info("Started review task executing with id {}", taskToExecute.getActionId());

        GooglePlayAPIExtended api = apiInteractService.getGPlayApi(taskToExecute);
        DetailsResponse detailsResponse = api.details(taskToExecute.getAppPackage());
        api.addOrEditReview(
                taskToExecute.getAppPackage(),
                taskToExecute.getComment(),
                "",
                taskToExecute.getStarsQuantity()
        );
        apiInteractService.updateTokenIfExpired(api, taskToExecute.getGmailAccountInfo());

        return OrderActionResult.of(taskToExecute, OrderActionsConsts.ActionResultEnum.OK_FINISHED);
    }

    @Override
    public OrderActionResult flagContent(ExecOrderAction taskToExecute) throws IOException, ApiBuilderException {
        log.info("Started flag content task executing with id {}", taskToExecute.getActionId());

        GooglePlayAPIExtended.FLAG_INAPPROPRIATE_REASON flagContentReason = GooglePlayAPIExtended
                .FLAG_INAPPROPRIATE_REASON
                .valueOf(taskToExecute.getFlagContentName());

        GooglePlayAPIExtended api = apiInteractService.getGPlayApi(taskToExecute);
        DetailsResponse detailsResponse = api.details(taskToExecute.getAppPackage());
        api.flagContent(
                taskToExecute.getAppPackage(), flagContentReason, taskToExecute.getComment()
        );
        apiInteractService.updateTokenIfExpired(api, taskToExecute.getGmailAccountInfo());

        return OrderActionResult.of(taskToExecute, OrderActionsConsts.ActionResultEnum.OK_FINISHED);
    }

    @Override
    public OrderActionResult applicationActivity(ExecOrderAction taskToExecute) throws Exception {
        log.info("Started application activity task executing with id {}", taskToExecute.getActionId());

        GooglePlayAPIExtended api = apiInteractService.getGPlayApi(taskToExecute);
        api.experimentsAndConfigs();
        ActivityLogRequest activity = new ActivityLogRequest(api);
        addActivities(
                activity,
                taskToExecute.getAppPackage(),
                api.details(taskToExecute.getAppPackage()).getDocV2().getDetails().getAppDetails()
        );
        activity.sendLogRequest();

        return OrderActionResult.of(taskToExecute, OrderActionsConsts.ActionResultEnum.OK_FINISHED);
    }

    @Override
    public OrderActionResult simulateSearch(ExecOrderAction taskToExecute) throws IOException, ApiBuilderException {
        log.info("Started search task executing with id {}", taskToExecute.getActionId());

        GooglePlayAPIExtended api = apiInteractService.getGPlayApi(taskToExecute);
        api.simulateSearch(taskToExecute.getKeyWordToSearch());
        apiInteractService.updateTokenIfExpired(api, taskToExecute.getGmailAccountInfo());

        return OrderActionResult.of(taskToExecute, OrderActionsConsts.ActionResultEnum.OK_FINISHED);
    }

    private void addActivities(ActivityLogRequest activity, String appPackage, AppDetails appDetails) throws Exception {
        activity.addActivity(
                ACTIVITY_NEXUS_PACKAGE_NAME,
                ACTIVITY_NEXUS_NAME,
                System.currentTimeMillis(),
                ACTIVITY_NEXUS_VERSION_NAME,
                ACTIVITY_NEXUS_VERSION_CODE
        );

        activity.addActivity(
                ACTIVITY_ANDROID_VENDING_PACKAGE_NAME,
                ACTIVITY_ANDROID_VENDING_NAME,
                System.currentTimeMillis(),
                ACTIVITY_ANDROID_VENDING_VERSION_NAME,
                ACTIVITY_ANDROID_VENDING_VERSION_CODE
        );

        activity.addActivity(
                appPackage,
                appPackage + MAIN_ACTIVITY,
                System.currentTimeMillis(),
                appDetails.getVersionString(),
                appDetails.getVersionCode()
        );

        activity.addActivity(
                ACTIVITY_FACEBOOK_PACKAGE_NAME,
                ACTIVITY_FACEBOOK_NAME,
                System.currentTimeMillis(),
                ACTIVITY_FACEBOOK_VERSION_NAME,
                ACTIVITY_FACEBOOK_VERSION_CODE
        );
    }
}