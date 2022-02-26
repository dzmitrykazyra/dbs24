/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.fields.desc;

import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.service.funcs.ServiceFuncs;

import java.util.Collection;
import java.util.Map;

import static org.dbs24.consts.SysConst.*;

/**
 *
 * @author kazyra_d
 */
public class AppFieldsMap {

    private final Map<Integer, AppFieldsCaptions> fieldsList
            = ServiceFuncs.<Integer, AppFieldsCaptions>getOrCreateMap(ServiceFuncs.MAP_NULL);

    //==========================================================================
    private AppFieldsCaptions getAppFieldsCaptions( Integer app_id, Long user_id, Collection<FieldDescription> lst) {

        return NullSafe.create(ServiceFuncs.
                <Integer, AppFieldsCaptions>getMapValue_silent(this.fieldsList,
                        p -> p.getKey().equals(app_id)))
                .whenIsNull(() -> {

                    final AppFieldsCaptions afc = NullSafe.createObject(AppFieldsCaptions.class);

                    afc.loadReference(FORCE_RELOAD, Long.valueOf(app_id));

                    // настройки конкретного пользователя
                    final Collection<AppFieldCaption> lstFltr
                            = ServiceFuncs.<AppFieldCaption>filterCollection_Silent(
                                    afc.getCardFiles(),
                                    attr -> attr.getUser_id().equals(user_id));

                    final Collection<AppFieldCaption> lstFltr2;

                    // коллекция пустая
                    if (lstFltr.isEmpty()) {
                        lstFltr2 = ServiceFuncs.<AppFieldCaption>filterCollection_Silent(
                                afc.getCardFiles(),
                                attr -> attr.getUser_id().equals(SERVICE_USER_ID));
                    } else {
                        lstFltr2 = lstFltr;
                    }

                    // добавляем в список дефолтное значение из аннотации, если оно не было загружено из БД
                    //for (FieldDescription fldDesc : lst) {
                    lst
                            .stream()
                            .forEach(fldDesc -> {

                                NullSafe.create(ServiceFuncs.<AppFieldCaption>getCollectionElement_silent(
                                        lstFltr2,
                                        attr -> attr.getField_name().equals(fldDesc.getField_name())))
                                        .whenIsNull(() -> {
                                            final AppFieldCaption newField = NullSafe.createObject(AppFieldCaption.class);

                                            newField.setApp_id(app_id);
                                            newField.setUser_id(SERVICE_USER_ID);
                                            // взять из аннотации
                                            newField.setField_name(fldDesc.getField_name());
                                            newField.setField_caption(fldDesc.getField_caption());
                                            newField.setField_tooltip(fldDesc.getField_tooltip());

                                            afc.getCardFiles().add(newField);
                                        });
                            });

                    getFieldsList().put(app_id, afc);

                    return afc;

                })
                .<AppFieldsCaptions>getObject();
    }

    //==========================================================================
    public String getFieldCaption( Integer app_id, Long user_id, String attrName, Collection<FieldDescription> lst) {

        return this.getAppFieldsCaptions(app_id, user_id, lst).getAttrCaption(user_id, attrName);
    }

    //==========================================================================
    public String getFieldToolTip( Integer app_id, Long user_id, String attrName, Collection<FieldDescription> lst) {
        return this.getAppFieldsCaptions(app_id, user_id, lst).getAttrToolTip(user_id, attrName);
    }

    //==========================================================================
    public Collection<FieldDescription> getFieldDescription( Integer app_id, Long user_id) {

        return NullSafe.create(OBJECT_NULL)
                .execute2result(() -> {

                    final Collection<FieldDescription> col = ServiceFuncs.<FieldDescription>createCollection();

                    final AppFieldsCaptions afc = ServiceFuncs.
                            <Integer, AppFieldsCaptions>getMapValue_silent(getFieldsList(),
                                    p -> p.getKey().equals(app_id));

                    // настройки конкретного пользователя
                    final Collection<AppFieldCaption> lstFltr
                            = ServiceFuncs.<AppFieldCaption>filterCollection_Silent(
                                    afc.getCardFiles(),
                                    attr -> attr.getUser_id().equals(user_id));

                    final Collection<AppFieldCaption> lstFltr2;

                    // коллекция пустая
                    if (lstFltr.isEmpty()) {
                        lstFltr2 = ServiceFuncs.<AppFieldCaption>filterCollection_Silent(
                                afc.getCardFiles(),
                                attr -> attr.getUser_id().equals(SERVICE_USER_ID));
                    } else {
                        lstFltr2 = lstFltr;
                    }

                    lstFltr2
                            .stream()
                            .forEach(fldDesc -> {
                                final String fldName = fldDesc.getField_name();
                                final String fldCaption = fldDesc.getField_caption();
                                final String fldTT = fldDesc.getField_tooltip();

                                col.add(new FieldDescription() {

                                    @Override
                                    public String getField_name() {
                                        return fldName;
                                    }

                                    @Override
                                    public String getField_caption() {
                                        return fldCaption;
                                    }

                                    @Override
                                    public String getField_tooltip() {
                                        return fldTT;
                                    }
                                });
                            });

                    return col;

                })
                .<Collection<FieldDescription>>getObject();

//        AppFieldsCaptions afc = null;
//
//        // нашли нужную коллекцию
//        for (Map.Entry<Integer, AppFieldsCaptions> mafc : getFieldsList().entrySet()) {
//            if (mafc.getKey().equals(app_id)) {
//                afc = mafc.getValue();
//            }
//        }
//
//        // настройки конкретного пользователя
//        List<AppFieldCaption> lstFltr
//                = ((List<AppFieldCaption>) afc.getCardFiles()).stream().filter(p -> p.getUser_id().equals(user_id)).collect(Collectors.toList());
//
//        // настройки конкретного пользователя не найдены
//        // пользуем дефолтные настройки
//        if (lstFltr.isEmpty()) {
//            lstFltr = ((List<AppFieldCaption>) afc.getCardFiles()).stream().filter(p -> p.getUser_id().equals(SERVICE_USER_ID)).collect(Collectors.toList());
//        }
//
//        List<FieldDescription> answer = (List<FieldDescription>) ServiceFuncs.<FieldDescription>createCollection();
//
//        // переконвертировали в List<FieldDescription>
//        for (AppFieldCaption a : lstFltr) {
//
//            String fldName = a.getField_name();
//            String fldCaption = a.getField_caption();
//            String fldTT = a.getField_tooltip();
//
//            answer.add(new FieldDescription() {
//
//                @Override
//                public String getField_name() {
//                    return fldName;
//                }
//
//                @Override
//                public String getField_caption() {
//                    return fldCaption;
//                }
//
//                @Override
//                public String getField_tooltip() {
//                    return fldTT;
//                }
//            });
//        }
//
//        return answer;
    }

    public Map<Integer, AppFieldsCaptions> getFieldsList() {

        return this.fieldsList;
    }

}
