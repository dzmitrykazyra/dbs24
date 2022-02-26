package org.dbs24.tik.assist.entity.dto.order;

import lombok.Data;

@Data
public abstract class AbstractCreateOrderDto {

    protected Integer orderActionsQuantity;
    protected Integer executionDaysQuantity;
    protected String promocode;
}
