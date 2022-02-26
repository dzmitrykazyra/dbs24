/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.notifier.component;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.service.AbstractReferencesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.dbs24.stmt.StmtProcessor.ifTrue;

@Getter
@Log4j2
@Component
@EqualsAndHashCode(callSuper = true)
public class RefsService extends AbstractReferencesService {

    @Value("${refs.synchronize:true}")
    private Boolean needSynchronize;

    public RefsService() {
   }

    @Override
    public void initialize() {

        super.initialize();

        ifTrue(needSynchronize, this::synchronizeRefs, () -> log.info("system references synchronization is disabled "));

    }

    @Transactional
    public void synchronizeRefs() {

        log.info("synchronize system references");

    }
}
