/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.tariff;

import java.math.BigDecimal;
import javax.persistence.*;
import lombok.Data;

//import com.kdg.fs24.application.core.api.ObjectRoot;
@Data
@Entity
@Table(name = "TariffRates_3")
@IdClass(TariffRate_1PK.class)
public class TariffRate_3 extends TariffRecordAbstract {

    // фиксированная величина
    private BigDecimal rate_value;
    private BigDecimal fix_sum;

}
