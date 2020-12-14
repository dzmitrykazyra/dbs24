/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.tariffs.accretionscheme;

import org.dbs24.references.api.ReferenceRec;
import org.dbs24.references.api.AbstractRefRecord;
import org.dbs24.references.api.ReferenceSyncOrder;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Data
@Entity
@Table(name = "TariffAccretionSchemeRef")
@ReferenceSyncOrder(order_num = 1)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TariffAccretionScheme extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "tariff_scheme_id")
    Integer tariffSchemeId;
    @Column(name = "tariff_scheme_name")
    String tariffSchemeName;
}
