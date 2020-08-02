/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.lias.opers.api;

/**
 *
 * @author kazyra_d
 */
import com.kdg.fs24.lias.opers.napi.LiasOper2Tariff;
import java.util.Collection;

@Deprecated
public interface LiasOpersTemplate {
    
    Collection<LiasOper2Tariff> getLiasOpersDefinitions();
    
}
