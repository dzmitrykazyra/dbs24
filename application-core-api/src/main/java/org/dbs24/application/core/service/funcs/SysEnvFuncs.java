/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.service.funcs;

import org.dbs24.application.core.nullsafe.NullSafe;

/**
 *
 * @author Козыро Дмитрий
 */
public final class SysEnvFuncs {

    //==========================================================================
    public static final String getSysEnvFuns() {
        final CustomCollectionImpl customCollection = NullSafe.createObject(CustomCollectionImpl.class, "System env \n");

        System.getenv()
                .keySet()
                .stream()
                .unordered()
                .forEach(obj -> {

                    customCollection.addCustomRecord(() -> String.format("%40s = '%s'\n",
                            obj,
                            //ServiceFuncs.getStringObjValue(paramsMap.get(obj)), NullSafe.create(paramsMap.get(obj))
                            ServiceFuncs.getStringObjValue(System.getenv().get(obj))));

                });

        return customCollection.getRecord();

    }
    //==========================================================================

    public static final String getSysProperties() {
        //======================================================================
        final CustomCollectionImpl customCollection1 = NullSafe.createObject(CustomCollectionImpl.class, "sysProperties \n");

        System.getProperties()
                .forEach((k, v) -> {

                    customCollection1.addCustomRecord(() -> String.format("%40s = '%s'\n",
                            (String) k,
                            //ServiceFuncs.getStringObjValue(paramsMap.get(obj)), NullSafe.create(paramsMap.get(obj))
                            ServiceFuncs.getStringObjValue((Object) v)));
                });

        return customCollection1.getRecord();
    }
}
