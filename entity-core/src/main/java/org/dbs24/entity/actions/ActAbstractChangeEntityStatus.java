/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.actions;

/**
 *
 * @author Козыро Дмитрий
 */
import org.dbs24.entity.core.AbstractAction;
import org.dbs24.entity.core.AbstractActionEntity;
import org.dbs24.consts.EntityConst;
import org.dbs24.application.core.nullsafe.NullSafe;

public abstract class ActAbstractChangeEntityStatus<T extends AbstractActionEntity>
        extends AbstractAction<T> {

    public static Boolean isAllowed( AbstractActionEntity entity) {

        //return (!entity.getJustCreated());
        return (Boolean.TRUE);
    }

//    @Override
//    public T getEntity() {
//        return (T) this.entity;
//    }
    protected abstract void changeEntityStatus();

//    @Override
//    protected void doUpdate() {
//        NullSafe.create()
//                .execute(() -> {
//                    // статус аннулирования сделки
//                    this.changeEntityStatus();
//                    if (this.getEntity().getIsAuthorized()) {
//                        this.getEntity()
//                                .getEntityMarks()
//                                .saveEntityMark(
//                                        this.getEntity().getEntity_id(),
//                                        this.actionId,
//                                        EntityConst.MR_AUTHORIZE_ENTITY,
//                                        EntityConst.MR_AUTHORIZE_ENTITY_NOT_AUTH);
//                    }
//                }).throwException();
//    }
    //==========================================================================    
//    protected final void setUnathorized( Integer newStatus) {
//        // сбросили отметку авторизации
//        if (this.getEntity().getIsAuthorized()) {
//            this.getEntity()
//                    .getEntityMarks()
//                    .removeEntityMark(EntityConst.MR_AUTHORIZE_ENTITY);
//
//            this.getEntity()
//                    .AbstractActionEntityEntityConst.NOT_AUTHORIZED);
////            this.getEntity().setEntityStatus(newStatus);
////            this.getEntity().setClose_date(LocalDateTime.now().toLocalDate());
//        }
//    }
}
