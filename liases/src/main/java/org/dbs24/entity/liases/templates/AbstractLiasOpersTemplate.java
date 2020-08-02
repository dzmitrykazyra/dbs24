/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.liases.templates;

import org.dbs24.lias.opers.napi.LiasOper2Tariff;
import java.util.Collection;
import org.dbs24.lias.opers.api.LiasOpersTemplate;

/**
 *
 * @author kazyra_d
 */
public abstract class AbstractLiasOpersTemplate implements LiasOpersTemplate {

    //public Collection<NewLiasOper> opers4creation;

    //==========================================================================
    @Override
    public Collection<LiasOper2Tariff> getLiasOpersDefinitions() {
        
        
        return null;
    }

}
