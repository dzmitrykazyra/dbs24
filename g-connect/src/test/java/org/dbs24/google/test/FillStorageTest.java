package org.dbs24.google.test;

import lombok.extern.log4j.Log4j2;
import org.dbs24.google.GoogleConnectionApplication;
import org.dbs24.google.api.ExecOrderAction;
import org.dbs24.google.api.FlagContentReason;
import org.dbs24.google.api.OrderActionResult;
import org.dbs24.google.api.OrderActionsConsts;
import org.dbs24.google.api.dto.GmailAccountInfo;
import org.dbs24.google.api.dto.ProxyInfo;
import org.dbs24.google.dao.NewTasksStorage;
import org.dbs24.google.service.TaskService;
import org.dbs24.stmt.StmtProcessor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.dbs24.consts.SysConst.ALL_PACKAGES;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {GoogleConnectionApplication.class})
@Import({GoogleConnectionApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
@TestPropertySource(properties = "config.security.profile.webfilter.chain=development")
public class FillStorageTest { //extends AbstractWebTest {

    private final String KEYWORD_TO_SEARCH = "wa admin";
    private final String APP_PACKAGE_NAME = "com.trackerapp.waadmin";
    private final String REVIEW_COMMENT_TEXT = "Easy to use. Cool app";
    private final String FLAG_CONTENT_COMMENT = "Авторское право";
    private final Integer AMOUNT_STARS_REVIEW = 5;
    private final String COMMENT_ID_TO_RATE = "gp%3AAOqpTOHGlUunvlgFAz7ak2IHz_hFls0C3XY4kBkBYwg9wjvOPwuIsZTarJ8vqwfslzxcCoPbkBzvLbQ4YbNrzg";

    private TaskService taskService;
    private NewTasksStorage newTasksStorage;

    @Autowired
    public FillStorageTest(TaskService taskService, NewTasksStorage newTasksStorage) {

        this.taskService = taskService;
        this.newTasksStorage = newTasksStorage;
    }

    @RepeatedTest(1)
    @Order(100)
    public void assignTaskTest() {

        Assertions.assertTrue(true);

        ExecOrderAction taskToExecute = createSearchTask();

        OrderActionResult taskExecutionResult = taskService.execute(taskToExecute);

        log.info("Result {}", taskExecutionResult);
    }


    @RepeatedTest(100)
    public void testProxy() throws InterruptedException {
        ExecOrderAction searchTask = createSearchTask();
        newTasksStorage.addAll(List.of(searchTask));
        Thread.sleep(100000);
    }

    @RepeatedTest(1)
    @Order(200)
    public void fillStorage() throws InterruptedException {


        ExecOrderAction searchTask = createSearchTask();
//        SEARCH
        newTasksStorage.addAll(List.of(searchTask));
        Thread.sleep(15000);

//        INSTALL
        newTasksStorage.addAll(List.of(createInstallTask()));
        Thread.sleep(20000);

//        SEND ACTIVITIES
        newTasksStorage.addAll(List.of(createSendActivityTask()));
        Thread.sleep(20000);

//        REVIEW
        newTasksStorage.addAll(List.of(createReviewTask()));
        Thread.sleep(15000);

//        FLAG CONTENT
        newTasksStorage.addAll(List.of(createFlagContentTask()));
        Thread.sleep(15000);

//        RATE COMMENT
        newTasksStorage.addAll(List.of(createRateCommentTask()));

        Thread.sleep(10000);
    }

    private GmailAccountInfo createGmailAccount() {

        return StmtProcessor.create(GmailAccountInfo.class, info -> {
            info.setEmail("rolencomono@gmail.com");
            info.setPass("jvh1m2v1");
            info.setPhoneConfigName("device-nxtl09");
            info.setGmailToken("ya29.a0ARrdaM-EfDbyJGq__-g6Of7KuxgctVagWNc_0zV91gVCLx8RvfaZiJ17KO21KIsam0vvzDnfTIGwXR-kK7l02T5Pr8sacteg_UPNlcVg-rmiMZD3QpirGYzxBP0I0ODb4hBtql31iGVwoYvdsG4sHvZ_nVE78RW77UqmCl9_KI4LAnHw7w5NryXr-R_6okOmPi39Ne_O1oYhOJ5oBnls-qUKJ5TdzABjUCrSuLUEqEsnhUIq_ExR_lH2Ro1Toc5U0YkIGQxppD3A0hGjBqUzoM97xL9FlIbYnUufKnH24qEhoJ-T2r3haH7meIyP6X19zrupRPya5lwch6PuZ3v85j0");
            info.setAasToken("aas_et/AKppINaJT3rQIVV_BSSwMfJ8fkR66-ya8OX5WqjV1DJGf0dnIeQFtHXZYIMhnwETyOJ_xANesim27cd0xEIrvHUg4pi_Y0BHxQ-lNCCBVdevlSjtsaYaV42InykiVHq-PaahKFtmugQHFw03RdS6aDV6fHyM5JaT3gmNr6rgwbjh3V9kFSj5F1x4JgzXzqnLHe5JccowQPBAB6QPXCb1HQA=");
            info.setGsfId("3016d9917bb3a0ab");
            info.setGplayToken("ya29.a0ARrdaM__2ibEQqcfsJmdPQYrSk5t194JkrLWT-YvXRrTinkNiXr1IdDByC9TVRuBlkQSK5mvAHa0ZLRRHCNIxbNrZULhgPq1n6KXooigMt1bz9ph7SDG5RCmdsfCWBC5SOtSYirNl5Aa8GQsZLfpj2HQk1VR4fjN-AA_92dHdkmx-Zz89LdkwllTkr1FwUvJxslAR1Ry4ocH0v5zi1_XXY3JDmNKYrOdqoY46w1kai5GJY2s2KJSeMAhXaNOp9hFhPJEeN3L8sCuT_kCIT6Z6HZMUQAm7cjgNnxcy-A__7_KFkt4NGzFubzzgGSOz0fP");
        });
    }

    private ProxyInfo createProxyInfo() {

        return StmtProcessor.create(ProxyInfo.class, info -> {
            info.setUrl("45.92.174.231");
            info.setPort(64425);
            info.setLogin("gr1V8qxT");
            info.setPass("9A58PCRR");
        });
    }

    private ExecOrderAction createSearchTask() {

        return StmtProcessor.create(
                ExecOrderAction.class,
                execOrderAction -> {
                    execOrderAction.setKeyWordToSearch(KEYWORD_TO_SEARCH);
                    execOrderAction.setAppPackage(APP_PACKAGE_NAME);
                    execOrderAction.setActRefId(OrderActionsConsts.ActionEnum.USER_SEARCH.getCode());
                    execOrderAction.setGmailAccountInfo(createGmailAccount());
                    execOrderAction.setProxyInfo(createProxyInfo());
                }
        );
    }

    private ExecOrderAction createInstallTask() {

        return StmtProcessor.create(
                ExecOrderAction.class,
                execOrderAction -> {
                    execOrderAction.setKeyWordToSearch(KEYWORD_TO_SEARCH);
                    execOrderAction.setAppPackage(APP_PACKAGE_NAME);
                    execOrderAction.setActRefId(OrderActionsConsts.ActionEnum.INSTALL_ACTION.getCode());
                    execOrderAction.setGmailAccountInfo(createGmailAccount());
                    execOrderAction.setProxyInfo(createProxyInfo());
                }
        );
    }

    private ExecOrderAction createSendActivityTask() {

        return StmtProcessor.create(
                ExecOrderAction.class,
                execOrderAction -> {
                    execOrderAction.setKeyWordToSearch(KEYWORD_TO_SEARCH);
                    execOrderAction.setAppPackage(APP_PACKAGE_NAME);
                    execOrderAction.setActRefId(OrderActionsConsts.ActionEnum.APPLICATION_ACTIVITY.getCode());
                    execOrderAction.setGmailAccountInfo(createGmailAccount());
                    execOrderAction.setProxyInfo(createProxyInfo());
                }
        );
    }

    private ExecOrderAction createReviewTask() {

        return StmtProcessor.create(
                ExecOrderAction.class,
                execOrderAction -> {
                    execOrderAction.setKeyWordToSearch(KEYWORD_TO_SEARCH);
                    execOrderAction.setAppPackage(APP_PACKAGE_NAME);
                    execOrderAction.setComment(REVIEW_COMMENT_TEXT);
                    execOrderAction.setStarsQuantity(AMOUNT_STARS_REVIEW);
                    execOrderAction.setActRefId(OrderActionsConsts.ActionEnum.REVIEW_ACTION.getCode());
                    execOrderAction.setGmailAccountInfo(createGmailAccount());
                    execOrderAction.setProxyInfo(createProxyInfo());
                }
        );
    }

    private ExecOrderAction createFlagContentTask() {

        return StmtProcessor.create(
                ExecOrderAction.class,
                execOrderAction -> {
                    execOrderAction.setKeyWordToSearch(KEYWORD_TO_SEARCH);
                    execOrderAction.setAppPackage(APP_PACKAGE_NAME);
                    execOrderAction.setComment(FLAG_CONTENT_COMMENT);
                    execOrderAction.setFlagContentName(FlagContentReason.COPYCAT_OR_IMPERSONATION.name());
                    execOrderAction.setActRefId(OrderActionsConsts.ActionEnum.FLAG_CONTENT.getCode());
                    execOrderAction.setGmailAccountInfo(createGmailAccount());
                    execOrderAction.setProxyInfo(createProxyInfo());
                }
        );
    }

    private ExecOrderAction createRateCommentTask() {

        return StmtProcessor.create(
                ExecOrderAction.class,
                execOrderAction -> {
                    execOrderAction.setKeyWordToSearch(KEYWORD_TO_SEARCH);
                    execOrderAction.setAppPackage(APP_PACKAGE_NAME);
                    execOrderAction.setIsCommentHelpful(true);
                    execOrderAction.setCommentIdToRate(COMMENT_ID_TO_RATE);
                    execOrderAction.setActRefId(OrderActionsConsts.ActionEnum.RATE_COMMENT_ACTION.getCode());
                    execOrderAction.setGmailAccountInfo(createGmailAccount());
                    execOrderAction.setProxyInfo(createProxyInfo());
                }
        );
    }

}
