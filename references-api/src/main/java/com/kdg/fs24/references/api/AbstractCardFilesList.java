/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.references.api;

import com.kdg.fs24.application.core.log.LogService;
import com.kdg.fs24.references.list.ObjectList;
import java.util.List;
import java.util.stream.Collectors;
import com.kdg.fs24.application.core.nullsafe.NullSafe;

/**
 *
 * @author kazyra_d
 */
public abstract class AbstractCardFilesList extends ObjectList {

    public AbstractCardFilesList() {
        super();
    }

    //==========================================================================
    // СЃРѕР·РґР°РЅРёРµ Рё РґРѕР±Р°РІР»РµРЅРёРµ СЃРїСЂР°РІРѕС‡РЅРёРєР° РІ РєРѕР»Р»РµРєС†РёСЋ
    public AbstractCardFile findOrCreateReferenceExt(final Class clazz) {

        AbstractCardFile cardFile = null;

//        for (AbstractCardFile ar : (List<AbstractCardFile>) getObjectList()) {
//
//            // РЅР°СЃР»РµРґРЅРёРє С‚СЂРµР±СѓРµРјРѕРіРѕ РєР»Р°СЃСЃР°
//            if (ar.getClass().isAssignableFrom(clazz)) {
//                cardFile = ar;
//                break;
//            }
//        }
        List<AbstractCardFile> cl = ((List<AbstractCardFile>) getObjectList())
                .stream()
                .unordered()
                .filter(p -> p.getClass().isAssignableFrom(clazz))
                .collect(Collectors.toList());

        // СЃРїСЂР°РІРѕС‡РЅРёРє РЅРµ РЅР°Р№РґРµРЅ
        if (!cl.isEmpty()) {
            cardFile = cl.get(0);
        } else {
            cardFile = (AbstractCardFile) NullSafe.createObject(clazz);
            getObjectList().add(cardFile);
        }

        return cardFile;

    }

}
