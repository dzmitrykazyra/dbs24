/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.contracts.actions;

import com.kdg.fs24.application.core.nullsafe.NullSafe;
import com.kdg.fs24.entity.contract.subjects.ContractSubject;
import com.kdg.fs24.entity.contracts.AbstractEntityServiceContract;
import com.kdg.fs24.entity.core.AbstractAction;
import com.kdg.fs24.entity.core.api.ActionCodeId;
import com.kdg.fs24.entity.core.api.EntityConst;
import com.kdg.fs24.entity.core.api.EntityContractConst;
import com.kdg.fs24.entity.core.api.ViewAction;
import com.kdg.fs24.entity.core.api.PreViewDialog;
import com.kdg.fs24.entity.marks.EntityMark;
import com.kdg.fs24.entity.marks.MarkValue;
import com.kdg.fs24.references.api.AbstractRefRecord;
import lombok.Data;
import com.kdg.fs24.entity.core.api.RefreshEntity;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@ActionCodeId(action_code = EntityContractConst.ACT_AUTHORIZE_CONTRACT,
        action_name = "Авторизация договора")
@ViewAction
@PreViewDialog
//@SkipRefresh
//@AllowedMethod(action = ActAuthorizeAbstractContract.class, entity = EntityContract.class)
public class ActAuthorizeAbstractContract extends AbstractContractAction<AbstractEntityServiceContract> {

    //==========================================================================
    public static Boolean isAllowed(final AbstractEntityServiceContract
            entity) {

//        return (NullSafe.create()
//                .execute2result(() -> {
//                    return !entity.getIsAuthorized() && (entity.getEntityStatus().getEntity_status_id().equals(SysConst.ES_VALID));
//                }))
//                .catchException2result((e) -> {
//                    return SysConst.IS_NOT_ALLOWED_ACTION;
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
                record -> record.getMarkId().equals(EntityConst.MR_AUTHORIZE_ENTITY)
                && record.getMarkValueId().equals(EntityConst.MR_AUTHORIZE_ENTITY_AUTH)));
        entityMark.setDirection(EntityConst.IS_AUTHORIZED);

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
