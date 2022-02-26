/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.service.funcs;

import java.util.Map;

@FunctionalInterface
public interface MapProcessor<K, V> {

    void processMap(Map<K, V> map);

}
