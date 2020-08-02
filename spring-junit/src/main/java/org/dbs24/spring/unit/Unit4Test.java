/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.spring.unit;

import org.dbs24.application.core.log.LogService;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.spring.boot.api.AbstractSpringBootApplication;
import org.dbs24.spring.core.api.ApplicationConfiguration;
import org.dbs24.application.core.service.funcs.GenericFuncs;
import org.dbs24.application.core.service.funcs.ServiceFuncs;


/**
 *
 * @author Козыро Дмитрий
 */
public abstract class Unit4Test<BOOT extends AbstractSpringBootApplication, CONF extends ApplicationConfiguration> {

//    static class SpringBootMap extends ConcurrentHashMap<Class<? extends SpringBoot4Test>, SpringBoot4Test> {
//    }
    final static private String[] EMPTY_ARGS = new String[0];
    final static private SpringBootClasses BOOT_CLASSES = NullSafe.createObject(SpringBootClasses.class);

    public Unit4Test() {

        LogService.LogInfo(this.getClass(), () -> String.format("Unit4Test '%s' is running (%d) ",
                this.getClass().getCanonicalName(),
                BOOT_CLASSES.size()));

        final Class<BOOT> sbClass = (Class<BOOT>) GenericFuncs.<BOOT>getTypeParameterClass(this.getClass(), 0);
        final Class<CONF> appConfigClass = (Class<CONF>) GenericFuncs.<CONF>getTypeParameterClass(this.getClass(), 1);

        if (!ServiceFuncs.<Class<? extends AbstractSpringBootApplication>>getCollectionElement(BOOT_CLASSES, bootClass -> bootClass.equals(sbClass))
                .isPresent()) {

            NullSafe.create()
                    .execute(() -> {

                        AbstractSpringBootApplication.runSpringBootApplication(EMPTY_ARGS, sbClass);
                        // инициализация контейнера
                        AbstractSpringBootApplication.initializeContext(appConfigClass);

                        //BOOT_APPS.put(sbClass, this);
                    }).finallyBlock(() -> {
                BOOT_CLASSES.add(sbClass);
            })
                    .throwException();
        }
    }
}

//interface SpringBootEntry extends Map.Entry<Class<? extends SpringBoot4Test>, SpringBoot4Test> {
//}
//class SpringBootMap extends ConcurrentHashMap<Class<? extends SpringBoot4Test>, SpringBoot4Test> {
//}
