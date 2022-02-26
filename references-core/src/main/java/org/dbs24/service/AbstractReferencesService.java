/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.component.PersistenceService;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.GenericApplicationContext;

import static org.dbs24.consts.SysConst.BOOLEAN_TRUE;

@Getter
@Log4j2
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractReferencesService extends AbstractApplicationService {

    @Value("${entity.core.ref.synchronize:true}")
    private Boolean refSynchronize = BOOLEAN_TRUE;

    @Autowired
    GenericApplicationContext genericApplicationContext;

    @Autowired
    PersistenceService persistenceEntityManager;
}
