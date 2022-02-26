/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.entity.dto.order;

import lombok.Data;
import java.util.Collection;
import java.util.List;

import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.tik.assist.entity.dto.order.UserOrderDto;

@Data
public class UserOrderDtoList {

    private List<UserOrderDto> userOrderDtoList;
}
