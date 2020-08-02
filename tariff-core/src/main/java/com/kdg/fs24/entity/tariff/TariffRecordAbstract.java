/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.tariff;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kdg.fs24.references.tariffs.kind.TariffRateRecord;
import com.kdg.fs24.references.application.currency.Currency;
import java.time.LocalDate;
import javax.persistence.*;
import lombok.Data;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@MappedSuperclass
public class TariffRecordAbstract extends TariffRateRecord {

    @Id
    @ManyToOne
    @JoinColumn(name = "rate_id")
    @JsonIgnore
    private TariffRate tariffRate;
    @Id
    @Column(name = "rate_date")
    private LocalDate rateDate;
    @Id
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;
}
