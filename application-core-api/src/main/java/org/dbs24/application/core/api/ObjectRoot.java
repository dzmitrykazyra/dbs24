/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.api;

/**
 *
 * @author Козыро Дмитрий
 */
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
//import org.dbs24.registry.api.RegistryCoreService;
//import org.dbs24.jpa.entity.service.PersistanceEntityManagerService;

public abstract class ObjectRoot extends Object implements Serializable, Cloneable {

//    private static RegistryCoreService rcs;
    //private volatile static PersistanceEntityManagerService persistanceEntityManagerService;
    @JsonIgnore
    private final Object monitorLock = new Object();

    public ObjectRoot() {
        super();
    }

    //==========================================================================
//    @JsonIgnore
//    public RegistryCoreService getRcs() {
//
//        return ObjectRoot.getStaticRcs();
//    }
//
//    //==========================================================================
//    @JsonIgnore
//    public static RegistryCoreService getStaticRcs() {
//
//        if (NullSafe.isNull(ObjectRoot.rcs)) {
//            NullSafe.create(LogService.getCurrentObjProcName(RegistryCoreService.class))
//                    .execute(() -> {
//                        //ObjectRoot.rcs = (RegistryCore) ObjectList.getEJB(RegistryCore.class);
//                        ObjectRoot.rcs = ServiceLocator.find(RegistryCoreService.class);
//                    });
//        }
//
//        return ObjectRoot.rcs;
//    }
    //==========================================================================
//    @JsonIgnore
//    public PersistanceEntityManagerService getPersistanceService() {
//
//        return ObjectRoot.getStaticPersistanceService();
//    }
    //==========================================================================
    public void initialize() {

        synchronized (this.getMonitorLock()) {
            this.internalInitialize();
        }

    }

    protected void internalInitialize() {

    }

    //==========================================================================
    protected static final Object monitorDbServiceLock = new Object();

    private static void initializePersistanceService() {

        synchronized (ObjectRoot.monitorDbServiceLock) {

//            NullSafe.create(ObjectRoot.persistanceEntityManagerService)
//                    .whenIsNull(() -> ObjectRoot.persistanceEntityManagerService
//                    = ServiceLocator.find(PersistanceEntityManagerService.class));
        }
    }

    //==========================================================================
    //==========================================================================
//    @JsonIgnore
//    public static PersistanceEntityManagerService getStaticPersistanceService() {
//
//        return NullSafe.create(ObjectRoot.persistanceEntityManagerService)
//                .whenIsNull(() -> {
//                    ObjectRoot.initializePersistanceService();
//                    return ObjectRoot.persistanceEntityManagerService;
//
//                })
//                .<PersistanceEntityManagerService>getObject();
//
//    }
    //==========================================================================
    public Object getMonitorLock() {
        return monitorLock;
    }
}
