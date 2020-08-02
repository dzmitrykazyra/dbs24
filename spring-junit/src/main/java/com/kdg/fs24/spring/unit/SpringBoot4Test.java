/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.spring.unit;

import com.kdg.fs24.application.core.nullsafe.NullSafe;
import com.kdg.fs24.application.core.service.funcs.GenericFuncs;
import com.kdg.fs24.spring.boot.api.AbstractSpringBootApplication;
import com.kdg.fs24.spring.core.api.ApplicationConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import java.util.Map;
import com.kdg.fs24.application.core.service.funcs.ServiceFuncs;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;

/**
 *
 * @author Козыро Дмитрий
 */
//@SpringBootApplication
//@ComponentScan
public abstract class SpringBoot4Test extends AbstractSpringBootApplication {

//    public final void initializeTest() {
//
//        final Class<? extends SpringBoot4Test> sbClass = this.getClass();
//        final Class<T> appConfigClass = (Class<T>) GenericFuncs.<T>getTypeParameterClass(sbClass);
//
//        Optional<SpringBoot4Test> o
//                = ServiceFuncs.<Class<? extends SpringBoot4Test>, SpringBoot4Test>getMapValue(BOOT_APPS, mapEntry -> mapEntry.getKey().equals(sbClass));
//
//        AbstractSpringBootApplication.runSpringBootApplication(EMPTY_ARGS, sbClass);
//        // инициализация контейнера
//        AbstractSpringBootApplication.initializeContext(appConfigClass);
//    }
}

//interface SpringBootEntry1 extends Map.Entry<Class<? extends SpringBoot4Test>, SpringBoot4Test> {
//}

