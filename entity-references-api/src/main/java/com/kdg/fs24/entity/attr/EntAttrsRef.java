/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.attr;

import com.kdg.fs24.application.core.service.funcs.AnnotationFuncs;
import com.kdg.fs24.application.core.service.funcs.ServiceFuncs;
import com.kdg.fs24.exception.references.ReferenceNotFound;
import com.kdg.fs24.references.api.LangStrValue;
import com.kdg.fs24.references.core.AbstractReference;
import com.kdg.fs24.application.core.nullsafe.NullSafe;
import com.kdg.fs24.entity.api.EntityReferenceConst;
import com.kdg.fs24.references.api.ReferenceSyncOrder;
import com.kdg.fs24.application.core.service.funcs.ReflectionFuncs;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 *
 * @author kazyra_d
 */
@ReferenceSyncOrder(order_num = 1)
public class EntAttrsRef<T extends EntAttr> extends AbstractReference<EntAttr> {

    //==========================================================================
    public T getEntityAttrById(final Integer attr_id) throws ReferenceNotFound {

        return (T) this.<T>findReference(() -> (this.findEntityAttrById(attr_id)),
                String.format("Неизвестный атрибут энтити (EntAttrRef.attr_id=%d)", attr_id));
    }

    //==========================================================================
    private T findEntityAttrById(final Integer attr_id) {

        return (T) this.findCachedRecords((object) -> ((T) object).getAttr_id().equals(attr_id));
    }

    //--------------------------------------------------------------------------
    public static <T extends EntAttr> void registerReference() {
        // сохраняем в бд записи справочника видов обязательств

//        AbstractReference.<T>store(() -> {
//
//            return NullSafe.create(EntityReferenceConst.ENT_ATTR_CLASS.getCanonicalName())
//                    .execute2result(() -> {
//
//                        final Collection<T> refCollection = ServiceFuncs.<T>createCollection();
//
//                        ReflectionFuncs.createPkgClassesCollection(EntityReferenceConst.ENTITY_ATTRS_CLASSES_PACKAGE, EntityReferenceConst.ENT_ATTRIBUTE_CLASS)
//                                .stream()
//                                .unordered()
//                                .filter(p -> AnnotationFuncs.isAnnotated(p, EntityReferenceConst.ENT_ATTR_ID_ANN))
//                                .collect(Collectors.toList())
//                                .forEach((c_clazz) -> {
//
//                                    final EntityAttrId eai = AnnotationFuncs.<EntityAttrId>getAnnotation(c_clazz, EntityReferenceConst.ENT_ATTR_ID_ANN);
//
//                                    NullSafe.create(EntityReferenceConst.ENT_ATTR_CLASS.getCanonicalName())
//                                            .execute(() -> {
//
//                                                refCollection.add((T) NullSafe.createObject(EntityReferenceConst.ENT_ATTR_CLASS)
//                                                        .setAttr_id(eai.id())
//                                                        .setAttr_code(c_clazz.getSimpleName())
//                                                        .setAttr_name(AbstractReference.getTranslatedValue(
//                                                                new LangStrValue(eai.attrName_en(), eai.attrName()))));
//                                            });
//                                });
//
//                        return refCollection;
//                    }).<Collection<T>>getObject();
//
//        }, "{call core_insertorupdate_EntAttrRef(:EAI, :EAC, :EAN)}",
//                (stmt, record) -> {
//                    stmt.setParamByName("EAI", record.getAttr_id())
//                            .setParamByName("EAC", record.getAttr_code())
//                            .setParamByName("EAN", record.getAttr_name());
//                });
    }
}
