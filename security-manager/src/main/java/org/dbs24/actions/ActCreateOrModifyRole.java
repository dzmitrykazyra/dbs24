/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.actions;

import org.dbs24.consts.SecurityConst;
import org.dbs24.entity.core.AbstractAction;
import org.dbs24.entity.core.api.ActionCodeId;
import org.dbs24.entity.security.ApplicationRole;
import lombok.Data;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@ActionCodeId(action_code = SecurityConst.ACT_CREATE_OR_MODIFY_ROLE,
        action_name = "Регистрация или изменение роли")
public class ActCreateOrModifyRole extends AbstractAction<ApplicationRole> {

    @Override
    protected void doUpdate() {
        //this.getPersistenceObjects().add(this.getEntity());
    }

}
