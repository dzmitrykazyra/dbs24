/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.action;

import lombok.extern.log4j.Log4j2;
import static org.dbs24.insta.reg.consts.InstaConsts.ActionCodes.ACT_GET_FIRST;

@Log4j2
@ActionCodeId(ACT_GET_FIRST)
public class ActGetFirst extends AbstractAction {

    @Override
    public void perform() {

        setActionNote(String.format("getFirst: %s",
                getInstaService().getFirst(getProxyClient().getHttpProxyClient(), getAccount().getInstaCsrf().getCookie())));

    }
}
