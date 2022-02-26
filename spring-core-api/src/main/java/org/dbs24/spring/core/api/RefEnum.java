/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.spring.core.api;

import java.util.Arrays;

public interface RefEnum extends AbstractEnum {

    String getValue();

    Integer getCode();

    default RefEnum findByCode(RefEnum[] refEnum, Integer code) {
        return Arrays.stream(refEnum).filter(en -> en.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("%s: Unknown enum code - (%d)", getClass().getCanonicalName(), code)));
    }
}
