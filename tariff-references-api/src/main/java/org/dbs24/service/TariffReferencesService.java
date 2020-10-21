/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import java.util.Arrays;
import lombok.Data;
import org.dbs24.persistence.core.PersistenceEntityManager;
import org.dbs24.references.api.AbstractRefRecord;
import org.dbs24.references.core.CachedReferencesClasses;
import org.dbs24.references.tariffs.accretionscheme.TariffAccretionScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.service.funcs.ServiceFuncs;

@Data
@Slf4j
@Service
@CachedReferencesClasses(classes = {TariffAccretionScheme.class})
public class TariffReferencesService extends AbstractReferencesService {

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

    //==========================================================================
    public static final <T extends TariffAccretionScheme> Collection<T> getTariffAccretionSchemeRecords() {

        final Collection<T> actualList = ServiceFuncs.<T>createCollection();
        final Class<T> clazz = (Class<T>) (TariffAccretionScheme.class);

        log.debug("initialize ref values {}", clazz.getCanonicalName());        
        
        final String[][] constList = new String[][]{
            {"1", "30/360"},
            {"2", "FACT/FACT"}
        };

        Arrays.stream(constList)
                .unordered()
                .forEach(stringRow
                        -> actualList.add((T) NullSafe.<T>createObject(clazz, object -> {
                    object.setTariffSchemeId(Integer.valueOf(stringRow[0]));
                    object.setTariffSchemeName(stringRow[1]);
                })));

        return actualList;
    }
}
