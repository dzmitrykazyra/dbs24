/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity;

import org.dbs24.references.api.ReferenceRec;
import org.dbs24.references.api.AbstractRefRecord;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "core_CurrenciesRef")
public class Currency extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "currency_id")
    private Integer currencyId;
    @Column(name = "currency_iso")
    private String currencyIso;
    @Column(name = "currency_name")
    private String currencyName;

}
