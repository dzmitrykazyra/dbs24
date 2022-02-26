/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.action;

import lombok.extern.log4j.Log4j2;
import static org.dbs24.insta.reg.consts.InstaConsts.ActionCodes.ACT_CREATE_ACCOUNT_THIRD;

@Log4j2
@ActionCodeId(ACT_CREATE_ACCOUNT_THIRD)
public class ActCreateAttemptThird extends AbstractAction {

    @Override
    public void perform() {

        final RegAttemptResult regAttemptResult = getInstaService().createAttempt3(getProxyClient().getHttpProxyClient(), getAccount());

        setActionNote(String.format("%d: ActCreateAccountThird: %s", regAttemptResult.getAnswerCode(), regAttemptResult));

//        StmtProcessor.ifTrue(StmtProcessor.notNull(regAttemptResult.getNameSuggestions()),
//                () -> getAccount().setLogin(regAttemptResult.getNameSuggestions()));

    }
}
