/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import static org.dbs24.application.core.sysconst.SysConst.*;

/**
 *
 * @author Козыро Дмитрий
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //on class level
public @interface RefClass {
   Class<? extends ReferenceRec> reference_class() default ReferenceRec.class;
   String db_table_name() default EMPTY_STRING;
}
