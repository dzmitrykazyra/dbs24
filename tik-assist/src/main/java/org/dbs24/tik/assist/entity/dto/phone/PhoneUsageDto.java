/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.entity.dto.phone;

import lombok.Data;

@Data
public class PhoneUsageDto {

    private Integer phoneUsageId;
    private Integer phoneId;
    private Long actualDate;
    private Boolean isSuccess;
    private String errMsg;
}
