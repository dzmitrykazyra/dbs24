/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.service.funcs;

import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.stmt.StmtProcessor;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.dbs24.consts.SysConst.STRING_NULL_VALUE;

/**
 * @author Козыро Дмитрий
 */
public final class GenericFuncs {

    @SuppressWarnings("unchecked")
    public static Class<?> getTypeParameterClass(Class<?> parametrizedType) {
        return (Class<?>) GenericFuncs.getTypeParameterClass(parametrizedType, 0);
    }

    //==========================================================================
    @SuppressWarnings("unchecked")
    public static Class<?> getTypeParameterClass(Class<?> parametrizedType, int paramNum) {

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
                //.catchException(e -> log.error(e.getMessage()))
                .throwException()
                .<Class<?>>getObject();
    }

    //==========================================================================
    @SuppressWarnings("unchecked")
    public static Class<?> getTypeParameterObject(Class<?> parametrizedType, int paramNum) {
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
    public static Class getElementClass(Collection collection) {
        final Class clazz;

        if (collection.isEmpty()) {
            clazz = null;
        } else {

            clazz = (Class) collection.iterator().next().getClass();

        }

        return clazz;
    }

    //==========================================================================
    public static <T> String getObjectsDiffs(T object1, T object2) {

        StmtProcessor.assertNotNull(Object.class, object1, "getObjectsDiffs: first object is null or not defined");
        StmtProcessor.assertNotNull(Object.class, object2, "getObjectsDiffs: second object is null or not defined");

        final StringBuilder sb = new StringBuilder(1024);
        sb.append(String.format("%s: ", object1.getClass().getCanonicalName()));
        sb.append(getObjectsDiffsInternal.apply(object1, object2));
        return sb.toString();

    }

    //==========================================================================
    private static Function<Class<?>, Field[]> getFields = clazz -> {
        return clazz.getDeclaredFields();
    };

    private static Function<Object, String> object2str = value -> {

        final StringBuilder result = new StringBuilder(1024);

        StmtProcessor.ifNotNull(value, notNullValue -> {

            final String className = notNullValue.getClass().getSimpleName();

            switch (className) {

                case "Integer", "Long", "String", "LocalDateTime", "BigDecimal", "Boolean":
                    result.append(notNullValue.toString());
                    break;
                default:
                    result.append(String.format("unsupported type: %s ", className));
                    break;
            }
        });

        StmtProcessor.ifTrue(result.isEmpty(), () -> result.append(STRING_NULL_VALUE));

        return result.toString();

    };

    private static BiFunction<Object, Object, String> getObjectsDiffsInternal = (object1, object2) -> {

        final StringBuilder sb = new StringBuilder(1024);
        final Class<?> cmpClass = object1.getClass();
        Class<?> currentClass = cmpClass;

        do {

            Stream.of(getFields.apply(currentClass))
                    .forEach(fld -> StmtProcessor.execute(() -> {

                        fld.setAccessible(true);

                        final String strValue1 = object2str.apply(fld.get(object1));
                        final String strValue2 = object2str.apply(fld.get(object2));

                        //log.debug("{}:{}", strValue1, strValue2);

                        fld.setAccessible(false);

                        StmtProcessor.ifTrue(!(strValue1.equals(strValue2)), () -> sb.append(String.format(" %s: '%s' -> '%s'; ", fld.getName(), strValue1, strValue2)));

                    }));

            currentClass = currentClass.getSuperclass();

        } while (!currentClass.equals(Object.class));

        if (sb.isEmpty()) {
            sb.append(" - no differences");
        }

        return sb.toString();

    };

}
