/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.actions;

import static org.dbs24.entity.core.api.EntityContractConst.*;
import static org.dbs24.consts.EntityReferenceConst.*;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.entity.AbstractEntityContract;
import org.dbs24.entity.core.api.ActionCodeId;
import org.dbs24.entity.core.api.ViewAction;
import org.dbs24.entity.core.api.PreViewDialog;
import org.dbs24.entity.marks.EntityMark;
import org.dbs24.entity.marks.MarkValue;
import org.dbs24.references.api.AbstractRefRecord;
import lombok.Data;


@Data
@ActionCodeId(action_code = ACT_AUTHORIZE_CONTRACT,
        action_name = "Авторизация договора")
@ViewAction
@PreViewDialog
//@SkipRefresh
//@AllowedMethod(action = ActAuthorizeAbstractContract.class, entity = EntityContract.class)
public class ActAuthorizeAbstractContract extends AbstractContractAction<AbstractEntityContract> {

    //==========================================================================
    public static Boolean isAllowed( AbstractEntityContract
            entity) {

//        return (NullSafe.create()
//                .execute2result(() -> {
//                    return !entity.getIsAuthorized() && (entity.getEntityStatus().getEntity_status_id().equals(ES_ACTUAL));
//                }))
//                .catchException2result((e) -> {
//                    return IS_NOT_ALLOWED_ACTION;
//                })
//                .<Boolean>getObject();
        return true;

    }

    //==========================================================================
    @Override
    public void doUpdate() {

        // отметка об авторизации
        final EntityMark entityMark = NullSafe.createObject(EntityMark.class);

        entityMark.setAction(this.getPersistAction());
        entityMark.setEntity(this.getEntity());
        entityMark.setMarkValue(AbstractRefRecord.<MarkValue>getRefeenceRecord(
                MarkValue.class,
                record -> record.getMarkId().equals(MR_AUTHORIZE_ENTITY)
                && record.getMarkValueId().equals(MR_AUTHORIZE_ENTITY_AUTH)));
        entityMark.setDirection(IS_AUTHORIZED);

        this.addPersistenceEntity(entityMark);
        //this.getContractEntity().getEntityMarks().add(entityMark);

    }

    //==========================================================================    
    @Override
    protected final void afterCommit() {
        // добавили отметку в коллекцию отметок
//        this.getContractEntity()
//                .getEntityMarks()
//                .addEntityMark(EntityConst.MR_AUTHORIZE_ENTITY,
//                        EntityConst.MR_AUTHORIZE_ENTITY_NOT_AUTH);
//        this.getContractEntity()
//                .setIsAuthorized(EntityConst.IS_AUTHORIZED);
    }
    //==========================================================================
}
