/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.lias.opers.napi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.dbs24.application.core.service.funcs.CustomCollectionImpl;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.lias.opers.api.DocAttrLinkProperty;
import org.dbs24.application.core.log.LogService;
import org.dbs24.application.core.nullsafe.NullSafe;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author Козыро Дмитрий
 */
@Deprecated
public class NewLiasOperImpl implements OldLiasOper {

    final private Collection<OperAttr> operAttrsCollection
            = ServiceFuncs.<OperAttr>createCollection();
    final private Collection<LiasOperActionExt> advancedActions
            = ServiceFuncs.<LiasOperActionExt>createCollection();

    private static Map<Integer, Class<? extends OperAttr>> linkedFields;

    //--------------------------------------------------------------------------
    @Override
    public <LD extends OperAttr> NewLiasOperImpl addAttr(final LD attrValue) {

//        LogService.LogInfo(attrValue.getClass(), () -> String.format("attrValue: %s = (%s) %s",
//                attrValue.getClass().getInterfaces()[0].getSimpleName(),
//                attrValue.value().getClass().getSimpleName(),
//                attrValue.value()));
        this.getOperAttrsCollection().add(attrValue);
        return this;
    }

    //--------------------------------------------------------------------------
    @Override
    public <LD extends OperAttr> void updateAttr(final LD attrValue) {
        //замена через удаление, используется только при тестировании

        final OperAttr operAttr = ServiceFuncs.<OperAttr>getCollectionElement(
                this.getOperAttrsCollection(),
                attr -> attr.getClass().getInterfaces()[0].equals(attrValue.getClass().getInterfaces()[0]),
                String.format("Attribute is not found (%s)", attrValue.getClass().getCanonicalName()));

        synchronized (this.getOperAttrsCollection()) {

            this.getOperAttrsCollection().remove(operAttr);
            this.getOperAttrsCollection().add(attrValue);
        }
    }

    //--------------------------------------------------------------------------
    @Override
    public <V> V attrValue(final Class<? extends OperAttr> clazz) {

        final OperAttr operAttr = ServiceFuncs.<OperAttr>getCollectionElement_silent(
                this.getOperAttrsCollection(),
                attr -> attr.getClass().getInterfaces()[0].getName().equals(clazz.getName()));
        final V v;

        if (null != operAttr) {
            v = (V) operAttr.value();
        } else {
            v = null;
        }

        return v;
    }

    //--------------------------------------------------------------------------
    @Override
    public Collection<OperAttr> getOperAttrsCollection() {
        return this.operAttrsCollection;
    }

    //--------------------------------------------------------------------------
    @Override
    public void printOperAttrsCollection() {

        //анонимный класс для принтования списка остатков
        final CustomCollectionImpl customCollection = NullSafe.createObject(CustomCollectionImpl.class,String.format("LiasOpers attributes (%d): \n",
                NewLiasOperImpl.this.operAttrsCollection.size()));

        this.getOperAttrsCollection()
                .stream()
                .sorted((r1, r2) -> {
                    // сортировка по номеру операции в списке
                    return r1.getClass()
                            .getInterfaces()[0]
                            .getSimpleName()
                            .compareTo(r2.getClass()
                                    .getInterfaces()[0]
                                    .getSimpleName());
                })
                .forEach(attr -> {
                    customCollection.addCustomRecord(() -> String.format("%30s: %s\n",
                            attr.getClass().getInterfaces()[0].getSimpleName(),
                            ServiceFuncs.getStringObjValue(attr.value())));
                });

        LogService.LogInfo(this.getClass(), () -> customCollection.getRecord());
    }

    //--------------------------------------------------------------------------
    @JsonIgnore
    public Map<Integer, Class<? extends OperAttr>> getLinkedFields() {
        synchronized (NewLiasOperImpl.class) {

            NullSafe.create(NewLiasOperImpl.linkedFields)
                    .whenIsNull(() -> {

                        this.linkedFields = ServiceFuncs.<Integer, Class<? extends OperAttr>>getOrCreateMap(ServiceFuncs.MAP_NULL);

                        this.operAttrsCollection
                                .stream()
                                .unordered()
                                .filter(operAttr -> operAttr.getClass().isAnnotationPresent(DocAttrLinkProperty.class))
                                .forEach((operAttr) -> {
//                                    final Field field = operAttr.getClass().getField("attrValue");
//                                    field.setAccessible(BOOLEAN_TRUE);
                                    final DocAttrLinkProperty dal = ((DocAttrLinkProperty) operAttr.getClass().getAnnotation(DocAttrLinkProperty.class));
                                    NewLiasOperImpl.linkedFields.put(dal.docAttr(), operAttr.getClass());
                                });

//                        Arrays.stream(this.getClass().getDeclaredFields())
//                                .unordered()
//                                .filter(field -> field.isAnnotationPresent(DocAttrLinkProperty.class))
//                                .forEach((field) -> {
//                                    field.setAccessible(BOOLEAN_TRUE);
//                                    final DocAttrLinkProperty dal = ((DocAttrLinkProperty) field.getAnnotation(DocAttrLinkProperty.class));
//                                    NewLiasOperImpl.linkedFields.put(dal.docAttr(), field);
//                                });
                        return NewLiasOperImpl.linkedFields;
                    });
        }
        return NewLiasOperImpl.linkedFields;
    }

    //==========================================================================
    @Override
    public Collection<LiasOperActionExt> getAdvancedActions() {
        return this.advancedActions;
    }

    //==========================================================================
    @Override
    public <V extends LiasOperActionExt> V getOperAction(final Class<V> clazz) {
        return (V) ServiceFuncs.<V>getCollectionElement_silent(
                (Collection<V>) this.getAdvancedActions(),
                action -> action.getClass().getInterfaces()[0].equals(clazz));
    }

    //--------------------------------------------------------------------------
    //@Override
    public <LOA extends LiasOperActionExt> NewLiasOperImpl addExtAction(final LOA extAction) {

        this.getAdvancedActions().add(extAction);
        return this;
    }

}
