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
import org.dbs24.consts.WorldChessConst;
import org.dbs24.entity.core.api.ActionCodeId;
import org.dbs24.entity.classic.ClassicGame;
import org.dbs24.entity.core.api.RefreshEntity;
import org.dbs24.entity.core.AbstractAction;
import lombok.Data;

@Data
@ActionCodeId(action_code = WorldChessConst.ACT_CREATE_OR_MODIFY_GAME,
        action_name = "Создать\\изменить шахматную партию")
@RefreshEntity
public class ActCreateOrModifyGame extends AbstractAction<ClassicGame> {

    @Override
    protected void doUpdate() {
        //this.getPersistenceObjects().add(this.getEntity());
    }
}