/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.actions;

import org.dbs24.entity.core.AbstractAction;
import org.dbs24.application.core.exception.api.InternalAppException;
import org.dbs24.application.core.sysconst.SysConst;
import org.dbs24.entity.core.AbstractActionEntity;

/**
 *
 * @author Козыро Дмитрий
 */
public class ActAbstractReopenEntity<T extends AbstractActionEntity> extends ActAbstractChangeEntityStatus<T> {

    //==========================================================================
//    public static Boolean isAllowed(final AbstractActionEntity entity) {
//
//        return (AbstractAction.isAllowed(entity, SysConst.ES_CLOSED)
//                || AbstractAction.isAllowed(entity, SysConst.ES_CANCELLED));
//    }
//
    //==========================================================================
    @Override
    protected void changeEntityStatus() {
       // this.getEntity().updateEntityStatus(SysConst.ES_VALID);

    }
//
//    //==========================================================================
//    @Override
//    protected void afterCommit() {
//        // установили статус "действующая" на сделке
//        this.getEntity().setEntityStatus(SysConst.ES_VALID);
//        this.getEntity().setClose_date(SysConst.LOCALDATE_NULL);
//    }
    
}
