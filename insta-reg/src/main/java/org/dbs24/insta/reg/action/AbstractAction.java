/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.action;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.StringFuncs;
import static org.dbs24.consts.SysConst.STRING_NULL;
import static org.dbs24.consts.SysConst.BOOLEAN_TRUE;
import static org.dbs24.consts.SysConst.BOOLEAN_FALSE;
import org.dbs24.insta.reg.component.ProxyClient;
import org.dbs24.insta.reg.component.*;
import org.dbs24.insta.reg.entity.Account;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@Log4j2
public abstract class AbstractAction {

    private Byte actionCode;
    private Integer actionRefId;
    private String actionNote;
    private String errMsg;
    private Account account;

    @Autowired
    private RefsService refsService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ProxyService proxyService;

    @Autowired
    private InstaService instaService;

    private Boolean needRetry;
    private Boolean registerException = BOOLEAN_FALSE;
    private Integer attemptsLimit = 5;
    private Integer attemptsNum = 0;

    public ActionResult execute(Account account) {
        this.account = account;

        final String actionName = this.getClass().getSimpleName();

        actionCode = 0;

        final SimpleActionResult simpleActionResult = StmtProcessor.create(SimpleActionResult.class, ar -> ar.setCode(actionCode));

        log.info("execute action {}", actionName);

        do {

            needRetry = BOOLEAN_FALSE;

            actionCode = 0;
            simpleActionResult.setCode(actionCode);
            simpleActionResult.setMessage(STRING_NULL);
            simpleActionResult.setNote(STRING_NULL);

            final StmtProcessor stmtProcessor = StmtProcessor.createSilent(() -> perform());

            stmtProcessor
                    .whenIsNull(() -> BOOLEAN_FALSE)
                    .processException(e -> {

                        log.error("exception in action {}, {}", actionName, e);

                        final String thMessage = (String) StmtProcessor.nvl(e.getMessage(), "no exception message available");
                        final String errStackTrace = thMessage.concat("\n").concat(StmtProcessor.getStackTraceRaw(e));

                        if (registerException) {

                            simpleActionResult.setCode(Byte.valueOf("-9"));
                            simpleActionResult.setMessage(StringFuncs.truncString(errStackTrace, 10000));
                            simpleActionResult.setNote(actionNote);

                            onError(e);

                        } else {

                            needRetry = BOOLEAN_TRUE;
                            attemptsNum++;
                            registerException = attemptsLimit.equals(attemptsNum);

                            log.error("###({}): {}: register exception in {}: {} -> {}, {}",
                                    attemptsNum, getAccount().getFakedEmail(), actionName, e.getClass().getCanonicalName(), thMessage, errStackTrace);

                            StmtProcessor.sleep(100);
                        }

                    }).get();
        } while (needRetry);

        if (actionCode != 0) {
            log.info("{}: register fail: {}", actionName, simpleActionResult);
            simpleActionResult.setCode(actionCode);
        }
        simpleActionResult.setNote(actionNote);

        //log.info("{}: result is {}", actionName, simpleActionResult);
        return simpleActionResult;

    }

    //--------------------------------------------------------------------------
    abstract void perform();

    //--------------------------------------------------------------------------
    protected void onError(Throwable th) {
        log.error("onError: {}: {}", this.getClass().getSimpleName(), th.getMessage());
    }

    //--------------------------------------------------------------------------
    protected void registerProxyFails(Integer proxyId) {
        proxyService.registerProxyFails(proxyId);
    }

    //--------------------------------------------------------------------------
    protected ProxyClient getProxyClient() {

        final Integer proxyId = getAccount().getProxy().getProxyId();

        log.info("reserved proxy id = {}, fakedMail = {}", proxyId, getAccount().getFakedEmail());

        return proxyService
                .getProxyClients()
                .entrySet()
                .stream()
                .filter(client -> client.getKey().equals(proxyId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("proxy not found: " + proxyId))
                .getValue();
    }

    //--------------------------------------------------------------------------
//    protected void tryAction(ExecuteAction executeAction) {
//        
//    }
}
