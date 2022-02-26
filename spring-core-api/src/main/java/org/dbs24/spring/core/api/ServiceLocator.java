/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.spring.core.api;

import org.dbs24.application.core.service.funcs.ServiceFuncs;
import lombok.Data;
import java.util.Collection;
import lombok.extern.log4j.Log4j2;

@Data
@Log4j2
public final class ServiceLocator {

    private static final Collection<ApplicationBean> BEANS_LIST
            = ServiceFuncs.<ApplicationBean>createCollection();

    public static void registerService(ApplicationBean applicationBean) {
        final String className = applicationBean.getClass().getSimpleName();
        //log.debug("registerService '{}'", className.indexOf("$$") > 0 ? className.substring(0, className.indexOf("$$")) : className);        
        BEANS_LIST.add(applicationBean);
    }

    public static void releaseService(ApplicationBean applicationBean) {
        BEANS_LIST.remove(applicationBean);
    }

    public static <T> T findService(Class<T> clazz) {
        return (T) ServiceFuncs.<ApplicationBean>findCollectionElement(
                BEANS_LIST,
                srv -> srv.getClass().equals(clazz) || clazz.isAssignableFrom(srv.getClass()),
                String.format("%s: Can't find bean/service '%s' ",
                        ServiceLocator.class.getSimpleName(),
                        clazz.getCanonicalName()));
    }

}
