package org.dbs24.tik.assist.entity.dto.payment;

import lombok.Data;

@Data
public class MonthlySubscriptionPaymentDto {

    private Integer userId;
    private Boolean isSuccess;
}
