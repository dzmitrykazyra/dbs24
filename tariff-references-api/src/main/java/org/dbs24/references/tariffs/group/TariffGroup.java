/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.tariffs.group;

import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.references.api.ReferenceRec;
import org.dbs24.references.api.ReferenceSyncOrder;
import java.util.Map;
import org.dbs24.references.api.AbstractRefRecord;
//import org.dbs24.references.api.DocumentsConst;
import org.dbs24.references.api.LangStrValue;
import java.util.Arrays;
import java.util.Collection;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Data
@Entity
@Table(name = "TariffGroupsRef")
@ReferenceSyncOrder(order_num = 1)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class TariffGroup extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "tariff_group_id")
    private Integer tariffGroupId;
    @Column(name = "tariff_group_name")
    private String tariffGroupName;

    //==========================================================================
    public static final TariffGroup findTariffGroup(final Integer tariffGroupId) {
        return AbstractRefRecord.<TariffGroup>getRefeenceRecord(TariffGroup.class,
                record -> record.getTariffGroupId().equals(tariffGroupId));
    }

    public static <T extends TariffGroup> Collection<T> getActualReferencesList() {

        final Collection<T> actualList = ServiceFuncs.<T>createCollection();
        final Class<T> clazz = (Class<T>) (TariffGroup.class);
//
        final String[][] constList = new String[][]{
            {"101", "Loan issue", "Представление средств в виде кредита"}
        };

        Arrays.stream(constList)
                .unordered()
                .forEach(stringRow
                        -> actualList.add((T) NullSafe.<T>createObject(clazz, (object) -> {
                    object.setTariffGroupId(Integer.valueOf(stringRow[0]));
                    object.setTariffGroupName(AbstractRefRecord.getTranslatedValue(new LangStrValue(stringRow[1], stringRow[2])));
                })));

        return actualList;
    }
}
