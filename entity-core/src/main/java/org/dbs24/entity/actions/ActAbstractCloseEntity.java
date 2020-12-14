/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.actions;

import org.dbs24.entity.core.AbstractAction;
import org.dbs24.entity.core.AbstractActionEntity;
import org.dbs24.application.core.exception.api.InternalAppException;
import static org.dbs24.consts.SysConst.*;
import java.time.LocalDateTime;

/**
 *
 * @author Козыро Дмитрий
 */
public class ActAbstractCloseEntity<T extends AbstractActionEntity> extends ActAbstractChangeEntityStatus<T> {

//    public static Boolean isAllowed( AbstractActionEntity entity) {
//
//        return (entity.getIsAuthorized() && AbstractAction.isAllowed(entity, ES_ACTUAL));
//    }
//
    //==========================================================================
    @Override
    protected void changeEntityStatus() {
        //this.getEntity().updateEntityStatus(ES_CLOSED);

    }
//
//    //==========================================================================
//    @Override
//    protected void afterCommit() {
//        // установили статус "завершено" на сделке
//        this.setUnathorized(ES_CLOSED);
//        this.getEntity().setEntityStatus(ES_CLOSED);
//        this.getEntity().setClose_date(LocalDateTime.now().toLocalDate());
//    }
}
