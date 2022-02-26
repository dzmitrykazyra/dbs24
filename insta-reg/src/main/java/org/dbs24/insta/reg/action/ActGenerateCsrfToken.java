/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.action;

import lombok.extern.log4j.Log4j2;
import static org.dbs24.insta.reg.consts.InstaConsts.ActionCodes.ACT_GENERATE_CSRF;


@Log4j2
@ActionCodeId(ACT_GENERATE_CSRF)
public class ActGenerateCsrfToken extends AbstractAction {


    @Override
    public void perform() {

        getAccount()
                .setInstaCsrf(getInstaService().generateCsrf(getProxyClient().getHttpProxyClient(), getAccount().getInstaMid()));

        setActionNote(String.format("Csrf: %s", getAccount().getInstaCsrf().toString()));

    }
}
