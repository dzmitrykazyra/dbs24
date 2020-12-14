/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.dbs24.entity.core.AbstractAction;
import org.dbs24.entity.core.AbstractActionEntity;

@AllArgsConstructor
@Data
public class Pair {

    private Class<AbstractActionEntity> entClass;
    private Class<AbstractAction> actClass;
}
