/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.application.core.service.funcs;

/**
 *
 * @author Козыро Дмитрий
 */
@FunctionalInterface
public interface PropertyGetter<T> {

    T getProperty() throws Throwable;
}