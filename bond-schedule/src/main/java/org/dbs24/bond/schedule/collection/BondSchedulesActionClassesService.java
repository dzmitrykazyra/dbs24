/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.bond.schedule.collection;

/**
 *
 * @author kazyra_d
 */
import org.dbs24.entity.classes.AbstractActionClassesCollection;
import org.dbs24.exception.api.InternalAppException;
import org.dbs24.services.api.Service;

public class BondSchedulesActionClassesService extends AbstractActionClassesCollection implements Service {

    @Override
    public void initializeService() {
        // регистрация классов действий над сущностями-графиками
        //this.RegisterActionClass(ActSaveBondSchedule.class);        
        this.registerPackageActionClasses("org.dbs24.bond.schedule.actions");
        Service.super.initializeService();
    }

}
