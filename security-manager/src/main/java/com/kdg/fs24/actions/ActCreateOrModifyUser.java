/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.actions;

import com.kdg.fs24.entity.core.AbstractAction;
import com.kdg.fs24.entity.security.ApplicationUser;
import com.kdg.fs24.entity.core.api.ActionCodeId;
import com.kdg.fs24.config.SecurityConst;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * @author Козыро Дмитрий
 */
//@Entity
@Data
@ActionCodeId(action_code = SecurityConst.ACT_CREATE_OR_MODIFY_USER,
        action_name = "Регистрация или изменение пользователя")
public class ActCreateOrModifyUser extends AbstractAction<ApplicationUser> {

    @Override
    protected void doUpdate() {
        //this.getPersistenceObjects().add(this.getEntity());
    }

}
