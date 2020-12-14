/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.service.funcs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dbs24.application.core.exception.api.InternalAppException;
import org.dbs24.application.core.nullsafe.NullSafe;
import static org.dbs24.consts.SysConst.*;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import org.dbs24.stmt.StmtProcessor;

public final class ServiceFuncs {

    public static final Collection COLLECTION_NULL = null;
    public static final Map MAP_NULL = null;
    public static final Boolean THROW_WHEN_NOT_FOUND = Boolean.TRUE;
    public static final Boolean SF_DONT_THROW_EXC = Boolean.FALSE;

    public static <T> Collection<T> getOrCreateCollection(Collection<T> existCollection) {

        final Collection<T> collection;

        if (null != existCollection) {
            collection = existCollection;
        } else {
            collection = NullSafe.createObject(ArrayList.class);
        }

        return collection;
    }

    //==========================================================================
    public static <T> Collection<T> createCollection() {

        return StmtProcessor.create(ArrayList.class);
    }

    //==========================================================================
    public static <T> Collection<T> createConcurencyCollection() {

        return StmtProcessor.create(CopyOnWriteArrayList.class);
    }

    //==========================================================================
    public static <T, V> Map<T, V> createMap() {

        return StmtProcessor.create(HashMap.class);
    }

    //==========================================================================
    public static <T> Collection<T> getOrCreateCollection_Safe(Collection<T> existCollection) {

        final Collection<T> collection;

        if (null != existCollection) {
            collection = existCollection;
        } else {
            collection = NullSafe.createObject(CopyOnWriteArrayList.class);
        }

        return collection;
    }

    //==========================================================================   
    @Deprecated
    public static <T> T getCollectionElement(Collection<T> collection,
            final FilterComparator<T> filterComparator,
            final String exceptionMessage) {

        synchronized (collection) {

            return NullSafe.create(OBJECT_NULL, !ServiceFuncs.THROW_WHEN_NOT_FOUND)
                    .execute2result(() -> {

                        return collection
                                .stream()
                                .unordered()
                                .filter((fltr) -> filterComparator.getFilter(fltr))
                                .collect(Collectors.toList())
                                .get(0);
                    }).catchException(e -> {
                throw new RuntimeException(String.format("%s ('%s:%s')",
                        exceptionMessage,
                        e.getClass().getCanonicalName(),
                        NullSafe.getErrorMessage(e)));
            })
                    .throwException()
                    .<T>getObject();
        }
    }

    @Deprecated
    //==========================================================================
    public static <T> T getCollectionElement_silent(Collection<T> collection,
            final FilterComparator<T> filterComparator) {

        synchronized (collection) {

            return (T) NullSafe.create(OBJECT_NULL, !ServiceFuncs.SF_DONT_THROW_EXC)
                    .execute2result(() -> {

                        return collection
                                .stream()
                                .unordered()
                                .filter((fltr) -> filterComparator.getFilter(fltr))
                                .collect(Collectors.toList())
                                .get(0);

                    })
                    .<T>getObject();
        }
    }

    //==========================================================================
    //==========================================================================
    @Deprecated
    public static <T> Collection<T> filterCollection_Silent(Collection<T> collection,
            final FilterComparator<T> filterComparator) {

        return NullSafe.create(OBJECT_NULL, !ServiceFuncs.THROW_WHEN_NOT_FOUND)
                .execute2result(() -> {

                    return collection
                            .stream()
                            .unordered()
                            .filter((fltr) -> filterComparator.getFilter(fltr))
                            .collect(Collectors.toList());

                })
                .throwException()
                .<Collection<T>>getObject();

    }

    //==========================================================================
    public static <K, V> Map<K, V> getOrCreateMap(Map<K, V> existMap) {

        final Map<K, V> map;

        if (null != existMap) {
            map = existMap;
        } else {
            map = NullSafe.createObject(HashMap.class);
        }

        return map;
    }
    //==========================================================================

    public static <K, V> Map<K, V> getOrCreateMap_Safe(Map<K, V> existMap) {

        final Map<K, V> map;

        if (null != existMap) {
            map = existMap;
        } else {
            map = NullSafe.createObject(ConcurrentHashMap.class);
        }

        return map;
    }
    //==========================================================================

    public static <K, V> Map.Entry<K, V> getMapEntry(Map<K, V> collection,
            final MapComparator<K, V> mapComparator,
            final String exceptionMessage,
            final Boolean raiseExceptionWhenNotFound) {
        synchronized (collection) {
            return NullSafe.create(OBJECT_NULL, !raiseExceptionWhenNotFound)
                    .execute2result(() -> {

                        return collection
                                .entrySet()
                                .stream()
                                .unordered()
                                .filter((mapEntry) -> mapComparator.filterMap(mapEntry))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                                .entrySet()
                                .iterator()
                                .next();

                    }).catchException(e -> {
                throw new RuntimeException(String.format("%s ('%s')",
                        exceptionMessage,
                        NullSafe.getErrorMessage(e)));
            })
                    .throwException()
                    .<Map.Entry<K, V>>getObject();
        }
    }

    @Deprecated
    //==========================================================================
    public static <K, V> V getMapValue(Map<K, V> collection,
            final MapComparator<K, V> mapComparator,
            final String exceptionMessage,
            final Boolean raiseExceptionWhenNotFound) {
        synchronized (collection) {
            return NullSafe.create(OBJECT_NULL, !raiseExceptionWhenNotFound)
                    .execute2result(() -> {

                        return collection
                                .entrySet()
                                .stream()
                                .unordered()
                                .filter((mapEntry) -> {
                                    return mapComparator.filterMap(mapEntry);
                                })
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                                .entrySet()
                                .iterator()
                                .next()
                                .getValue();

                    }).catchException(e -> {
                throw new RuntimeException(String.format("%s ('%s')",
                        exceptionMessage,
                        NullSafe.getErrorMessage(e)));
            })
                    .throwException()
                    .<V>getObject();
        }
    }

    //==========================================================================
    @Deprecated
    public static <K, V> V getMapValue_silent(Map<K, V> collection,
            final MapComparator<K, V> mapComparator) {
        synchronized (collection) {
            return NullSafe.create(OBJECT_NULL, !ServiceFuncs.SF_DONT_THROW_EXC)
                    .execute2result(() -> {

                        return collection
                                .entrySet()
                                .stream()
                                .unordered()
                                .filter((mapEntry) -> mapComparator.filterMap(mapEntry))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                                .entrySet()
                                .iterator()
                                .next()
                                .getValue();
                    })
                    .<V>getObject();
        }
    }

    //==========================================================================
    public static <K, V> Optional<V> getMapValue(Map<K, V> collection,
            final MapComparator<K, V> mapComparator) {
        synchronized (collection) {

            return Optional.ofNullable(NullSafe.create(OBJECT_NULL, !ServiceFuncs.SF_DONT_THROW_EXC)
                    .execute2result(() -> collection
                    .entrySet()
                    .stream()
                    .unordered()
                    .filter((mapEntry) -> mapComparator.filterMap(mapEntry))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                    .entrySet()
                    .iterator()
                    .next()
                    .getValue())
                    .<V>getObject());
        }
    }
    //==========================================================================

    public static <T> Optional<T> getCollectionElement(Collection<T> collection,
            final FilterComparator<T> filterComparator) {

        synchronized (collection) {

            return collection
                    .stream()
                    .unordered()
                    .filter((fltr) -> filterComparator.getFilter(fltr))
                    .findFirst();
        }
    }

    //==========================================================================
    static class ElementNotFound extends InternalAppException {

        public ElementNotFound(String message) {
            super(message);
        }
    }

    public static <T> T findCollectionElement(Collection<T> collection,
            final FilterComparator<T> filterComparator,
            final String excMsg) {

        synchronized (collection) {

            return NullSafe.create(OBJECT_NULL)
                    .execute2result(() -> {

                        return collection
                                .stream()
                                .unordered()
                                .filter((fltr) -> filterComparator.getFilter(fltr))
                                .findFirst()
                                .orElseThrow(() -> new ElementNotFound(excMsg));
                    })
                    .throwException()
                    .<T>getObject();
        }
    }

    //==========================================================================
    public static <K, V> K getMapKeyElement(Map<K, V> collection,
            final MapComparator<K, V> mapComparator,
            final String exceptionMessage,
            final Boolean raiseExceptionWhenNotFound) {

        synchronized (collection) {

            return NullSafe.create(OBJECT_NULL, !raiseExceptionWhenNotFound)
                    .execute2result(() -> {

                        return collection
                                .entrySet()
                                .stream()
                                .unordered()
                                .filter((mapEntry) -> {
                                    return mapComparator.filterMap(mapEntry);
                                })
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                                .entrySet()
                                .iterator()
                                .next()
                                .getKey();

                    }).catchException(e -> {
                throw new RuntimeException(String.format("%s ('%s')",
                        exceptionMessage,
                        NullSafe.getErrorMessage(e)));
            })
                    .throwException()
                    .<K>getObject();
        }
    }

    //=========================================================================================
//    public static <T extends Class> Collection<T> createPkgClassesCollection( String packageName, T rootClazz) {
//
//        return new ArrayList<>((Collection<T>) (new Reflections(packageName)).getSubTypesOf(rootClazz));
//    }
//
//    //==========================================================================
//    public static <T> Collection<T> createPkgClassesCollectionExt( String packageName, Class<T> rootClazz) {
//
//        return new ArrayList<>((Collection<T>) (new Reflections(packageName)).getSubTypesOf(rootClazz));
//    }
    //=========================================================================================
//    public static <T> ArrayList<T> createCollection( Set<Class<? extends T>> someSet) {
//
//        return new ArrayList<T>(someSet);
//    }
    //==========================================================================
    public static <T> T getPropertySafe(PropertyGetter propertyGetter, T default_value) {
        return (T) NullSafe.create((Object) default_value, NullSafe.DONT_THROW_EXCEPTION)
                .execute2result(() -> propertyGetter.getProperty())
                .<T>getObject();
    }

    //==========================================================================
    public static <T> T getPropertySafe(PropertyGetter propertyGetter) {
        return (T) ServiceFuncs.<T>getPropertySafe(propertyGetter, (T) OBJECT_NULL);
    }

    //==========================================================================    
    public static String getStringObjValue(Object object) {

        return NullSafe.create(STRING_NULL, NullSafe.DONT_THROW_EXCEPTION)
                .whenIsNull(() -> ((LocalDate) object).format(FORMAT_dd_MM_yyyy))
                .whenIsNull(() -> String.format("%s", object))
                .whenIsNull(() -> String.format("%d", object))
                .whenIsNull(() -> String.format("%f", object))
                .whenIsNull(() -> String.format("%b", object))
                .whenIsNull(() -> NOT_DEFINED)
                .<String>getObject();
    }

    //==========================================================================
    public static String getJsonFromObject(Object object) {
        return NullSafe.create()
                .execute2result(() -> new ObjectMapper().writeValueAsString(object))
                .<String>getObject();
    }

}
