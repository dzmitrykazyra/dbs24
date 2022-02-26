/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.action;

import lombok.extern.log4j.Log4j2;
import static org.dbs24.insta.reg.consts.InstaConsts.ActionCodes.ACT_EMAIL_SIGN_UP;

@Log4j2
@ActionCodeId(ACT_EMAIL_SIGN_UP)
public class ActEmailSignUp extends AbstractAction {

    @Override
    public void perform() {

        final EmailSignUpResult emailSignUpResult = getInstaService().emailSignUp(getProxyClient().getHttpProxyClient(), getAccount().getInstaCsrf());
        
        setActionNote(String.format("%d: EmailSignUp: %s", emailSignUpResult.getAnswerCode(), emailSignUpResult));

    }
}
