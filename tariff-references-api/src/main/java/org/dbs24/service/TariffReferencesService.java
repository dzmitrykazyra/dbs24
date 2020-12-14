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
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.service.funcs.AnnotationFuncs;
import org.dbs24.application.core.service.funcs.ReflectionFuncs;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import static org.dbs24.consts.SysConst.REFERENCE_PACKAGE;
import static org.dbs24.consts.SysConst.TARIFF_PACKAGE;
import static org.dbs24.consts.TariffConst.*;
import org.dbs24.references.api.LangStrValue;
import org.dbs24.references.tariffs.group.TariffGroup;
import org.dbs24.references.tariffs.kind.TariffKind;
import org.dbs24.references.tariffs.kind.TariffKindId;
import org.dbs24.references.tariffs.serv.TariffServ;
import org.dbs24.references.tariffs.serv.TariffServId;

@Data
@Log4j2
@Service
@CachedReferencesClasses(classes = {TariffGroup.class, TariffAccretionScheme.class, TariffKind.class, TariffServ.class})
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
    public final TariffGroup findTariffGroup(Integer tariffGroupId) {
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

    //==========================================================================
    public final TariffKind findTariffKind(Integer tariffKindId) {

        return AbstractRefRecord.<TariffKind>getRefeenceRecord(TARIFF_KIND_CLASS,
                record -> record.getTariffKindId().equals(tariffKindId));
    }

    //==========================================================================
    public static Collection<TariffKind> getTariffKindCollection() {

        final Collection<TariffKind> actualList = ServiceFuncs.<TariffKind>createCollection();

        // значения для справочника берутся из аннотаций классов
        ReflectionFuncs.processPkgClassesCollection(TARIFF_PACKAGE, TARIFF_KIND_CLASS, TARIFF_KINDID_CLASS,
                tkClass -> {

                    final TariffKindId tariffKindId = AnnotationFuncs.getAnnotation(tkClass, TARIFF_KINDID_CLASS);

                    // коллекция для персиста
                    actualList.add(NullSafe.createObject(TARIFF_KIND_CLASS, object -> {
                        object.setTariffKindId(tariffKindId.tariff_kind_id());
                        object.setTariffKindName(AbstractRefRecord.getTranslatedValue(new LangStrValue(tariffKindId.en_kind_name(), tariffKindId.tariff_kind_name())));
                        //object.setTariffServ(NullSafe.createObject(tariffKindId.tariff_serv_class()));

                        object.setTariffServ(AbstractRefRecord.<TariffServ>getRefeenceRecord(TARIFF_SERV_CLASS,
                                record -> record.getTariffServId().equals(tariffKindId.tariff_serv_id())));
                    }));
                });
        return actualList;
    }

    //==========================================================================
    public final TariffServ findTariffServ(Integer tariffServId) {

        return AbstractRefRecord.<TariffServ>getRefeenceRecord(TARIFF_SERV_CLASS,
                record -> record.getTariffServId().equals(tariffServId));
    }

    //==========================================================================
    public static Collection<TariffServ> getTariffServCollection() {

        final Collection<TariffServ> actualList = ServiceFuncs.<TariffServ>createCollection();

        // значения для справочника берутся из аннотаций классов
        ReflectionFuncs.processPkgClassesCollection(REFERENCE_PACKAGE, TARIFF_SERV_CLASS, TARIFF_SERVID_CLASS,
                tkClass -> {

                    final TariffServId tariffServId = AnnotationFuncs.getAnnotation(tkClass, TARIFF_SERVID_CLASS);

                    actualList.add(NullSafe.createObject(TARIFF_SERV_CLASS, object -> {
                        object.setTariffGroupId(tariffServId.group_id());
                        object.setTariffServName(AbstractRefRecord.getTranslatedValue(
                                new LangStrValue(tariffServId.en_serv_name(), tariffServId.serv_name())));
                        object.setTariffServId(tariffServId.serv_id());
                        object.setClientPay(tariffServId.client_pay());
                    }));
                });
        return actualList;
    }
}
