/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.api;

import java.util.Map;

/**
 *
 * @author kazyra_d
 */
@FunctionalInterface
public interface RefMultiLangValues {
    void getMultiLangValues(Map<Integer, LangStrValue> langValues);
}
