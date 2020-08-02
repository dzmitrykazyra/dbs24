/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.service.funcs;

import org.dbs24.application.core.nullsafe.NullSafe;
import java.lang.reflect.Modifier;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Козыро Дмитрий
 */
public final class ReflectionFuncs {
    
    public static <T extends Class> Collection<T> createPkgClassesCollection(final String packageName, final T rootClazz) {
        
        return new ArrayList<>((Collection<T>) (new Reflections(packageName)).getSubTypesOf(rootClazz));
    }

    //==========================================================================
    public static <T> Collection<T> createPkgClassesCollectionExt(final String packageName, final Class<T> rootClazz) {
        
        return new ArrayList<>((Collection<T>) (new Reflections(packageName)).getSubTypesOf(rootClazz));
    }

    //==========================================================================
    public static <T extends Class, A extends Class> void processPkgClassesCollection(
            final String packageName,
            final T rootClass,
            final A annClass,
            final ServClassProcessor servClassProcessor) {

        // значения для справочника берутся из аннотаций классов
        ReflectionFuncs.createPkgClassesCollection(packageName, rootClass)
                .stream()
                .filter(p -> !p.isInterface())
                .filter(p -> !Modifier.isAbstract(p.getModifiers()))
                .filter(p -> NullSafe.isNull(annClass) || AnnotationFuncs.isAnnotated(p, annClass))
                .forEach((refClazz) -> {
                    servClassProcessor.processClass((Class) refClazz);
                });        
    }
}
