/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.actions;

import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.entity.core.api.ActionCodeId;
import org.dbs24.consts.WorldChessConst;
import org.dbs24.entity.core.AbstractAction;
import org.dbs24.entity.core.api.PreViewDialog;
import org.dbs24.entity.core.api.ViewAction;
import org.dbs24.entity.AbstractPlayer;
import static org.dbs24.consts.EntityReferenceConst.*;
import org.dbs24.entity.marks.EntityMark;
import org.dbs24.entity.marks.MarkValue;
import org.dbs24.references.api.AbstractRefRecord;
import lombok.Data;

@Data
@ActionCodeId(action_code = WorldChessConst.ACT_AUTHORIZE_PLAYER,
        action_name = "Авторизация игрока")
@ViewAction
@PreViewDialog
public class ActAuthorizePlayer extends AbstractAction<AbstractPlayer> {

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
    }
}
