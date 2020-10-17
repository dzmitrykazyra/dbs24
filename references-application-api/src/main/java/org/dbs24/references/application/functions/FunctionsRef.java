/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.application.functions;

import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.references.core.AbstractReference;
import org.dbs24.exception.references.ReferenceNotFound;
import org.dbs24.references.api.LangStrValue;
import org.dbs24.references.api.ReferenceConst;
import java.util.Collection;
//import org.dbs24.api.AppConst;
import org.dbs24.references.api.ReferenceSyncOrder;
import org.dbs24.references.api.ReferencesCollection;
//import org.dbs24.services.api.ServiceLocator;
//import org.dbs24.services.FS24JdbcService;
import org.dbs24.application.core.nullsafe.NullSafe;
import java.util.ArrayList;

/**
 *
 * @author kazyra_d
 */
@ReferenceSyncOrder(order_num = 10)
public class FunctionsRef<T extends Function> extends AbstractReference<Function> {

    //==========================================================================
    public String getFuncNameById(final Integer func_id) throws ReferenceNotFound {

        return this.getFunctionById(func_id).getFunction_name();
    }

    //==========================================================================
    public T getFunctionById(final Integer func_id) throws ReferenceNotFound {

        return (T) this.<T>findReference(() -> (this.findFunctionById(func_id)),
                String.format("Неизвестный код функции (FunctionsRef.func_id=%d)", func_id));
    }

    //==========================================================================
    private T findFunctionById(final Integer func_id) {

        return (T) this.findCachedRecords((object) -> ((T) object).getFunction_id().equals(func_id));

    }

    public static <T extends Function> void registerReference() {
//        AbstractReference.<T>store(() -> {
//
//            final Class<T> clazz = (Class<T>) (Function.class);
//
//            return NullSafe.create()
//                    .execute2result(() -> {
//
//                        final Collection<T> refCollection = ServiceFuncs.<T>createCollection();
//
//                        refCollection.add((T) NullSafe.createObject(clazz)
//                                .setFunction_id(AppConst.fnc_1)
//                                .setFunction_code(AppConst.fnc_1_code)
//                                .setFunction_group_id(AppConst.fnc_group_1)
//                                .setFunction_name(AbstractReference.getTranslatedValue(new LangStrValue("n/a", AppConst.fnc_1_name))));
//
//                        return refCollection;
//                    }).<Collection<T>>getObject();
//
//        }, "{call core_insertorupdate_Function(:FID, :FGI, :FCD, :FNM)}",
//                (stmt, record) -> {
//
//                    stmt.setParamByName("FID", record.getFunction_id())
//                            .setParamByName("FGI", record.getFunction_group_id())
//                            .setParamByName("FCD", record.getFunction_code())
//                            .setParamByName("FNM", record.getFunction_name());
//                });
    }
}
