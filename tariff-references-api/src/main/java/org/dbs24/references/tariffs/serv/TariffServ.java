/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.tariffs.serv;

import org.dbs24.references.api.AbstractRefRecord;
import org.dbs24.references.api.ReferenceRec;
import org.dbs24.references.api.ReferenceSyncOrder;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
}