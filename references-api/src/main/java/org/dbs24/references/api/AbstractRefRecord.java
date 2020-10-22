/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.api;

import org.dbs24.application.core.exception.api.InternalAppException;
import java.lang.reflect.Field;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.service.funcs.FilterComparator;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import static org.dbs24.application.core.sysconst.SysConst.*;
import org.dbs24.persistence.api.PersistenceEntity;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public abstract class AbstractRefRecord implements PersistenceEntity {

    public static final Map<Class<? extends AbstractRefRecord>, Collection<? extends AbstractRefRecord>> REF_CACHE
            = ServiceFuncs.getOrCreateMap_Safe(ServiceFuncs.MAP_NULL);

    public final long calcRecordHash() {

        return NullSafe.create()
                .execute2result(() -> {
                    long recHash = 0;
                    Class clazz = AbstractRefRecord.this.getClass();
                    while (!clazz.equals(Object.class)) {
                        for (Field field : clazz.getDeclaredFields()) {
                            field.setAccessible(true);
                            if (NullSafe.notNull(field.get(AbstractRefRecord.this))) {
                                recHash += field.get(AbstractRefRecord.this).hashCode();
                            }
                        }
                        clazz = clazz.getSuperclass();
                    }
                    return recHash;
                }).<Long>getObject();
    }

    //==========================================================================
    static class RefCollectionIsNotFound extends InternalAppException {

        public RefCollectionIsNotFound(final String message) {
            super(message);
        }
    }

    static class ActRefeenceRecordIsNotFound extends InternalAppException {

        public ActRefeenceRecordIsNotFound(final String message) {
            super(message);
        }
    }

    public static <T extends AbstractRefRecord> T getRefeenceRecord(
            final Class<T> clazz,
            final FilterComparator<T> filterComparator) {

        return (T) ServiceFuncs.getMapValue(REF_CACHE, mapEntry -> mapEntry.getKey().equals(clazz))
                .orElseThrow(() -> new RefCollectionIsNotFound(String.format("Reference collection is not found (%s)", clazz.getCanonicalName())))
                .stream()
                .map(x -> (T) x)
                .collect(Collectors.toList())
                .stream()
                .filter(fltr -> filterComparator.getFilter(fltr))
                .findFirst()
                .orElseThrow(() -> new ActRefeenceRecordIsNotFound(String.format("%s is not found (%s) ",
                clazz.getSimpleName(), filterComparator.toString())));

    }
    //==========================================================================
    public static String getTranslatedValue(final LangStrValue langStrValue) {

        return (RUSSIAN_REF_LANG.get()
                ? langStrValue.getRu() : langStrValue.getEn()); // 
    }
}
