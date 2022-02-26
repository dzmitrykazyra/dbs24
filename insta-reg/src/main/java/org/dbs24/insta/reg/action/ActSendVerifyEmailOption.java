/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.action;

import lombok.extern.log4j.Log4j2;
import static org.dbs24.insta.reg.consts.InstaConsts.ActionCodes.ACT_SEND_VERIFY_EMAIL_OPTION;

@Log4j2
@ActionCodeId(ACT_SEND_VERIFY_EMAIL_OPTION)
public class ActSendVerifyEmailOption extends AbstractAction {

    @Override
    public void perform() {

        final RegAttemptResult regAttemptResult = getInstaService().sendVerifyEmailOption(getProxyClient().getHttpProxyClient(), getAccount());

        setActionNote(String.format("%d: ActSendVerifyEmailOption: %s", regAttemptResult.getAnswerCode(), regAttemptResult));

    }
}
