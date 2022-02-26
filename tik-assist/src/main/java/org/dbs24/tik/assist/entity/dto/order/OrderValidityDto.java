package org.dbs24.tik.assist.entity.dto.order;

import lombok.Data;

@Data
public class OrderValidityDto {

    private String message;

    public static OrderValidityDto empty() {
        return new OrderValidityDto();
    }
}
