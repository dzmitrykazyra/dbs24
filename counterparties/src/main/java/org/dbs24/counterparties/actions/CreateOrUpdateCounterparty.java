/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.counterparties.actions;

import org.dbs24.entity.core.AbstractAction;
import org.dbs24.entity.counterparties.api.Counterparty;
import org.dbs24.entity.core.api.ActionCodeId;
import org.dbs24.entity.counterparties.api.CounterpartyConst;
import lombok.Data;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@ActionCodeId(action_code = CounterpartyConst.ACT_CREATE_OR_MODIFY_COUNTERPARTY,
        action_name = "Регистрация или изменение атрибутов контрагента")
public class CreateOrUpdateCounterparty extends AbstractAction<Counterparty> {

    @Override
    protected void doUpdate() {
        //this.getPersistenceObjects().add(this.getEntity());
    }

}
