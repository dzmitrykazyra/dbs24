/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.references.list;


import java.io.Serializable;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import com.kdg.fs24.application.core.service.funcs.ServiceFuncs;

public abstract class ObjectList<T extends Object> implements Serializable {

    private final Collection<T> objectList = ServiceFuncs.<T>createCollection();

    // конструктор по умолчанию
    public ObjectList() {
        super();
    }

    //--------------------------------------------------------------------------
    public void clearObjectList() {
        objectList.clear();

    }

    //==========================================================================
    public Collection<T> getObjectList() {
        return objectList;
    }

    //==========================================================================
    @PostConstruct
    public void postConstruct() {

    }

    @PreDestroy
    public void preDestroy() {
    }

    //==========================================================================
//    public static ObjectList getEJB(final String beanName) throws UnknownBeanException {
//
//        ObjectList bean = (NullSafe.create()
//                .execute2result(() -> {
//                    return (ObjectList) new InitialContext().lookup(beanName);
//                })).<ObjectList>getObject();
//
//        NullSafe.create(bean)
//                .whenIsNull(() -> {
//
//                    String errMsg = String.format("%s: lookUp bean exception \n beanName: %s )",
//                            LogService.getCurrentObjProcName(ObjectList.class), beanName);
//
//                    LogService.LogErr(ObjectList.class, LogService.getCurrentObjProcName(ObjectList.class), () -> errMsg);
//
//                    throw new UnknownBeanException(errMsg);
//                });
//
//        return bean;
//
//    }
//
//    //==========================================================================
//    public static ObjectList getEJB(final Class<?> clazz) throws UnknownBeanException {
//
//        return (ObjectList) ObjectList.getEJB(clazz, LogGate.getWarPackageName());
//
//    }
//
//    //==========================================================================
//    public static ObjectList getEJB(final Class<?> clazz, final String packageName) throws UnknownBeanException {
//
//        return (ObjectList) ObjectList.getEJB(String.format("java:global/%s/%s",
//                packageName,
//                clazz.getSimpleName()));
//
//    }
}
