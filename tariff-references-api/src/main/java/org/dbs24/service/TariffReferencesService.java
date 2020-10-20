/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import lombok.Data;
import org.dbs24.persistence.core.PersistenceEntityManager;
import org.dbs24.references.api.AbstractRefRecord;
import org.dbs24.references.tariffs.accretionscheme.TariffAccretionScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
public class TariffReferencesService extends AbstractApplicationService {

    final PersistenceEntityManager persistenceEntityManager;

    //==========================================================================
    @Autowired
    public TariffReferencesService(PersistenceEntityManager persistenceEntityManager) {
        this.persistenceEntityManager = persistenceEntityManager;
    }
    //==========================================================================

    public final TariffAccretionScheme findTariffAccretionScheme(Integer tariffAccretionSchemeId) {
        return AbstractRefRecord.<TariffAccretionScheme>getRefeenceRecord(TariffAccretionScheme.class,
                record -> record.getTariffSchemeId().equals(tariffAccretionSchemeId));
    }
}