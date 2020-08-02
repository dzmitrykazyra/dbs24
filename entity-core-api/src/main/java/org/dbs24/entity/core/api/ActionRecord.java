/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.core.api;

/**
 *
 * @author kazyra_d
 */
public interface ActionRecord<ACT extends Action> {

    int getAction_code();

    Class getActionClass();

    ACT getActionInstanse();

    void setActionInstanse(ACT actInstanse);

}
