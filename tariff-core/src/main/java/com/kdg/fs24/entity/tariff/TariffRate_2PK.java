/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.tariff;

import java.math.BigDecimal;
import lombok.Data;
/**
 *
 * @author Козыро Дмитрий
 */
@Data
public class TariffRate_2PK extends TariffRate_1PK {
    
    private BigDecimal min_sum;
    
}
