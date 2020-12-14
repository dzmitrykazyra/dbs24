/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.service.funcs;

import org.dbs24.application.core.log.LogService;
import org.dbs24.application.core.nullsafe.NullSafe;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 *
 * @author Козыро Дмитрий
 */
public final class GenericFuncs {

    @SuppressWarnings("unchecked")
    public static Class<?> getTypeParameterClass( Class<?> parametrizedType) {
        return (Class<?>) GenericFuncs.getTypeParameterClass(parametrizedType, 0);
    }

    //==========================================================================
    @SuppressWarnings("unchecked")
    public static Class<?> getTypeParameterClass( Class<?> parametrizedType, int paramNum) {

//        LogService.LogInfo(parametrizedType, () -> String.format("%s: process '%s' type (paramNum - %d )",
//                LogService.getCurrentObjProcName(GenericFuncs.class),
//                parametrizedType.getCanonicalName(),
//                paramNum));

        return NullSafe.create()
                .execute2result(() -> {

                    final Type type = parametrizedType.getGenericSuperclass();
                    final ParameterizedType paramType = (ParameterizedType) type;
                    return paramType.getActualTypeArguments()[paramNum];
                })
                .catchException(e -> LogService.LogErr(parametrizedType,
                () -> String.format("%s: Can't process '%s' type (paramNum - %d )\n %s",
                        LogService.getCurrentObjProcName(GenericFuncs.class),
                        parametrizedType.getCanonicalName(),
                        paramNum,
                        NullSafe.getErrorMessage(e))))
                .throwException()
                .<Class<?>>getObject();
    }

    //==========================================================================
    @SuppressWarnings("unchecked")
    public static Class<?> getTypeParameterObject( Class<?> parametrizedType, int paramNum) {
//        return (Class<?>) (((ParameterizedType) parametrizedType
//                .getGenericSuperclass())
//                .getActualTypeArguments()[paramNum]);
//        LogService.LogInfo(parametrizedType, String.format("%s: process '%s' type (paramNum - %d )",
//                LogService.getCurrentObjProcName(GenericFuncs.class),
//                parametrizedType.getCanonicalName(),
//                paramNum));
        return NullSafe.create()
                .execute2result(() -> {
                    final Type type = parametrizedType.getGenericSuperclass();
                    final ParameterizedType paramType = (ParameterizedType) type;

                    return paramType.getActualTypeArguments()[paramNum];

                }).throwException()
                .<Class<?>>getObject();
    }

    //==========================================================================
    public static Class getElementClass( Collection collection) {
        final Class clazz;

        if (collection.isEmpty()) {
            clazz = null;
        } else {

            clazz = (Class) collection.iterator().next().getClass();

        }

        return clazz;
    }
}
