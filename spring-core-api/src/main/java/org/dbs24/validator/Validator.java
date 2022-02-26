/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.validator;

import java.util.Collection;
import org.dbs24.application.core.service.funcs.ServiceFuncs;

public interface Validator<T> {

//    public default Collection<ErrorInfo> validate(T t) {
//        return ServiceFuncs.<ErrorInfo>createCollection();
//    }

    public default Collection<WarnInfo> warn(T t) {
        return ServiceFuncs.<WarnInfo>createCollection();
    }
    
    public default Collection<ErrorInfo> buildErrorCollection() {
        return ServiceFuncs.<ErrorInfo>createCollection();
    }
}
