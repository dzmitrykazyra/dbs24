/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.core.api;

//import java.util.Collection;

/**
 *
 * @author kazyra_d
 */
public interface Action4Test<T extends Action> {

    void advancedProcessAction(T action);

}
