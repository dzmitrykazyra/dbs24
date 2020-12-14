/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.service.funcs;

import java.util.UUID;

/**
 *
 * @author Козыро Дмитрий
 */
public final class TestFuncs {

    public static final String generateTestString15() {
        return generateTestString(15);
    }
    
    public static final String generateTestString20() {
        return generateTestString(20);
    }

    public static final String generateTestString(int strLength ) {
        return UUID.randomUUID().toString().substring(1, strLength);
    }
    
}
