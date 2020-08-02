/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.lias.opers.napi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author kazyra_d
 */
@Repeatable(LiasOper2Tariff.LiasOperSetup.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LiasOper2Tariff {

    int tariff_serv_id();
    
    int debt_state_id();

    int fin_oper_code();

    int lias_action_type();

    int lias_kind_id();

    int lias_type_id();

    int base_asset_type_id();

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface LiasOperSetup {

        LiasOper2Tariff[] value();
    }

}
