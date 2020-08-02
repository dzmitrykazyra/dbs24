/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.attr;

//import com.kdg.fs24.entity.core.attr.PropertyGetter;
import com.kdg.fs24.attribute.api.Attribute;

/**
 *
 * @author Козыро Дмитрий
 */
@FunctionalInterface
public interface EntityAttribute<T> extends Attribute<T> {

    @Override
    T value();
}
