/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.core.api;

/**
 *
 * @author Козыро Дмитрий
 */
public interface BusinessRule<T extends ActionEntity> {


    EntityWarningsList getEntityWarningsList(T entity);

    EntityFieldsDescriptions getEntityFieldsDescriptions();

}
