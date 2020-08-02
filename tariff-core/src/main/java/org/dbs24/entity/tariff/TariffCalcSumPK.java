/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.tariff;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
public class TariffCalcSumPK implements Serializable {

    private TariffCalcRecord tariffCalcRecord;
    private LocalDate tariffCalcDate;

}
