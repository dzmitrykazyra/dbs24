/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.entity.dto.order;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.UserOrder;


@Data
public class CreatedUserOrderDto {

    private Integer createdOrderId;

    public static CreatedUserOrderDto toDto(UserOrder userOrder) {

        return StmtProcessor.create(
                CreatedUserOrderDto.class,
                createdUserOrderDto -> createdUserOrderDto.setCreatedOrderId(userOrder.getOrderId())
        );
    }
}
