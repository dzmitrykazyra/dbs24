/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.references.application.functionsgroups;

import com.kdg.fs24.application.core.service.funcs.ServiceFuncs;
import com.kdg.fs24.exception.references.ReferenceNotFound;
import com.kdg.fs24.references.core.AbstractReference;
import com.kdg.fs24.references.api.LangStrValue;
//import com.kdg.fs24.sysconst.api.AppConst;
import java.util.ArrayList;
import java.util.Collection;
import com.kdg.fs24.references.api.ReferenceSyncOrder;
import com.kdg.fs24.application.core.nullsafe.NullSafe;

/**
 *
 * @author kazyra_d
 */
@ReferenceSyncOrder(order_num = 1)
public class FunctionsGroupsRef<T extends FunctionGroup> extends AbstractReference<FunctionGroup> {

    //==========================================================================
    public String getFuncGroupNameById(final Integer func_group_id) throws ReferenceNotFound {

        return this.getFuncGroupById(func_group_id).getFunction_group_name();
    }

    //==========================================================================
    public T getFuncGroupById(final Integer func_group_id) throws ReferenceNotFound {

        return (T) this.<T>findReference(() -> (this.findFunctionGroupById(func_group_id)),
                String.format("Неизвестный код группы функций (FunctionsGroupsRef.function_group_id=%d)", func_group_id));
    }

    //==========================================================================
    private T findFunctionGroupById(final Integer func_group_id) {

        return (T) this.findCachedRecords((object) -> ((T) object).getFunction_group_id().equals(func_group_id));

    }

    //--------------------------------------------------------------------------
    public static <T extends FunctionGroup> void registerReference() {
//        AbstractReference.<T>store(() -> {
//
//            final Class<T> clazz = (Class<T>) (FunctionGroup.class);
//
//            return NullSafe.create()
//                    .execute2result(() -> {
//
//                        final Collection<T> refCollection = ServiceFuncs.<T>createCollection();
//
//                        refCollection.add((T) NullSafe.createObject(clazz)
//                                .setFunction_group_id(AppConst.fnc_group_1)
//                                .setFunction_group_code(AppConst.fnc_group_1_code)
//                                .setFunction_group_name(AbstractReference.getTranslatedValue(new LangStrValue("func group #1", "Группа функций №1"))));
//
//                        return refCollection;
//                    }).<Collection<T>>getObject();
//
//        }, "{call core_insertorupdate_FunctionsGroup(:FGD, :FCD, :FNM)}",
//                (stmt, record) -> {
//
//                    stmt.setParamByName("FGD", record.getFunction_group_id())
//                            .setParamByName("FCD", record.getFunction_group_code())
//                            .setParamByName("FNM", record.getFunction_group_name());
//
//                });
    }
}
