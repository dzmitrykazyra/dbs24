/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.action;

import lombok.extern.log4j.Log4j2;
import static org.dbs24.insta.reg.consts.InstaConsts.AccountProcessing.*;
import static org.dbs24.insta.reg.consts.InstaConsts.AccountStatuses.*;
import static org.dbs24.insta.reg.consts.InstaConsts.ActionCodes.ACT_WEB_CREATE;
import org.dbs24.stmt.StmtProcessor;

@Log4j2
@ActionCodeId(ACT_WEB_CREATE)
public class ActWebCreate extends AbstractAction {

    @Override
    public void perform() {

        final AccountCreateResult accountCreateResult = getInstaService().webCreate(getProxyClient().getHttpProxyClient(), getAccount());

        setActionNote(String.format("%d: ActWebCreate: %s", accountCreateResult.getAnswerCode(), accountCreateResult.getAttemptResult()));

        final String instaAnswer = accountCreateResult.getAttemptResult();

        Byte finalStatus = AS_IN_PROGRESS;

        if (instaAnswer.contains(EMAIL_SHARING_LIMIT)) {
            finalStatus = AS_EMAIL_SHARING_LIMIT;
        }

        if (instaAnswer.contains(CHECKPOINT_REQ)) {
            finalStatus = AS_CHECK_POINT_REQUIRED;
        }

        if (instaAnswer.contains(OPEN_PROXY)) {
            finalStatus = AS_MARKED_AS_OPEN_PROXY;
        }

        if (accountCreateResult.getAccountCreated()) {
            finalStatus = AS_READY;
        }

        if (finalStatus == AS_IN_PROGRESS) {
            finalStatus = AS_REJECTED;
        }

        getAccount()
                .setAccountStatus(getRefsService()
                        .findAccountStatus(finalStatus));

        StmtProcessor.ifTrue(!getAccount().getAccountStatus().getAccountStatusId().equals(AS_READY),
                () -> this.registerProxyFails(getAccount().getProxy().getProxyId()));

    }
}
