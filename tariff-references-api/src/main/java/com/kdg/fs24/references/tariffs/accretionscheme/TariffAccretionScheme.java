/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.references.tariffs.accretionscheme;

import com.kdg.fs24.references.api.ReferenceRec;
import java.util.Map;
import com.kdg.fs24.references.api.AbstractRefRecord;
import com.kdg.fs24.references.api.ReferenceSyncOrder;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 *
 * @author kazyra_d
 */
@Data
@Entity
@Table(name = "TariffAccretionSchemeRef")
@ReferenceSyncOrder(order_num = 1)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class TariffAccretionScheme extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "tariff_scheme_id")
    private Integer tariffSchemeId;
    @Column(name = "tariff_scheme_name")
    private String tariffSchemeName;

    @Override
    public void record2Map(final Map<String, Integer> map) {
        //   map.put(String.format("%d - %s", this.getTariff_scheme_id(), this.getTariff_scheme_name()), this.getTariff_scheme_id());
    }

    //==========================================================================
    public final static TariffAccretionScheme findTariffAccretionScheme(final Integer tariffAccretionSchemeId) {
        return AbstractRefRecord.<TariffAccretionScheme>getRefeenceRecord(TariffAccretionScheme.class,
                record -> record.getTariffSchemeId().equals(tariffAccretionSchemeId));
    }
}
