/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.spring.condition;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class DisabledCondition extends DefaultCondition {

    @Override
    public boolean matches(ConditionContext cc, AnnotatedTypeMetadata atm) {
        return false;
    }
}
