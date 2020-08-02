/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.lias.opers.napi;

/**
 *
 * @author Козыро Дмитрий
 */
public interface GenericOperAttr<T> extends OperAttr<T> {

    @Override
    T value();
}
