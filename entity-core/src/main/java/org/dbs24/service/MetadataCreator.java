/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import java.util.Map;

@FunctionalInterface
public interface MetadataCreator {

    void fillMataData(Map<String, Object> metadata);
}
