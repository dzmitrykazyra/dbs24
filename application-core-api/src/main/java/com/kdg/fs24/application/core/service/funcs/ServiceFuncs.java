/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.application.core.service.funcs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdg.fs24.application.core.exception.api.InternalAppException;
import com.kdg.fs24.application.core.nullsafe.NullSafe;
import com.kdg.fs24.application.core.sysconst.SysConst;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 *
 * @author Козыро Дмитрий
 */
public final class ServiceFuncs {

    public static final Collection COLLECTION_NULL = null;
    public static final Map MAP_NULL = null;
    public static final Boolean THROW_WHEN_NOT_FOUND = Boolean.TRUE;
    public static final Boolean SF_DONT_THROW_EXC = Boolean.FALSE;

    public static <T> Collection<T> getOrCreateCollection(final Collection<T> existCollection) {

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

        return NullSafe.createObject(ArrayList.class);
    }
    //==========================================================================
    public static <T> Collection<T> createConcurencyCollection() {

        return NullSafe.createObject(CopyOnWriteArrayList.class);
    }

    
    //==========================================================================
    public static <T> Collection<T> getOrCreateCollection_Safe(final Collection<T> existCollection) {

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
    public static <T> T getCollectionElement(final Collection<T> collection,
            final FilterComparator<T> filterComparator,
            final String exceptionMessage) {

        synchronized (collection) {

            return NullSafe.create(SysConst.OBJECT_NULL, !ServiceFuncs.THROW_WHEN_NOT_FOUND)
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
    public static <T> T getCollectionElement_silent(final Collection<T> collection,
            final FilterComparator<T> filterComparator) {

        synchronized (collection) {

            return (T) NullSafe.create(SysConst.OBJECT_NULL, !ServiceFuncs.SF_DONT_THROW_EXC)
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
    public static <T> Collection<T> filterCollection_Silent(final Collection<T> collection,
            final FilterComparator<T> filterComparator) {

        return NullSafe.create(SysConst.OBJECT_NULL, !ServiceFuncs.THROW_WHEN_NOT_FOUND)
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
    public static <K, V> Map<K, V> getOrCreateMap(final Map<K, V> existMap) {

        final Map<K, V> map;

        if (null != existMap) {
            map = existMap;
        } else {
            map = NullSafe.createObject(HashMap.class);
        }

        return map;
    }
    //==========================================================================

    public static <K, V> Map<K, V> getOrCreateMap_Safe(final Map<K, V> existMap) {

        final Map<K, V> map;

        if (null != existMap) {
            map = existMap;
        } else {
            map = NullSafe.createObject(ConcurrentHashMap.class);
        }

        return map;
    }
    //==========================================================================

    public static <K, V> Map.Entry<K, V> getMapEntry(final Map<K, V> collection,
            final MapComparator<K, V> mapComparator,
            final String exceptionMessage,
            final Boolean raiseExceptionWhenNotFound) {
        synchronized (collection) {
            return NullSafe.create(SysConst.OBJECT_NULL, !raiseExceptionWhenNotFound)
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
    public static <K, V> V getMapValue(final Map<K, V> collection,
            final MapComparator<K, V> mapComparator,
            final String exceptionMessage,
            final Boolean raiseExceptionWhenNotFound) {
        synchronized (collection) {
            return NullSafe.create(SysConst.OBJECT_NULL, !raiseExceptionWhenNotFound)
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
    public static <K, V> V getMapValue_silent(final Map<K, V> collection,
            final MapComparator<K, V> mapComparator) {
        synchronized (collection) {
            return NullSafe.create(SysConst.OBJECT_NULL, !ServiceFuncs.SF_DONT_THROW_EXC)
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
    public static <K, V> Optional<V> getMapValue(final Map<K, V> collection,
            final MapComparator<K, V> mapComparator) {
        synchronized (collection) {

            return Optional.ofNullable(NullSafe.create(SysConst.OBJECT_NULL, !ServiceFuncs.SF_DONT_THROW_EXC)
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
                    .<V>getObject());
        }
    }
    //==========================================================================

    public static <T> Optional<T> getCollectionElement(final Collection<T> collection,
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

        public ElementNotFound(final String message) {
            super(message);
        }
    }

    public static <T> T findCollectionElement(final Collection<T> collection,
            final FilterComparator<T> filterComparator,
            final String excMsg) {

        synchronized (collection) {

            return NullSafe.create(SysConst.OBJECT_NULL)
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
    public static <K, V> K getMapKeyElement(final Map<K, V> collection,
            final MapComparator<K, V> mapComparator,
            final String exceptionMessage,
            final Boolean raiseExceptionWhenNotFound) {

        synchronized (collection) {

            return NullSafe.create(SysConst.OBJECT_NULL, !raiseExceptionWhenNotFound)
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
//    public static <T extends Class> Collection<T> createPkgClassesCollection(final String packageName, final T rootClazz) {
//
//        return new ArrayList<>((Collection<T>) (new Reflections(packageName)).getSubTypesOf(rootClazz));
//    }
//
//    //==========================================================================
//    public static <T> Collection<T> createPkgClassesCollectionExt(final String packageName, final Class<T> rootClazz) {
//
//        return new ArrayList<>((Collection<T>) (new Reflections(packageName)).getSubTypesOf(rootClazz));
//    }
    //=========================================================================================
//    public static <T> ArrayList<T> createCollection(final Set<Class<? extends T>> someSet) {
//
//        return new ArrayList<T>(someSet);
//    }
    //==========================================================================
    public static <T> T getPropertySafe(final PropertyGetter propertyGetter, final T default_value) {
        return (T) NullSafe.create((Object) default_value, NullSafe.DONT_THROW_EXCEPTION)
                .execute2result(() -> propertyGetter.getProperty())
                .<T>getObject();
    }

    //==========================================================================
    public static <T> T getPropertySafe(final PropertyGetter propertyGetter) {
        return (T) ServiceFuncs.<T>getPropertySafe(propertyGetter, (T) SysConst.OBJECT_NULL);
    }

    //==========================================================================    
    public static String getStringObjValue(final Object object) {

        return NullSafe.create(SysConst.STRING_NULL, NullSafe.DONT_THROW_EXCEPTION)
                .whenIsNull(() -> ((LocalDate) object).format(SysConst.FORMAT_dd_MM_yyyy))
                .whenIsNull(() -> String.format("%s", object))
                .whenIsNull(() -> String.format("%d", object))
                .whenIsNull(() -> String.format("%f", object))
                .whenIsNull(() -> String.format("%b", object))
                .whenIsNull(() -> SysConst.NOT_DEFINED)
                .<String>getObject();
    }

    //==========================================================================
    public static String getJsonFromObject(final Object object) {
        return NullSafe.create()
                .execute2result(() -> new ObjectMapper().writeValueAsString(object))
                .<String>getObject();
    }

}
