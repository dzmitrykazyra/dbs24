/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.action;

import lombok.extern.log4j.Log4j2;
import static org.dbs24.insta.reg.consts.InstaConsts.ActionCodes.ACT_CHECK_AGES;

@Log4j2
@ActionCodeId(ACT_CHECK_AGES)
public class ActCheckAgeEligibility extends AbstractAction {

    @Override
    public void perform() {

        final RegAttemptResult regAttemptResult = getInstaService().checkAgeEligibility(getProxyClient().getHttpProxyClient(), getAccount());

        setActionNote(String.format("%d: ActCheckAgeEligibility: %s", regAttemptResult.getAnswerCode(), regAttemptResult));

    }
}
