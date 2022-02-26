/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.action;

import lombok.extern.log4j.Log4j2;
import static org.dbs24.insta.reg.consts.InstaConsts.ActionCodes.ACT_BATCH_FETCH_WEB;

@Log4j2
@ActionCodeId(ACT_BATCH_FETCH_WEB)
public class ActBatchFetchWeb extends AbstractAction {

    @Override
    public void perform() {

        getAccount()
                .setBatchFetchWeb(getInstaService().batchFetchWeb(getProxyClient().getHttpProxyClient(), getAccount().getInstaCsrf()));

        setActionNote(String.format("%d: BatchFetchWeb: %s",
                getAccount().getBatchFetchWeb().getBatchFetchWebCode(),
                getAccount().getBatchFetchWeb()));

    }
}
