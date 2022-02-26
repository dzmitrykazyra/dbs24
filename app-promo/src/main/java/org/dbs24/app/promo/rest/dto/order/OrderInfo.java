/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.rest.dto.order;

import lombok.Data;
import org.dbs24.spring.core.api.EntityInfo;

@Data
public class OrderInfo implements EntityInfo {

    private Integer orderId;
    private Long actualDate;
    private Integer appPackageId;
    private Integer batchTemplateId;
    private Integer orderStatusId;
    private String orderName;
    private Integer orderBatchesAmount;
    private Integer successBatchesAmount;
    private Integer failBatchesAmount;
    private Long execStartDate;
    private Long execFinishDate;
    private Long execLastDate;
    private String orderNote;

}
