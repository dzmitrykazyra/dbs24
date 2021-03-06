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
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import static org.dbs24.consts.SysConst.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //on class level

public @interface JsfCommand {

    String command() default EMPTY_STRING; // комманда jsf по умолчанию

}
