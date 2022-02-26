package org.dbs24.tik.assist.entity.dto.payment;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class IncreaseUserDepositDto {

    private BigDecimal increaseAmount;
}
