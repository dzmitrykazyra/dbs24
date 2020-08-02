/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.application.core.service.funcs;

import com.kdg.fs24.application.core.nullsafe.NullSafe;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;

public final class AnnotationFuncs {

    public static <T extends Object> Boolean isAnnotated(final Class checkedClass, final Class<T> requiredAnnotation) {

        return NullSafe.notNull(checkedClass.<T>getAnnotation(requiredAnnotation));

    }

    //==========================================================================
    public static <T extends Object> Boolean isAnnotated(final Class checkedClass, final Class<T> required, final Boolean checkParentClass) {

        Boolean result = Boolean.FALSE;
        Class clAss = checkedClass;

        while (NullSafe.notNull(clAss)) {

            result = AnnotationFuncs.<T>isAnnotated(clAss, required);

            if (result) {
                break;
            }

            clAss = clAss.getSuperclass();
        }
        return result;
    }

    //==========================================================================
    public static <T extends Object> T getAnnotation(final Class checkedClass, final Class<T> requiredAnnotation) {

        //result = (Boolean) annViewAction.isView();
        return (T) checkedClass.<T>getAnnotation(requiredAnnotation);
    }

    //==========================================================================
    public static <T extends Object> T getAnnotation(final Class checkedClass, final Class<T> requiredAnnotation, final Boolean checkParentClass) {

        Annotation annotation = null;
        //result = (Boolean) annViewAction.isView();
        Class clAss = checkedClass;

        while (NullSafe.notNull(clAss)) {

            annotation = clAss.<T>getAnnotation(requiredAnnotation);

            if (NullSafe.notNull(annotation)) {
                break;
            }

            clAss = clAss.getSuperclass();
        }

        return (T) annotation;
    }

    //==========================================================================
    public static <T extends Object> Collection<T> getRepeatedAnnotation(final Class checkedClass, final Class<T> requiredAnnotation) {
        //return AnnotationFuncs.<T>getRepeatedAnnotation(checkedClass, requiredAnnotation, Boolean.false);
        return AnnotationFuncs.<T>getRepeatedAnnotation(checkedClass, requiredAnnotation, Boolean.FALSE);
    }

    //==========================================================================
    public static <T extends Object> Collection<T> getRepeatedAnnotation(
            final Class checkedClass,
            final Class<T> requiredAnnotation,
            final Boolean checkParentClass) {

        Annotation[] annotation = null;
        final Collection<T> annCollection = ServiceFuncs.<T>createCollection();
        //result = (Boolean) annViewAction.isView();
        Class clAss = checkedClass;

        while (NullSafe.notNull(clAss)) {

            annotation = clAss.getAnnotationsByType(requiredAnnotation);

            if (NullSafe.notNull(annotation)) {

                Arrays.stream(annotation)
                        .unordered()
                        .forEach(repAnn -> annCollection.add((T) repAnn));

                break;
            }

            clAss = clAss.getSuperclass();
        }

        return annCollection;
    }
}
