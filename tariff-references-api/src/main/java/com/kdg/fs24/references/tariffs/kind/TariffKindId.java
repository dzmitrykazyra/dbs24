/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.references.tariffs.kind;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.kdg.fs24.references.tariffs.serv.TariffServ;

/**
 *
 * @author Козыро Дмитрий
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //on class level
public @interface TariffKindId {

    //Class<? extends TariffServ> tariff_serv_class();
    int tariff_serv_id();
    
    int tariff_kind_id();
    
    int tariff_scheme_id();

    String tariff_kind_name();
    
    String en_kind_name() default "n/a";
}
