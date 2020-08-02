/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.attr.template;

/**
 *
 * @author Козыро Дмитрий
 */
import org.dbs24.application.core.service.funcs.AnnotationFuncs;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.entity.api.EntityReferenceConst;

import org.dbs24.references.core.AbstractReference;
import org.dbs24.application.core.nullsafe.NullSafe;
import java.util.Collection;
import java.util.stream.Collectors;
import org.dbs24.application.core.service.funcs.ReflectionFuncs;

public class EntAttrTemplatesRef extends AbstractReference<EntAttrTemplate> {
    //--------------------------------------------------------------------------

    public static <T extends EntAttrTemplate> void registerReference() {
        // сохраняем в бд записи справочника видов обязательств

//        AbstractReference.<T>store(() -> {
//
//            return NullSafe.create(EntityReferenceConst.ENT_ATTR_TEMPLATE_CLASS.getCanonicalName())
//                    .execute2result(() -> {
//
//                        final Collection<T> refCollection = ServiceFuncs.<T>createCollection();
//
//                        ReflectionFuncs.createPkgClassesCollection(EntityReferenceConst.ENTITY_ATTR_TEMPLATE_CLASSES_PACKAGE,
//                                EntityReferenceConst.ENT_LIST_ATTR_TEMPLATE_CLASS)
//                                .stream()
//                                .unordered()
//                                .filter(p -> AnnotationFuncs.isAnnotated(p, EntityReferenceConst.ENT_ATTR_TEMPLATE_ID_ANN))
//                                .collect(Collectors.toList())
//                                .forEach((c_clazz) -> {
//
//                                    // получили id аннотации
//                                    final EntityAttrTemplateId eai = AnnotationFuncs
//                                            .<EntityAttrTemplateId>getAnnotation(c_clazz, EntityReferenceConst.ENT_ATTR_TEMPLATE_ID_ANN);
//                                    final Integer template_id = eai.id();
//
//                                    // получили список повторяющихся аннотаций @EntityAttrRow
//                                    final Collection<EntityAttrRow> col = AnnotationFuncs
//                                            .<EntityAttrRow>getRepeatedAnnotation(c_clazz, EntityReferenceConst.ENT_ATTR_ROW_ANN);
//
//                                    col.stream()
//                                            .unordered()
//                                            .forEach((template_attr_id) -> {
//
//                                                NullSafe.create(EntityReferenceConst.ENT_ATTR_ROW_ANN.getCanonicalName())
//                                                        .execute(() -> {
//
//                                                            refCollection.add((T) NullSafe.createObject(EntityReferenceConst.ENT_ATTR_TEMPLATE_CLASS)
//                                                                    .setAttr_id(template_attr_id.attr_id())
//                                                                    .setAttr_template_id(eai.id()));
//                                                        });
//                                            });
//                                });
//
//                        return refCollection;
//                    }).<Collection<T>>getObject();
//
//        }, "{call core_insertorupdate_EntAttrTemplatesRef(:EATI, :EAI)}",
//                (stmt, record) -> {
//                    stmt.setParamByName("EAI", record.getAttr_id())
//                            .setParamByName("EATI", record.getAttr_template_id());
//                });
    }
}
