/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.action;

import lombok.extern.log4j.Log4j2;

import static org.dbs24.insta.reg.consts.InstaConsts.ActionCodes.ACT_VALIDAATE_EMAIL;

@ActionCodeId(ACT_VALIDAATE_EMAIL)
@Log4j2
public class ActValidateEmail extends AbstractAction {

    @Override
    public void perform() {

        getEmailService().validateGmailAccount(getAccount().getEmail().getEmailAddress(), getAccount().getEmail().getPwd());

    }
}
