/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.securities.api;

/**
 *
 * @author kazyra_d
 */
import com.kdg.fs24.entity.contract.AbstractEntityContract;
import com.kdg.fs24.exception.api.CreateEntityException;

public abstract class AbstractSecurity extends AbstractEntityContract {

    public AbstractSecurity(final Long entity_id) throws CreateEntityException {
        super(entity_id);
    }

}
