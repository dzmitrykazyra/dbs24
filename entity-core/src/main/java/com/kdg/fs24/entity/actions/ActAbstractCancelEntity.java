/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.actions;

import com.kdg.fs24.entity.core.AbstractAction;
import com.kdg.fs24.application.core.exception.api.InternalAppException;
import com.kdg.fs24.application.core.sysconst.SysConst;
import java.time.LocalDateTime;
import com.kdg.fs24.entity.core.AbstractActionEntity;

/**
 *
 * @author Козыро Дмитрий
 */
public class ActAbstractCancelEntity<T extends AbstractActionEntity> extends ActAbstractChangeEntityStatus<T> {

//    public static Boolean isAllowed(final AbstractActionEntity entity) {
//
//        return AbstractAction.isAllowed(entity, SysConst.ES_VALID);
//
//    }
//
    @Override
    protected void changeEntityStatus() {
        //this.getEntity().updateEntityStatus(SysConst.ES_CANCELLED);

    }
//
//    //==========================================================================                        
//    @Override
//    protected void afterCommit() {
//        // установили статус "аннулировано" на сделке
//        this.setUnathorized(SysConst.ES_CANCELLED);
//        this.getEntity().setEntityStatus(SysConst.ES_CANCELLED);
//        this.getEntity().setClose_date(LocalDateTime.now().toLocalDate());
//    }
}
