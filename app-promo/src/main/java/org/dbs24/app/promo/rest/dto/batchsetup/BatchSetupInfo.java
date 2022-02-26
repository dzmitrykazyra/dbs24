/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.rest.dto.batchsetup;

import lombok.Data;
import org.dbs24.spring.core.api.EntityInfo;

@Data
public class BatchSetupInfo implements EntityInfo {

    private Integer batchSetupId;
    private Integer batchTemplateId;
    private Integer actRefId;
    private Integer executionOrder;
    private Integer minDelay;
    private Integer maxDelay;
    private Long actualDate;
    private Boolean isActual;
    private String batchNote;

}
