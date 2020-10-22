/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.tariffs.serv;

import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.service.funcs.AnnotationFuncs;
import org.dbs24.application.core.service.funcs.ReflectionFuncs;
import org.dbs24.references.api.LangStrValue;
import org.dbs24.references.api.AbstractRefRecord;
import org.dbs24.references.api.ReferenceRec;
import org.dbs24.references.api.ReferenceSyncOrder;
import java.util.Collection;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import static org.dbs24.application.core.sysconst.SysConst.*;

@Data
@Entity
@Table(name = "TariffServsRef")
@ReferenceSyncOrder(order_num = 1)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TariffServ extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "tariff_serv_id", updatable = false)
    private Integer tariffServId;
    @Column(name = "tariff_group_id")
    private Integer tariffGroupId;
    @Column(name = "tariff_serv_name")
    private String tariffServName;
    @Column(name = "client_pay")
    private Boolean clientPay;

    //==========================================================================
    public static <T extends TariffServ, A extends TariffServId> Collection<T> getActualReferencesList() {

        final Collection<T> actualList = ServiceFuncs.<T>createCollection();
        final Class<T> clazz = (Class<T>) (TariffServ.class);
        final Class annClazz = (Class<A>) TariffServId.class;

        // значения для справочника берутся из аннотаций классов
        ReflectionFuncs.processPkgClassesCollection(REFERENCE_PACKAGE, clazz, annClazz,
                (tkClass) -> {

                    final A classAnnotation = (A) AnnotationFuncs.getAnnotation(tkClass, annClazz);

                    actualList.add((T) NullSafe.<T>createObject(clazz, (object) -> {
                        object.setTariffGroupId(classAnnotation.group_id());
                        object.setTariffServName(AbstractRefRecord.getTranslatedValue(
                                new LangStrValue(classAnnotation.en_serv_name(), classAnnotation.serv_name())));
                        object.setTariffServId(classAnnotation.serv_id());
                        object.setClientPay(classAnnotation.client_pay());
                    }));
                });
        return actualList;
    }
}
