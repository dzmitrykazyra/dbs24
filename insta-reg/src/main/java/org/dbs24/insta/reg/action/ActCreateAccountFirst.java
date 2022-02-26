/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.action;

import lombok.extern.log4j.Log4j2;
import static org.dbs24.insta.reg.consts.InstaConsts.ActionCodes.ACT_CREATE_ACCOUNT_FIRST;
import static org.dbs24.insta.reg.consts.InstaConsts.AccountProcessing.ANOTHER_ACCOUNT;
import static org.dbs24.insta.reg.consts.InstaConsts.AccountProcessing.EMAIL_SHARING_LIMIT;
import static org.dbs24.insta.reg.consts.InstaConsts.AccountStatuses.AS_ANOTHER_ACCOUNT_USING;
import static org.dbs24.insta.reg.consts.InstaConsts.AccountStatuses.AS_EMAIL_SHARING_LIMIT;

@Log4j2
@ActionCodeId(ACT_CREATE_ACCOUNT_FIRST)
public class ActCreateAccountFirst extends AbstractAction {

    @Override
    public void perform() {

        final RegAttemptResult regAttemptResult = getInstaService().createAttempt1(getProxyClient().getHttpProxyClient(), getAccount());

        setActionNote(String.format("%d: ActCreateAccountFirst: %s", regAttemptResult.getAnswerCode(), regAttemptResult));

        final String instaAnswer = regAttemptResult.getAttemptResult();

        if (instaAnswer.contains(EMAIL_SHARING_LIMIT)) {

            this.setActionCode(Byte.valueOf("-1"));
            getAccount()
                    .setAccountStatus(getRefsService()
                            .findAccountStatus(AS_EMAIL_SHARING_LIMIT));
        }
//
//        if (instaAnswer.contains(ANOTHER_ACCOUNT)) {
//
//            this.setActionCode(Byte.valueOf("-1"));
//            getAccount()
//                    .setAccountStatus(getRefsService()
//                            .findAccountStatus(AS_ANOTHER_ACCOUNT_USING));
//
//        }
    }
}
