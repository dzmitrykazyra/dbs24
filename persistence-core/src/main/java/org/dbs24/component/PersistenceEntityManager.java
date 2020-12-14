/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import javax.persistence.*;
import static org.dbs24.consts.SysConst.*;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.persistence.api.PersistenceAction;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.dbs24.spring.core.api.AbstractApplicationBean;
import java.util.Collection;
import org.dbs24.persistence.api.QueryExecutor;
import org.dbs24.persistence.api.PersistenceQuery;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.config.BeanDefinition;
import reactor.core.publisher.Mono;
import org.dbs24.persistence.core.*;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Data
@Log4j2
@Component
@EnableScheduling
@EnableAsync
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
    @Value("${defaultJdbcBatchSize:100}")
    private Integer defaultJdbcBatchSize;
    @Value("${hibernate.c3p0.max_size:5}")
    private String c3poMaxConnections;
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

        NullSafe.create(persistenceUnitName)
                .execute(() -> {

                    RUSSIAN_REF_LANG.set(!this.russianRefLan.toLowerCase().equals(STRING_FALSE));

                    log.debug("Try 2 create persistence '{}'", persistenceUnitName);

                    this.factory = Persistence.createEntityManagerFactory(persistenceUnitName, this.getProperties());

                    log.debug("Persistence '{}' is created", persistenceUnitName);

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
        result.put("hibernate.cache.use_second_level_cache", "false");
        result.put("hibernate.connection.isolation", hibernateConnectionIsolation);
        result.put("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        result.put("hibernate.dialect", hibernateDialect);
        result.put("exclude-unlisted-classes", "true");
        result.put("hibernate.show_sql", hibernateShowSql);
        result.put("hibernate.format_sql", hibernateFormatSql);
        result.put("hibernate.c3p0.min_size", "1");
        result.put("hibernate.c3p0.max_size", c3poMaxConnections);

        return result;
    }
    //========================================================================

    private void createOrUpdateEntityManager() {

        if (this.safeMode.compareAndSet(BOOLEAN_FALSE, BOOLEAN_TRUE)) {

            StmtProcessor.execute(() -> {

                //log.debug(this.getBeansDefs());
                log.debug("{}: try to create/recreate entity manager ", this.persistenceUnitName);

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

                log.debug("{}: Successfully create entity manager ({}, {}) ",
                        this.persistenceUnitName, this.getEntityManager().getClass().getCanonicalName());

                log.info("Successfully connection to {} (UID={}, PWD=***) ",
                        this.persistenceJdbcUrl,
                        this.persistenceJdbcUser);

                if (persistenceDebug) {
                    log.debug("EMF Properties \n {} ", this.getEmfProperties());
                }
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
    protected void internalInitializeService(Map<String, Object> properties) {

        NullSafe.create(persistenceUnitName)
                .execute(() -> {
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

            Mono.just(this)
                    .log()
                    .subscribe(new Subscriber() {

                        final Boolean isActiveTransaction = entityTransaction.isActive();
                        final StopWatcher stopWatcher = StopWatcher.create();

                        @Override
                        public void onSubscribe(Subscription s) {
                            s.request(Long.MAX_VALUE);
                        }

                        @Override
                        public void onNext(Object action) {

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
                        }

                        @Override
                        public void onError(Throwable t) {

                            log.error("{}: FAIL executeTransaction: ('{}')",
                                    getClass().getSimpleName(),
                                    NullSafe.getErrorMessage(t).toUpperCase());
                        }

                        @Override
                        public void onComplete() {
                            log.debug(stopWatcher.getStringExecutionTime());
                        }
                    });
        });
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
    public final <T> Collection<T> executeNativeQuery(String sql, Class<? extends PersistenceQuery> clazz) {
        return this.executeNativeQuery(sql, clazz, null);
    }

    //==========================================================================
    public final <T> Collection<T> executeNativeQuery(String sql,
            final Class<? extends PersistenceQuery> clazz,
            final QueryExecutor queryExecutor) {

        return NullSafe.create()
                .execute2result(() -> {

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
                transactionEntityManager.getIsBusy().set(Boolean.TRUE);
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

            mgrList.removeIf(em -> {

                final Boolean toRemove = (Boolean.FALSE.equals(em.getIsBusy().get()) || (!em.getEntityManager().isOpen()));

                if (toRemove) {
                    if (em.getEntityManager().isOpen()) {
                        em.getEntityManager().clear();
                        em.getEntityManager().close();
                    }
                    log.info("remove transaction/session: {}", em.getEntityManager());
                }

                return toRemove;
            });
        }

        log.info("transaction sessions: {}", mgrList.size());
    }

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
}
