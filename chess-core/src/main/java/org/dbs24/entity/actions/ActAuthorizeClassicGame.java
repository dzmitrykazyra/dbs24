/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.actions;

import org.dbs24.consts.WorldChessConst;
import org.dbs24.entity.core.api.ActionCodeId;
import org.dbs24.entity.core.api.PreViewDialog;
import org.dbs24.entity.core.api.ViewAction;
import org.dbs24.entity.classic.ClassicGame;
import lombok.Data;
import org.dbs24.entity.core.AbstractAction;
import org.dbs24.entity.core.api.EntityConst;
import org.dbs24.entity.marks.EntityMark;
import org.dbs24.entity.marks.MarkValue;
import org.dbs24.references.api.AbstractRefRecord;
import org.dbs24.application.core.nullsafe.NullSafe;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@ActionCodeId(action_code = WorldChessConst.ACT_AUTHORIZE_GAME,
        action_name = "Авторизация партии")
@ViewAction
@PreViewDialog
public class ActAuthorizeClassicGame extends AbstractAction<ClassicGame> {

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
}