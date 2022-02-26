/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.rest.dto.action;

import lombok.Data;
import org.dbs24.spring.core.api.EntityInfo;

@Data
public class OrderActionInfo implements EntityInfo {

    private Integer actionId;
    private Long actualDate;
    private Long actionStartDate;
    private Long actionFinishDate;
    private Integer batchSetupId;
    private Integer actRefId;
    private Integer actionResultId;
    private Integer orderId;
    private Integer botId;
    private Integer executionOrder;
    private String usedIp;
    private String errMsg;

}
