/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.core.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author kazyra_d
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) //on field level
public @interface ViewField {

    String field_caption() default "field_caption";

    String field_tooltip() default "field_tooltip";
}
