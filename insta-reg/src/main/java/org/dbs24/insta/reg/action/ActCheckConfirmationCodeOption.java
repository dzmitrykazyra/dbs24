/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.action;

import lombok.extern.log4j.Log4j2;
import static org.dbs24.insta.reg.consts.InstaConsts.ActionCodes.ACT_CHECK_CONFIRM_CODE_OPT;

@Log4j2
@ActionCodeId(ACT_CHECK_CONFIRM_CODE_OPT)
public class ActCheckConfirmationCodeOption extends AbstractAction {

    @Override
    public void perform() {

        final RegAttemptResult regAttemptResult = getInstaService().checkConfirmationCodeOption(getProxyClient().getHttpProxyClient(), getAccount());

        setActionNote(String.format("%d: ActCheckConfirmationCodeOption: %s", regAttemptResult.getAnswerCode(), regAttemptResult));

    }
}
