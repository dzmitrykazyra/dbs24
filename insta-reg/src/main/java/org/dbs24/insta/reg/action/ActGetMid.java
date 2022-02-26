/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.action;

import lombok.extern.log4j.Log4j2;
import static org.dbs24.insta.reg.consts.InstaConsts.ActionCodes.ACT_GET_MID;

@Log4j2
@ActionCodeId(ACT_GET_MID)
public class ActGetMid extends AbstractAction {

    @Override
    public void perform() {

        getAccount()
                .setInstaMid(getInstaService().getMid(getProxyClient().getHttpProxyClient()));

        setActionNote(String.format("Mid: %s", getAccount().getInstaMid()));

    }
}
