/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.reactor;

import org.dbs24.entity.core.AbstractActionEntity;

@FunctionalInterface
public interface RunnableAction {
    void run(AbstractActionEntity aae);
}
