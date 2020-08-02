/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.service;

import com.kdg.fs24.entity.core.AbstractAction;

/**
 *
 * @author Козыро Дмитрий
 */
@FunctionalInterface
public interface ActionInitializer<T extends AbstractAction> {
    void initialize(T action);
}
