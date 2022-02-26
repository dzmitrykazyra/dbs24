/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.persistence.api.PersistenceAction;
import org.dbs24.persistence.core.TransactionEntityManager;
import org.dbs24.persistence.core.TransactionStmts;
import org.dbs24.spring.core.api.AbstractApplicationBean;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static org.dbs24.consts.SysConst.RUSSIAN_REF_LANG;
import static org.dbs24.consts.SysConst.STRING_FALSE;

@Getter
@Log4j2
//@Component
@EnableScheduling
@EnableAsync
public abstract class PersistenceService extends AbstractApplicationBean {

    //@PersistenceUnit
    private EntityManager entityManager;
    private EntityManagerFactory factory;
    @Value("${config.persistence.name}")
    private String persistenceUnitName;
    @Value("${spring.datasource.url}") // spring.datasource.jdbc-url
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
    @Value("${defaultJdbcBatchSize:100}")
    private Integer defaultJdbcBatchSize;
    //    @Value("${hibernate.c3p0.max_size:10}")
//    private String c3poMaxConnections;
    @Value("${hibernate.connection.isolation:2}") // TRANSACTION_READ_COMMITTED
    private Integer hibernateConnectionIsolation;
    @Value("${hibernate.show_sql:false}")
    private String hibernateShowSql;
    @Value("${hibernate.format_sql:false}")
    private String hibernateFormatSql;

    @Value("${entity.manager.free.unused.min.amount:3}")
    private Integer minAmount;

    @Autowired
    private GenericApplicationContext genericApplicationContext;

    @Override
    public void initialize() {

        StmtProcessor.execute(() -> {

            RUSSIAN_REF_LANG.set(!this.russianRefLan.toLowerCase().equals(STRING_FALSE));

            log.debug("Try 2 create persistence '{}'", persistenceUnitName);

            this.factory = Persistence.createEntityManagerFactory(persistenceUnitName, this.getProperties());

            log.debug("Persistence '{}' is created", persistenceUnitName);

            createOrUpdateEntityManager();
        });

        super.initialize();

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
        result.put("hibernate.cache.use_second_level_cache", "false");
        result.put("hibernate.connection.isolation", hibernateConnectionIsolation);
        result.put("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        result.put("hibernate.dialect", hibernateDialect);
        result.put("exclude-unlisted-classes", "true");
        result.put("hibernate.show_sql", hibernateShowSql);
        result.put("hibernate.format_sql", hibernateFormatSql);
//        result.put("hibernate.c3p0.min_size", "3");
//        result.put("hibernate.c3p0.max_size", c3poMaxConnections);

        return result;
    }
    //========================================================================

    private void createOrUpdateEntityManager() {

        StmtProcessor.execute(() -> {

            //log.debug(this.getBeansDefs());
            log.debug("{}: try to create/recreate entity manager ", this.persistenceUnitName);

            if (StmtProcessor.notNull(this.getEntityManager())) {

                if (getEntityManager().isOpen()) {
                    getEntityManager().flush();
                    getEntityManager().clear();
                    getEntityManager().close();
                }
            }

            if (StmtProcessor.notNull(this.properties)) {
                entityManager = factory.createEntityManager(properties);

            } else {
                entityManager = factory.createEntityManager();
            }

            log.debug("{}: Successfully create entity manager ({}, {}) ",
                    persistenceUnitName,
                    getEntityManager().getClass().getCanonicalName(),
                    persistenceJdbcUrl);

            log.info("Successfully connection to {} (UID={}, PWD=*********) ",
                    persistenceJdbcUrl,
                    persistenceJdbcUser);

            if (persistenceDebug) {
                log.debug("EMF Properties \n {} ", this.getEmfProperties());
            }
        });
    }

    //==========================================================================
    @Override
    public void shutdown() {
        closeAll();
        super.shutdown();
    }

    private void closeAll() {
        if (StmtProcessor.notNull(this.getEntityManager())) {
            getEntityManager().clear();
            getEntityManager().close();
        }
        if (StmtProcessor.notNull(this.getFactory())) {
            if (getFactory().isOpen()) {
                getFactory().close();
            }
        }
    }

    //==========================================================================
    protected void internalInitializeService(Map<String, Object> properties) {

        StmtProcessor.execute(() -> {
            this.properties = properties;
            this.factory = Persistence.createEntityManagerFactory(persistenceUnitName);
            createOrUpdateEntityManager();
        });
    }

    //==========================================================================
    public final void executePersistAction(PersistenceAction persistAction, EntityManager entityManager) {
        persistAction.execute(entityManager);
    }

    //==========================================================================
    public final void executeTransaction(PersistenceAction persistAction) {
        this.executeTransaction(null, persistAction);
    }

    //==========================================================================
    public final void executeTransaction(EntityManager existingEM, PersistenceAction persistAction) {

        this.executeTransactionInternal(existingEM, trEntityManager -> {
            final EntityTransaction entityTransaction = trEntityManager.getTransaction();

            final Boolean isActiveTransaction = entityTransaction.isActive();
            final StopWatcher stopWatcher = StopWatcher.create();

            if (!isActiveTransaction) {
                entityTransaction.begin();
            }

            if (!trEntityManager.isJoinedToTransaction()) {
                trEntityManager.joinTransaction();
            }

            executePersistAction(persistAction, trEntityManager);
            if (!isActiveTransaction) {
                trEntityManager.flush();
                entityTransaction.commit();
            }
            log.debug(stopWatcher.getStringExecutionTime());

        });
    }

    //==========================================================================
    public final String getEmfProperties() {
        return this.getFactory().getProperties()
                .keySet()
                .stream()
                .reduce("EMF properties \n ",
                        (x, y) -> x.concat(" ").concat(String.format("%50s = '%s'\n",
                                y, StmtProcessor.getObj2String(System.getProperty(y)))));
    }

    //==========================================================================
    private final Collection<TransactionEntityManager> mgrList = ServiceFuncs.<TransactionEntityManager>createCollection();

    public void add(EntityManager entityManager) {
        mgrList.add(TransactionEntityManager.create(entityManager));
    }

    //==========================================================================
    private void executeTransactionInternal(EntityManager existingEM, TransactionStmts transactionStmts) {

        if (StmtProcessor.notNull(existingEM)) {
            transactionStmts.execute(existingEM);
        } else {

            final TransactionEntityManager transactionEntityManager;

            synchronized (TransactionEntityManager.class) {

                transactionEntityManager = mgrList
                        .stream()
                        .filter(em -> Boolean.FALSE.equals(em.getIsBusy().get()))
                        .findFirst()
                        .orElseGet(() -> {
                            final TransactionEntityManager tem
                                    = TransactionEntityManager.create(this.getFactory().createEntityManager());

                            mgrList.add(tem);

                            log.info("create transaction/session: {}", mgrList.size());

                            return tem;
                        });

                transactionEntityManager.getIsBusy()
                        .set(Boolean.TRUE);
            }

            try {
                transactionStmts.execute(transactionEntityManager.getEntityManager());
                transactionEntityManager.getEntityManager().clear();
                transactionEntityManager.getIsBusy().set(Boolean.FALSE);
            } catch (Throwable t) {
                log.error("executeTransactionInternal: {}, {}", t.getMessage(), transactionEntityManager.getEntityManager());
                transactionEntityManager.getEntityManager().clear();
                transactionEntityManager.getEntityManager().close();
            }
        }
    }

    //==========================================================================
    @Scheduled(fixedRateString = "${entity.manager.free.unused.interval:240000}")
    private void freeUnusedTransactionEntityManagers() {

        synchronized (TransactionEntityManager.class) {

            mgrList.removeIf(em
                            -> {

                        final Boolean toRemove = (Boolean.FALSE.equals(em.getIsBusy().get()) || (!em.getEntityManager().isOpen()));

                        if (toRemove) {
                            if (em.getEntityManager().isOpen()) {
                                em.getEntityManager().clear();
                                em.getEntityManager().close();
                            }
                            log.debug("remove transaction session: {}/{}", em.getEntityManager(), mgrList.size());
                        }

                        return toRemove;
                    }
            );
        }

        //log.info("transaction sessions: {}", mgrList.size());
    }

    //==========================================================================
    @Override
    public void destroy() {
        mgrList.removeIf(em -> {
            log.info("close transaction/session {}", em.getEntityManager());
            if (em.getEntityManager().isOpen()) {
                em.getEntityManager().flush();
                em.getEntityManager().clear();
                em.getEntityManager().close();
            }
            return true;
        });
    }

    //==========================================================================
    public <T> Optional<T> findEntityById(Class<T> entClass, Integer entityId) {
        return Optional.ofNullable(entityManager.find(entClass, entityId));
    }

    //==========================================================================
    public <T> Optional<T> findEntityById(Class<T> entClass, Long entityId) {
        return Optional.ofNullable(entityManager.find(entClass, entityId));
    }
}
