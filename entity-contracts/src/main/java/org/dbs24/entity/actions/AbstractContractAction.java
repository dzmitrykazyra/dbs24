/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.actions;

import org.dbs24.entity.AbstractEntityContract;
import org.dbs24.entity.core.AbstractAction;

public abstract class AbstractContractAction<T extends AbstractEntityContract> extends AbstractAction<T> {

    @Override
    protected void doCalculation() {
        super.doCalculation();
    }
    //==========================================================================
    public AbstractEntityContract getContractEntity() {
        return (AbstractEntityContract) this.getEntity();
    }
    //==========================================================================
    
//    public static Boolean isAllowed( EntityContract entity) {
//
//        return entity.getIsAuthorized();
//    }    

//    //==========================================================================
//    @Override
//    protected void beforeUpdate() throws InternalAppException {
//        super.beforeUpdate();
//    }
//
//    //==========================================================================
//    @Override
//    protected void doUpdate() throws Throwable  {
//        super.doUpdate();
//        this.getEntity().saveEntityInstance();
//    }
}
