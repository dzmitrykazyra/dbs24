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
    
    public static final String generateTestString20() {
        return UUID.randomUUID().toString().substring(1, 20);
    }
    
}
