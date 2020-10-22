/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import lombok.Data;
import org.dbs24.references.api.AbstractRefRecord;
import org.dbs24.references.core.CachedReferencesClasses;
import org.dbs24.references.tariffs.accretionscheme.TariffAccretionScheme;
import org.springframework.stereotype.Service;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import static org.dbs24.consts.TariffConst.*;
import org.dbs24.references.api.LangStrValue;
import org.dbs24.references.tariffs.group.TariffGroup;

@Data
@Slf4j
@Service
@CachedReferencesClasses(classes = {TariffGroup.class, TariffAccretionScheme.class})
public class TariffReferencesService extends AbstractReferencesService {

    //==========================================================================
    public final TariffAccretionScheme findTariffAccretionScheme(Integer tariffAccretionSchemeId) {
        return AbstractRefRecord.<TariffAccretionScheme>getRefeenceRecord(TARIFF_ACCRETION_SCHEME_CLASS,
                record -> record.getTariffSchemeId().equals(tariffAccretionSchemeId));
    }

    //==========================================================================
    public static final Collection<TariffAccretionScheme> getTariffAccretionSchemeCollection() {

        return AbstractReferencesService.<TariffAccretionScheme>getGenericCollection(
                TARIFF_ACCRETION_SCHEME_CLASS, new String[][]{
                    {"1", "30/360"},
                    {"2", "FACT/FACT"}
                },
                (record, stringRow) -> {
                    record.setTariffSchemeId(Integer.valueOf(stringRow[0]));
                    record.setTariffSchemeName(stringRow[1]);
                });
    }

    //==========================================================================
    public static final TariffGroup findTariffGroup(Integer tariffGroupId) {
        return AbstractRefRecord.<TariffGroup>getRefeenceRecord(TARIFF_GROUP_CLASS,
                record -> record.getTariffGroupId().equals(tariffGroupId));
    }

    //==========================================================================
    public static Collection<TariffGroup> getTariffGroupCollection() {

        return AbstractReferencesService.<TariffGroup>getGenericCollection(
                TARIFF_GROUP_CLASS, new String[][]{
                    {"101", "Loan issue", "Представление средств в виде кредита"}
                },
                (record, stringRow) -> {
                    record.setTariffGroupId(Integer.valueOf(stringRow[0]));
                    record.setTariffGroupName(AbstractRefRecord.getTranslatedValue(new LangStrValue(stringRow[1], stringRow[2])));
                });
    }
}
