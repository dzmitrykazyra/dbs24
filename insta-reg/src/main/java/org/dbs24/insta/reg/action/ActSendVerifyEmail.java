/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.action;

import lombok.extern.log4j.Log4j2;
import static org.dbs24.insta.reg.consts.InstaConsts.ActionCodes.ACT_SEND_VERIFY_EMAIL;
import static org.dbs24.insta.reg.consts.InstaConsts.AccountStatuses.AS_WAIT_4_CODE;

@Log4j2
@ActionCodeId(ACT_SEND_VERIFY_EMAIL)
public class ActSendVerifyEmail extends AbstractAction {

    @Override
    public void perform() {

        final RegAttemptResult regAttemptResult = getInstaService().sendVerifyEmail(getProxyClient().getHttpProxyClient(), getAccount());

        setActionNote(String.format("%d: ActSendVerifyEmail: %s", regAttemptResult.getAnswerCode(), regAttemptResult));
        
        getAccount().setAccountStatus(getRefsService().findAccountStatus(AS_WAIT_4_CODE));

    }
}
