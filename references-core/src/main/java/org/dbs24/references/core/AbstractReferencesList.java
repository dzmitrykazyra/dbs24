/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.core;


import org.dbs24.application.core.log.LogService;
import org.dbs24.references.api.AppLanguage;
import org.dbs24.references.api.ReferenceSyncOrder;
import org.dbs24.references.list.ObjectList;
import org.dbs24.application.core.service.funcs.AnnotationFuncs;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.application.core.service.funcs.ReflectionFuncs;
import java.lang.reflect.Method;
import static org.dbs24.consts.SysConst.*;
import org.dbs24.application.core.nullsafe.NullSafe;

/**
 *
 * @author kazyra_d
 */
@Deprecated
public abstract class AbstractReferencesList<T extends AbstractReference> extends ObjectList {

    // язык справочника
    private static final AppLanguage appLanguage = AppLanguage.RUSSIAN;

    public AbstractReferencesList() {
        super();
//        LogService.LogInfo(this.getClass(), () -> LogService.getCurrentObjProcName(this.getClass()));
    }

    //==========================================================================
    public static AppLanguage getAppLanguage() {
        return appLanguage;
    }

    //==========================================================================
    // создание и добавление справочника в коллекцию
    public <T extends AbstractReference> T findOrCreateReference( Class<T> refClass) {

        return (T) NullSafe.create(ServiceFuncs.<T>getCollectionElement_silent(getObjectList(),
                p -> p.getClass().isAssignableFrom(refClass)))
                .whenIsNull(() -> {
                    // добавление справочника из аннотации
                    final T reference = (T) (NullSafe.create()
                            .execute2result(() -> {

                                LogService.LogInfo(refClass, () -> String.format("Reference initialization [%s]", refClass.getCanonicalName()));

                                // создание экземпляра справочника
                                return NullSafe.createObject(refClass);
                            })).<T>getObject(); // добавили в коллекцию справочников

                    if (NullSafe.notNull(reference)) {
                        getObjectList().add(reference);
                    }
                    return reference;
                })
                .<T>getObject();
    }

    //==========================================================================
    private Method findRegisterMethod( Class clazz, String methodName) {
        final String key = String.format("%s_%s.%s",
                LogService.getCurrentObjProcName(this),
                clazz.getCanonicalName(),
                methodName);

        return (NullSafe.create()
                .execute2result(() -> {
                    return clazz.getMethod(methodName);
                })
                .catchMsgException((errMsg) -> {
                    LogService.LogErr(clazz, key,
                            () -> String.format("methodName not found ('%s', class='%s') (%s)",
                                    methodName,
                                    clazz.getCanonicalName(),
                                    errMsg));
                }))
                .<Method>getObject();
    }

    //==========================================================================
    @Deprecated
    public void registerReference( Class refClass) {
        final String key = String.format("%s_%s",
                LogService.getCurrentObjProcName(this),
                refClass.getCanonicalName());
        // находим через reflection и выполняем статический метод registerReference
        //LogService.LogInfo(this.getClass(), key, refClass.getCanonicalName());

//        final Method method = this.findRegisterMethod(refClass, "registerReference");
//
//        if (NullSafe.notNull(method)) {
//            NullSafe.create(key)
//                    .execute(() -> {
//                        synchronized (AbstractReference.class) {
//                            method.invoke(null);
//                        }
//                    });
//        }
        NullSafe.create(this.findRegisterMethod(refClass, "registerReference"))
                .safeExecute((ns_method) -> {
                    synchronized (AbstractReference.class) {
                        LogService.LogInfo(this.getClass(), key, () -> refClass.getCanonicalName());
                        ((Method) ns_method).invoke(null);
                    }
                }).throwException();
    }
    //==========================================================================
    @Deprecated
    public void registerPackageReferences( String modulePackage) {

        NullSafe.runNewThread(() -> {

            ReflectionFuncs.createPkgClassesCollection(modulePackage, AbstractReference.class)
                    .stream()
                    .sorted((refClass1, refClass2) -> { // достаем признак порядкового номера из аннотации

                        final Integer order_num1 = (NullSafe.create(OBJECT_NULL, NullSafe.DONT_THROW_EXCEPTION)
                                .execute2result(() -> {
                                    return ((ReferenceSyncOrder) AnnotationFuncs.getAnnotation(refClass1, ReferenceSyncOrder.class
                                    )).order_num();
                                }, Integer.valueOf("10000"))).<Integer>getObject();

                        final Integer order_num2 = (NullSafe.create(OBJECT_NULL, NullSafe.DONT_THROW_EXCEPTION)
                                .execute2result(() -> {
                                    return ((ReferenceSyncOrder) AnnotationFuncs.getAnnotation(refClass2, ReferenceSyncOrder.class
                                    )).order_num();
                                }, Integer.valueOf("10000"))).<Integer>getObject();

                        return order_num1.compareTo(order_num2);
                    })
                    .forEach((clazz) -> {
                        // регистрация справочников в отсортированном порядке

                        this.registerReference(clazz);
                    });
        });
    }
}
