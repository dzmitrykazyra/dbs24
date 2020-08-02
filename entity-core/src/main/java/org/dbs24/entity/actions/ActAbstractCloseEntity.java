/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.actions;

import org.dbs24.entity.core.AbstractAction;
import org.dbs24.entity.core.AbstractActionEntity;
import org.dbs24.application.core.exception.api.InternalAppException;
import org.dbs24.application.core.sysconst.SysConst;
import java.time.LocalDateTime;

/**
 *
 * @author Козыро Дмитрий
 */
public class ActAbstractCloseEntity<T extends AbstractActionEntity> extends ActAbstractChangeEntityStatus<T> {

//    public static Boolean isAllowed(final AbstractActionEntity entity) {
//
//        return (entity.getIsAuthorized() && AbstractAction.isAllowed(entity, SysConst.ES_VALID));
//    }
//
    //==========================================================================
    @Override
    protected void changeEntityStatus() {
        //this.getEntity().updateEntityStatus(SysConst.ES_CLOSED);

    }
//
//    //==========================================================================
//    @Override
//    protected void afterCommit() {
//        // установили статус "завершено" на сделке
//        this.setUnathorized(SysConst.ES_CLOSED);
//        this.getEntity().setEntityStatus(SysConst.ES_CLOSED);
//        this.getEntity().setClose_date(LocalDateTime.now().toLocalDate());
//    }
}
