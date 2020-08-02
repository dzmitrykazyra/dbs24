/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.persistence.core;

import org.dbs24.application.core.service.funcs.CustomCollectionImpl;
import org.dbs24.application.core.log.LogService;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
//import org.dbs24.services.api.Service;
import org.dbs24.application.core.nullsafe.NullSafe;
import javax.persistence.*;
//import org.dbs24.application.core.log.LogService;
import org.dbs24.application.core.sysconst.SysConst;
import org.dbs24.persistence.api.PersistenceAction;
import org.dbs24.persistence.api.PersistenceEntity;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.dbs24.spring.core.bean.AbstractApplicationBean;
import java.util.Collection;
import org.dbs24.persistence.api.QueryExecutor;
import org.dbs24.persistence.api.PersistenceQuery;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import javax.transaction.UserTransaction;
import javax.naming.InitialContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
public class PersistanceEntityManager extends AbstractApplicationBean {

    private final AtomicBoolean safeMode = NullSafe.createObject(AtomicBoolean.class);
    //@PersistenceUnit
    private volatile EntityManager entityManager;
    //@PersistenceContext
    private volatile EntityManagerFactory factory;
    @Value("${persistenceUnitName}")
    private volatile String persistenceUnitName;
    @Value("${spring.datasource.url}")
    private volatile String persistenceJdbcUrl;
    @Value("${spring.datasource.driver-class-name}")
    private volatile String persistenceJdbcDriver;
    @Value("${spring.datasource.username}")
    private volatile String persistenceJdbcUser;
    @Value("${spring.datasource.password}")
    private volatile String persistenceJdbcPassword;
    @Value("${spring.jpa.database-platform}")
    private volatile String hibernateDialect;
    //private static SessionFactory sessionFactory;
    private volatile Map<String, Object> properties;
    //private static final QueryExecutor QUERY_EXECUTOR_NULL = null;
    @Value("${debug}")
    private String debugMode;
    @Value("${persistence.debug:false}")
    private Boolean persistenceDebug;
    @Value("${russian_ref_lang}")
    private String russianRefLan; // = SysConst.STRING_FALSE;
    @Value("${defaultJdbcBatchSize}")
    private Integer defaultJdbcBatchSize; // = SysConst.STRING_FALSE;

//    @Resource
//    UserTransaction utx;
    //public PersistanceEntityManager(final String persistenceUnitName) {
    @Override
    public void initialize() {

        NullSafe.create(persistenceUnitName)
                .execute(() -> {
//sessionFactory = new Configuration().configure().buildSessionFactory();

                    SysConst.RUSSIAN_REF_LANG.set(!this.russianRefLan.toLowerCase().equals(SysConst.STRING_FALSE));

                    if (persistenceDebug) {
                        LogService.LogInfo(this.getClass(), () -> String.format("Try 2 create persistence '%s'",
                                persistenceUnitName));
                    }

                    this.factory = Persistence.createEntityManagerFactory(persistenceUnitName, this.getProperties());

                    if (persistenceDebug) {
                        LogService.LogInfo(this.getClass(), () -> String.format("Persistence '%s' is created",
                                persistenceUnitName));
                    }

                    createOrUpdateEntityManager();

                })
                .throwException();

//LogService.LogInfo(this.getClass(), () -> String.format("Service '%s' is initialized", this.getClass()));
//persistenceUnitName, properties)
    }

    //==========================================================================
    private Map getProperties() {
        final Map result = ServiceFuncs.<String, Object>getOrCreateMap(ServiceFuncs.MAP_NULL);

        // Read the properties from a file instead of hard-coding it here.
        // Or pass the password in from the command-line.
        result.put("javax.persistence.jdbc.url", persistenceJdbcUrl);
        result.put("javax.persistence.jdbc.driver", persistenceJdbcDriver);
        result.put("javax.persistence.jdbc.user", persistenceJdbcUser);
        result.put("javax.persistence.jdbc.password", persistenceJdbcPassword);
        result.put("hibernate.hbm2ddl.auto", "none");
        result.put("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
        result.put("hibernate.cache.use_second_level_cache", "true");
        result.put("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        result.put("hibernate.dialect", hibernateDialect);
        result.put("exclude-unlisted-classes", "true");

        return result;
    }
    //========================================================================

    private void createOrUpdateEntityManager() {

        //synchronized (this) {
        //if (!this.safeMode.get()) {
        if (this.safeMode.compareAndSet(SysConst.BOOLEAN_FALSE, SysConst.BOOLEAN_TRUE)) {

            NullSafe.create()
                    .execute(() -> {

                        if (persistenceDebug) {
                            LogService.LogInfo(this.getClass(), ()
                                    -> String.format("%s: try to create/recreate entity manager ",
                                            this.persistenceUnitName));
                        }

                        if (NullSafe.notNull(this.getEntityManager())) {

                            if (this.getEntityManager().isOpen()) {
                                this.getEntityManager().flush();
                                this.getEntityManager().clear();
                                this.getEntityManager().close();
                            }
                        }

                        if (NullSafe.notNull(this.properties)) {
                            this.entityManager = factory.createEntityManager(properties);
                        } else {
                            this.entityManager = factory.createEntityManager();
                        }

                        if (persistenceDebug) {
                            LogService.LogInfo(this.getClass(), () -> String.format("%s: Successfully create entity manager (%s) ",
                                    this.persistenceUnitName,
                                    this.getEntityManager().getClass().getCanonicalName()));

                            LogService.LogInfo(this.getClass(), () -> String.format("EMF Properties \n %s ",
                                    this.getEmfProperties()));
                        }

                    });

            this.safeMode.set(SysConst.BOOLEAN_FALSE);
        }
        //}
    }

    //==========================================================================
    @Override
    public void beforeDestroy() {
        this.closeAll();
        super.beforeDestroy();
    }

    private void closeAll() {
        if (NullSafe.notNull(this.getEntityManager())) {
            this.getEntityManager().clear();
            this.getEntityManager().close();
        }
        if (NullSafe.notNull(this.factory)) {
            if (this.factory.isOpen()) {
                this.factory.close();
            }
        }
    }

    //==========================================================================
    protected void internalInitializeService(final Map<String, Object> properties) {

        NullSafe.create(persistenceUnitName)
                .execute(() -> {
//sessionFactory = new Configuration().configure().buildSessionFactory();
                    this.properties = properties;
                    this.factory = Persistence.createEntityManagerFactory(persistenceUnitName);
                    createOrUpdateEntityManager();
                });
    }

//    @Override
//
//    public void stopService() {
//        NullSafe.create(getEntityManager())
//                .safeExecute((ns_factory) -> {
//                    this.closeAll();
//                });
//        Service.super.stopService();
//    }
    //==========================================================================
    public String getPersistenceUnitName() {
        return persistenceUnitName;
    }

    public void setPersistenceUnitName(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    //==========================================================================
    public final void executePersistAction(final PersistenceAction persistAction) {

        NullSafe.create()
                .execute(() -> {
                    persistAction.execute(this.getEntityManager());
                })
                .throwException();
    }

    //==========================================================================
    public final void executeUserTransaction(final PersistenceAction persistAction) {

        NullSafe.create()
                .execute(() -> {

                    //final EntityTransaction entityTransaction = this.getEntityManager().getTransaction();
                    //@Resource
                    final UserTransaction utx = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");

                    NullSafe.create()
                            .execute(() -> {

                                utx.begin();
                                if (persistenceDebug) {
                                    LogService.LogInfo(this.getClass(), () -> String.format("Start user jpa transaction (%d)",
                                            this.getEntityManager().getTransaction().hashCode()).toUpperCase());
                                }

                                this.executePersistAction(persistAction);

                                this.getEntityManager().flush();

                                utx.commit();

                                //this.getEntityManager().clear();
                                if (persistenceDebug) {
                                    LogService.LogInfo(this.getClass(), () -> String.format("commit user jpa transaction (%d)",
                                            this.getEntityManager().getTransaction().hashCode()).toUpperCase());
                                }

//                        LogService.LogInfo(this.getClass(), () -> String.format("Finish jpa transaction (%d)",
//                                this.getEntityManager().getTransaction().hashCode()));
                            }).catchException((e) -> {

                        //if (this.getEntityManager().getTransaction().isActive()) {
                        LogService.LogErr(this.getClass(), () -> String.format("FAIL executeUserTransaction ('%s')",
                                NullSafe.getErrorMessage(e)).toUpperCase());
                        NullSafe.create(SysConst.STRING_NULL, NullSafe.DONT_THROW_EXCEPTION)
                                .execute(() -> utx.rollback());

                        //reCreateEntityManager();
                        //}
                    }).throwException();
                }).throwException();
    }

    //==========================================================================
    public final void executeTransaction(final PersistenceAction persistAction) {

        NullSafe.create()
                .execute(() -> {

                    final EntityTransaction entityTransaction = this.getEntityManager().getTransaction();

                    NullSafe.create()
                            .execute(() -> {

                                final Boolean isActiveTransaction = entityTransaction.isActive();

                                if (!isActiveTransaction) {
                                    entityTransaction.begin();
                                    if (persistenceDebug) {
                                        LogService.LogInfo(this.getClass(), () -> String.format("Start jpa transaction (%d)",
                                                this.getEntityManager().getTransaction().hashCode()).toUpperCase());
                                    }
                                }
                                if (!this.getEntityManager().isJoinedToTransaction()) {
                                    if (persistenceDebug) {
                                        LogService.LogInfo(this.getClass(), () -> String.format("Join transaction (%d)",
                                                this.getEntityManager().getTransaction().hashCode()).toUpperCase());
                                    }
                                    this.getEntityManager().joinTransaction();
                                }

                                this.executePersistAction(persistAction);

                                if (!isActiveTransaction) {

                                    this.getEntityManager().flush();

                                    entityTransaction.commit();

                                    //this.getEntityManager().clear();
                                    if (persistenceDebug) {
                                        LogService.LogInfo(this.getClass(), () -> String.format("commit jpa transaction (%d)",
                                                this.getEntityManager().getTransaction().hashCode()).toUpperCase());
                                    }
                                }

//                        LogService.LogInfo(this.getClass(), () -> String.format("Finish jpa transaction (%d)",
//                                this.getEntityManager().getTransaction().hashCode()));
                            }).catchException((e) -> {

                        //if (this.getEntityManager().getTransaction().isActive()) {
                        LogService.LogErr(this.getClass(), () -> String.format("FAIL executeTransaction ('%s')",
                                NullSafe.getErrorMessage(e)).toUpperCase());
                        NullSafe.create(SysConst.STRING_NULL, NullSafe.DONT_THROW_EXCEPTION)
                                .execute(() -> entityTransaction.rollback());

                        //reCreateEntityManager();
                        //}
                    }).throwException();
                }).throwException();
    }

    //==========================================================================
    public final String getEmfProperties() {
        final CustomCollectionImpl customCollection = NullSafe.createObject(CustomCollectionImpl.class, "Emf properties \n");

        this.factory.getProperties()
                .keySet()
                .stream()
                .unordered()
                .forEach(obj -> {

                    customCollection.addCustomRecord(() -> String.format("%40s = '%s'\n",
                            obj,
                            //ServiceFuncs.getStringObjValue(paramsMap.get(obj)), NullSafe.create(paramsMap.get(obj))
                            ServiceFuncs.getStringObjValue(this.factory.getProperties().get(obj))));
                });
        return customCollection.getRecord();
    }
    //==========================================================================
//    public final <T> Collection<T> executeNativeQuery(final String sql) {
//        return this.executeNativeQuery(sql, SysConst.STRING_NULL);
//    }

    //==========================================================================
    public final <T> Collection<T> executeNativeQuery(final String sql, final Class<? extends PersistenceQuery> clazz) {
        return this.executeNativeQuery(sql, clazz, null);
    }

    //==========================================================================
    public final <T> Collection<T> executeNativeQuery(final String sql,
            final Class<? extends PersistenceQuery> clazz,
            final QueryExecutor queryExecutor) {

        return NullSafe.create()
                .execute2result(() -> {

                    //final Collection<T> result; // = ServiceFuncs.<T>createCollection();
                    final Query query = this.getEntityManager()
                            .createNativeQuery(sql, clazz.getSimpleName());

                    if (persistenceDebug) {
                        LogService.LogInfo(this.getClass(), () -> String.format("executeNativeQuery: (%s)", sql));
                    }

                    if (NullSafe.notNull(queryExecutor)) {
                        queryExecutor.execute(query);
                    }

                    return query.getResultList();

                }).<Collection<T>>getObject();
    }

    //==========================================================================
    public <T extends PersistenceEntity> T createPersistenceEntity(final Class<T> persistanceClass, final PersisntanceEntityCreator<T> pee) {
        final T persistenceEntity = NullSafe.<T>createObject(persistanceClass);
        pee.create(persistenceEntity);

        this.executeTransaction(em -> em.persist(persistenceEntity));

        return persistenceEntity;
    }

    //==========================================================================
    public <T extends PersistenceEntity> T mergePersistenceEntity(final Class<T> persistanceClass, final PersisntanceEntityCreator<T> pee) {
        final T persistenceEntity = NullSafe.<T>createObject(persistanceClass);
        pee.create(persistenceEntity);

        this.executeTransaction(em -> em.merge(persistenceEntity));

        return persistenceEntity;
    }
}
