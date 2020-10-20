/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.core;

/**
 *
 * @author kazyra_d
 */
import com.fasterxml.jackson.annotation.JsonIgnore;
//import org.dbs24.jdbc.api.QueryRecordSet;
//import org.dbs24.application.core.exception.api.InternalAppException;
import org.dbs24.application.core.log.LogService;
import org.dbs24.references.api.LangStrValue;
import org.dbs24.references.api.RefClass;
import org.dbs24.references.api.ReferenceRec;
import java.util.Collection;
import java.util.Map;
import static org.dbs24.application.core.sysconst.SysConst.*;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.application.core.service.funcs.AnnotationFuncs;
import org.dbs24.application.core.log.LogService;
import org.dbs24.references.api.ReferenceCollection;
//import org.dbs24.references.api.AbstractBatch;
import org.dbs24.references.api.AbstractRefRecord;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
//import org.dbs24.registry.api.ApplicationSetup;
//import org.dbs24.services.api.ServiceLocator;
import org.dbs24.application.core.nullsafe.NullSafe;

// Р°Р±СЃС‚СЂР°РєС‚РЅС‹Р№ СЃРїСЂР°РІРѕС‡РЅРёРє
public abstract class AbstractReference<T extends ReferenceRec> extends ObjectRoot {

    public interface Reference {

        Object getReference();
    }

    public interface CachedReference {

        Boolean compareCachedReference(Object object);
    }
    //==========================================================================
    private Collection<T> refRecords;
    private Map<String, Integer> referenceMap;

    public AbstractReference() {
        loadReference(FORCED_RELOAD);
    }

    //protected abstract void loadReference(final Boolean needReload);
    protected void loadReference(final Boolean needReload) {

        // Р·Р°РіСЂСѓР·РёР»Рё РІ РєРµС€ СЃРїСЂР°РІРѕС‡РЅРёРє
        if (needReload) {

            // С‡РёСЃС‚РёРј СЃРїРёСЃРѕРє
            getRefRecords().clear();

            String tableName = (NullSafe.create(OBJECT_NULL, NullSafe.DONT_THROW_EXCEPTION)
                    .execute2result(() -> {
                        return ((RefClass) AnnotationFuncs.getAnnotation(this.getClass(), RefClass.class)).db_table_name();
                    })).<String>getObject();

            if ((NullSafe.isNull(tableName)) || tableName.isEmpty()) {
                tableName = "core_".concat(this.getClass().getSimpleName());
            }

//            this.getDbService()
//                    .createQuery(String.format("SELECT * FROM %s", tableName))
//                    .openAndProcessQuery(this.getRs());
            initReferenceMap();

        }
    }

    //==========================================================================
    protected void initReferenceMap() {

        getReferenceMap().clear();

        NullSafe.create()
                .execute(() -> {

                    (this.getRefRecords()).forEach((object) -> {

//                        ((ReferenceRec) object).record2Map(this.getReferenceMap());

                    });
                });
    }

    //==========================================================================
    public Map<String, Integer> getReferenceMap() {
        return NullSafe.create(this.referenceMap)
                .whenIsNull(() -> {
                    this.referenceMap = ServiceFuncs.<String, Integer>getOrCreateMap(ServiceFuncs.MAP_NULL);
                    return this.referenceMap;
                }).<Map<String, Integer>>getObject();
    }

    //==========================================================================
    protected <T> T findReference(final Reference reference, final String referenceNotFoundMsg) {
        T result = null;
        Boolean needReload = FORCED_RELOAD;

        do {
            needReload = !needReload;
            this.loadReference(needReload);
            result = (T) reference.getReference();
        } while ((NullSafe.isNull(result)) && (!needReload));

        NullSafe.create(result)
                .whenIsNull(() -> {
                    //String string = String.format("РќРµРёР·РІРµСЃС‚РЅС‹Р№ РєРѕРґ РїСЂРёР»РѕР¶РµРЅРёСЏ (ApplicationsRef.app_id=%d)", app_id);

//                    LogService.LogErr(this.getClass(), () -> String.format("%s: %s", LogService.getCurrentObjProcName(this),
//                            referenceNotFoundMsg));
                    return null;
                });

        return result;
    }

    //==========================================================================
    protected T findCachedRecords(final CachedReference cachedReference) {

        return ServiceFuncs.<T>getCollectionElement(this.getRefRecords(),
                p -> cachedReference.compareCachedReference(p),
                String.format("can't find cached reference (%s)", this.getClass().getCanonicalName()));

    }

    //==========================================================================
    public Collection<T> getRefRecords() {

        this.refRecords = ServiceFuncs.<T>getOrCreateCollection(this.refRecords);

        return refRecords;
    }

    //==========================================================================
    public void setRefRecords(final Collection<T> refRecords) {
        this.refRecords = refRecords;
    }

    //==========================================================================
//    @JsonIgnore
//    public QueryRecordSet getRs() {
//        return (qry) -> {
//            NullSafe.create()
//                    .execute(() -> {
//
//                        final Class<T> clazz = (NullSafe.create(OBJECT_NULL, NullSafe.DONT_THROW_EXCEPTION)
//                                .execute2result(() -> {
//                                    // класс справочника
//                                    return ((RefClass) AnnotationFuncs.getAnnotation(this.getClass(), RefClass.class)).reference_class();
//                                }))
//                                .whenIsNull(() -> {
//
//                                    final String refClassName = this.getClass().getName();
//                                    String className;
//                                    Class<T> clazzz = null;
//                                    int i = 0;
//
//                                    // РЅРµ Р·Р°РґР°РЅ - РїСЂРѕР±СѓРµРј РІР·СЏС‚СЊ РёР· РёРјРµРЅРё СЃРІСЏР·Р°РЅРЅРѕРіРѕ СЃРїСЂР°РІРѕС‡РЅРёРєР°
//                                    while (NullSafe.isNull(clazzz)) {
//
//                                        className = refClassName.substring(0, refClassName.length() - 4 - i);
//
//                                        try {
//                                            clazzz = (Class<T>) Class.forName(className);
//                                        } catch (Throwable ex) {
//
//                                            if (i > 0) {
//
//                                                final String finalMsg = String.format("Ошибка получения класса справочника (%s, '%s')", className,
//                                                        NullSafe.getErrorMessage(ex));
//
//                                                LogService.LogErr(this.getClass(),
//                                                        () -> finalMsg);
//                                                break;
//                                            }
//                                            i++;
//                                        }
//                                    }
//                                    return clazzz;
//                                }).<Class<T>>getObject();
//
//                        if (NullSafe.notNull(clazz)) {
//                            this.setRefRecords(qry.<T>createCollection(clazz));
//                        }
//
//                    })
//                    .finallyBlock(() -> qry.closeRecordSet());
//        };
//    }

    //==========================================================================
    protected static Boolean needUpdate(final Class clazz, final Long refHashCode) {
        return false;
//        return !(NullSafe.create(ObjectRoot.getStaticDbService()
//                .createCallQuery("{:RES = call ref_get_version(:REF)}")
//                .setParamByNameAsOutput("RES", LONG_ZERO)
//                .setParamByName("REF", clazz.getSimpleName())
//                .<Long>getSingleFieldValue())
//                .<Long>getObject()
//                .equals(refHashCode));
    }
    //==========================================================================

//    protected static <V extends ReferenceRec> void store(
//            final ReferenceCollection rc,
//            final String sqlStmt,
//            final AbstractBatch<V> batch) {

//        NullSafe.create(NullSafe.create()
//                .execute2result(() -> Class.forName(Thread.currentThread().getStackTrace()[4].getClassName()))
//                .<Class>getObject())
//                .safeExecute((ns_clazz) -> {
//
//                    final Class class_ns = (Class) ns_clazz;
//
//                    // сумма хэшей справочника
//                    final long hashCodeSum = rc
//                            .getRefCollection()
//                            .stream()
//                            .mapToLong(record -> ((AbstractRefRecord) record).calcRecordHash())
//                            .sum();
//
//                    if (INTEGER_ZERO.equals(hashCodeSum)) {
//                        LogGate.LogErr((Class) ns_clazz,
//                                () -> String.format("Ошибка подсчета суммы хешей записей справочника (%s)",
//                                        class_ns.getCanonicalName()));
//                    } else {
//
//                        // хэш изменился, нужно обновление справочника в БД
//                        if ((AbstractReference.needUpdate(class_ns, hashCodeSum))) {
//
//                            // сохранили записи справочника в БД
//                            NullSafe.create()
//                                    .execute(() -> {
//                                        ObjectRoot.getStaticDbService()
//                                                .createCallBath(sqlStmt)
//                                                .execBatch(stmt -> {
//                                                    rc
//                                                            .getRefCollection()
//                                                            .stream()
//                                                            .forEach((record) -> {
//                                                                batch.fillBatch(stmt, (V) record);
//                                                                stmt.addBatch();
//                                                            });
//                                                }).commit();
//
//                                        (ObjectRoot.getStaticDbService()
//                                                .createCallQuery("{call update_ref_version(:R, :V)} ")
//                                                .setParamByName("R", ((Class) ns_clazz).getSimpleName())
//                                                .setParamByName("V", hashCodeSum)
//                                                .execCallStmt())
//                                                .commit();
//
//                                        LogService.LogInfo(class_ns, () -> String.format("updating reference '%s'", class_ns.getCanonicalName()).toUpperCase());
//                                    });
//                        }
//                    }
//                }).throwException();
//    }
    
    //==========================================================================
    private static Boolean russianRefLang = BOOLEAN_NULL;

    private static Boolean useRussianRefLang() {

//        if (NullSafe.isNull(AbstractReference.russianRefLang)) {
//            AbstractReference.russianRefLang = ServiceLocator.find(ApplicationSetup.class).getRegParam("fs24.reference.lang", "ru").equals("ru");
//        }

        return AbstractReference.russianRefLang;
    }

    public static String getTranslatedValue(final LangStrValue langStrValue) {

        return (AbstractReference.useRussianRefLang()
                ? langStrValue.getRu() : langStrValue.getEn()); // 
    }

//==========================================================================
//public abstract Integer sortOrder();
}
