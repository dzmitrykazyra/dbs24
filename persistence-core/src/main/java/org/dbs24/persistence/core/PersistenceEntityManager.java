/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.persistence.core;

import static org.dbs24.application.core.sysconst.SysConst.*;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.application.core.nullsafe.NullSafe;
import javax.persistence.*;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import org.springframework.beans.factory.config.BeanDefinition;

@Data
@Slf4j
@Component
public class PersistenceEntityManager extends AbstractApplicationBean {

    private final AtomicBoolean safeMode = NullSafe.createObject(AtomicBoolean.class);
    //@PersistenceUnit
    private EntityManager entityManager;
    private EntityManagerFactory factory;
    @Value("${persistenceUnitName}")
    private String persistenceUnitName;
    @Value("${spring.datasource.url}")
    private String persistenceJdbcUrl;
    @Value("${spring.datasource.driver-class-name}")
    private String persistenceJdbcDriver;
    @Value("${spring.datasource.username}")
    private String persistenceJdbcUser;
    @Value("${spring.datasource.password}")
    private String persistenceJdbcPassword;
    @Value("${spring.jpa.database-platform}")
    private String hibernateDialect;
    private Map<String, Object> properties;
    @Value("${debug}")
    private String debugMode;
    @Value("${persistence.debug:false}")
    private Boolean persistenceDebug;
    @Value("${russian_ref_lang:false}")
    private String russianRefLan;
    @Value("${defaultJdbcBatchSize:false}")
    private Integer defaultJdbcBatchSize;

    @Autowired
    private GenericApplicationContext genericApplicationContext;

    @Override
    public void initialize() {

        NullSafe.create(persistenceUnitName)
                .execute(() -> {

                    RUSSIAN_REF_LANG.set(!this.russianRefLan.toLowerCase().equals(STRING_FALSE));

                    log.info("Try 2 create persistence '{}'", persistenceUnitName);

                    this.factory = Persistence.createEntityManagerFactory(persistenceUnitName, this.getProperties());

                    log.info("Persistence '{}' is created", persistenceUnitName);

                    createOrUpdateEntityManager();
                })
                .throwException();
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
        result.put("hibernate.order_inserts", "true");
        result.put("hibernate.order_updates", "true");
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

        if (this.safeMode.compareAndSet(BOOLEAN_FALSE, BOOLEAN_TRUE)) {

            NullSafe.create()
                    .execute(() -> {

                        log.debug(this.getBeansDefs());

                        log.info("{}: try to create/recreate entity manager ", this.persistenceUnitName);

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

                        log.info("{}: Successfully create entity manager ({}) ",
                                this.persistenceUnitName, this.getEntityManager().getClass().getCanonicalName());
                        log.info("EMF Properties \n {} ", this.getEmfProperties());
                    });

            this.safeMode.set(BOOLEAN_FALSE);
        }
    }

    //==========================================================================
    private String getBeansDefs() {

        final String[] beansDefs = genericApplicationContext
                .getBeanDefinitionNames();

        return Arrays.stream(beansDefs)
                .sorted((s1, s2) -> s1.compareTo(s2))
                .reduce(String.format("Spring beans list (%d): \n",
                        beansDefs.length),
                        (x, y) -> {
                            final BeanDefinition bd = genericApplicationContext.getBeanDefinition(y);
                            final String bcn = String.format("%s: %s [%s, %s]",
                                    y,
                                    bd.getBeanClassName(),
                                    bd.getScope(),
                                    bd.getResourceDescription());
                            return x.concat("\n").concat(bcn);
                        });
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
                                    log.debug(String.format("Start user jpa transaction ({})",
                                            this.getEntityManager().getTransaction().hashCode()).toUpperCase());
                                }

                                this.executePersistAction(persistAction);

                                this.getEntityManager().flush();

                                utx.commit();

                                //this.getEntityManager().clear();
                                if (persistenceDebug) {
                                    log.debug(String.format("commit user jpa transaction ({})",
                                            this.getEntityManager().getTransaction().hashCode()).toUpperCase());
                                }

//                        LogService.LogInfo(this.getClass(), () -> String.format("Finish jpa transaction (%d)",
//                                this.getEntityManager().getTransaction().hashCode()));
                            }).catchException((e) -> {

                        //if (this.getEntityManager().getTransaction().isActive()) {
                        log.debug(String.format("FAIL executeUserTransaction ('{}')",
                                NullSafe.getErrorMessage(e)).toUpperCase());
                        NullSafe.create(STRING_NULL, NullSafe.DONT_THROW_EXCEPTION).execute(() -> utx.rollback());

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
                                        log.debug(String.format("Start jpa transaction ({})",
                                                this.getEntityManager().getTransaction().hashCode()).toUpperCase());
                                    }
                                }
                                if (!this.getEntityManager().isJoinedToTransaction()) {
                                    if (persistenceDebug) {
                                        log.debug(String.format("Join transaction ({})",
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
                                        log.debug(String.format("commit jpa transaction ({})",
                                                this.getEntityManager().getTransaction().hashCode()).toUpperCase());
                                    }
                                }

//                        LogService.LogInfo(this.getClass(), () -> String.format("Finish jpa transaction (%d)",
//                                this.getEntityManager().getTransaction().hashCode()));
                            }).catchException(e -> {

                        //if (this.getEntityManager().getTransaction().isActive()) {
                        log.error("FAIL executeTransaction ({})",
                                NullSafe.getErrorMessage(e).toUpperCase());
                        NullSafe.create(STRING_NULL, NullSafe.DONT_THROW_EXCEPTION)
                                .execute(() -> entityTransaction.rollback());

                        //reCreateEntityManager();
                        //}
                    }).throwException();
                }).throwException();
    }

    //==========================================================================
    public final String getEmfProperties() {
        return this.factory.getProperties()
                .keySet()
                .stream()
                .reduce("EMF properties \n ",
                        (x, y) -> x.concat(" ").concat(String.format("%50s = '%s'\n",
                                y, NullSafe.getStringObjValue(System.getProperty(y)))));

    }
    //==========================================================================
//    public final <T> Collection<T> executeNativeQuery(final String sql) {
//        return this.executeNativeQuery(sql, STRING_NULL);
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
                        log.debug(String.format("executeNativeQuery: ({})", sql));
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