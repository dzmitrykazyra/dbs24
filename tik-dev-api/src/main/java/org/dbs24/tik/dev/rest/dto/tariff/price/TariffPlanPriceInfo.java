/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.rest.dto.tariff.price;

import lombok.Data;
import org.dbs24.spring.core.api.EntityInfo;

import java.math.BigDecimal;

@Data
public class TariffPlanPriceInfo implements EntityInfo {

    private Long tariffPriceId;
    private Long actualDate;
    private Long tariffBeginDate;
    private Integer tariffPlanTypeId;
    private String countryCode;
    private String currencyIso;
    private BigDecimal summ;

}
