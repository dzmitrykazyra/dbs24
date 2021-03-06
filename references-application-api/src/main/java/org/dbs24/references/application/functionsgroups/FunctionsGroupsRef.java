/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.application.functionsgroups;

import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.exception.references.ReferenceNotFound;
import org.dbs24.references.core.AbstractReference;
import org.dbs24.references.api.LangStrValue;
//import org.dbs24.api.AppConst;
import java.util.ArrayList;
import java.util.Collection;
import org.dbs24.references.api.ReferenceSyncOrder;
import org.dbs24.application.core.nullsafe.NullSafe;

/**
 *
 * @author kazyra_d
 */
@ReferenceSyncOrder(order_num = 1)
public class FunctionsGroupsRef<T extends FunctionGroup> extends AbstractReference<FunctionGroup> {

    //==========================================================================
    public String getFuncGroupNameById( Integer func_group_id) throws ReferenceNotFound {

        return this.getFuncGroupById(func_group_id).getFunction_group_name();
    }

    //==========================================================================
    public T getFuncGroupById( Integer func_group_id) throws ReferenceNotFound {

        return (T) this.<T>findReference(() -> (this.findFunctionGroupById(func_group_id)),
                String.format("Неизвестный код группы функций (FunctionsGroupsRef.function_group_id=%d)", func_group_id));
    }

    //==========================================================================
    private T findFunctionGroupById( Integer func_group_id) {

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
