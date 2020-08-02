/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.references.tariffs.kind;

import com.kdg.fs24.persistence.api.PersistenceEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

/**
 *
 * @author kazyra_d
 */
@Data
public abstract class TariffRateRecord implements PersistenceEntity {

    //LocalDate getRateDate();
    private LocalDate rateDate;
    //BigDecimal getRateValue();
    private BigDecimal rateValue;

}
