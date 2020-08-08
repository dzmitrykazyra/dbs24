/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.actions;

import org.dbs24.consts.WorldChessConst;
import org.dbs24.entity.core.api.ActionCodeId;
import org.dbs24.entity.AbstractChessPlayer;
import org.dbs24.entity.core.api.RefreshEntity;
import org.dbs24.entity.core.AbstractAction;
import lombok.Data;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@ActionCodeId(action_code = WorldChessConst.ACT_CREATE_OR_MODIFY_PLAYER,
        action_name = "Создать\\изменить учетную запись игрока в шахматы")
@RefreshEntity
public class ActCreateOrModifyPlayer extends AbstractAction<AbstractChessPlayer> {

    @Override
    protected void doUpdate() {
        //this.getPersistenceObjects().add(this.getEntity());
    }
}
