/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.lias.opers.napi;

import java.util.Collection;
import java.util.Map;

/**
 *
 * @author Козыро Дмитрий
 */
@Deprecated
public interface OldLiasOper {

    //<T extends OperAttr, V> NewLiasOper addAttr(final V attrValue);
    //<LD extends LocalDateOperAttr> NewLiasOper addAttr(LD attrValue);
    <LD extends OperAttr> OldLiasOper addAttr(LD attrValue);

    <V> V attrValue(final Class<? extends OperAttr> clazz);

//    VO attrValue2(LD operAttr);
    <LD extends OperAttr> void updateAttr(LD attrValue);

    Collection<OperAttr> getOperAttrsCollection();

    Map<Integer, Class<? extends OperAttr>> getLinkedFields();

    void printOperAttrsCollection();

    Collection<LiasOperActionExt> getAdvancedActions();

    <V extends LiasOperActionExt> V getOperAction(Class<V> clazz);

}
