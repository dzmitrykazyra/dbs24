/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.rest.dto.batchtemplate;

import lombok.Data;
import org.dbs24.spring.core.api.EntityInfo;

@Data
public class BatchTemplateInfo implements EntityInfo {

    private Integer batchTemplateId;
    private Long actualDate;
    private Integer batchTypeId;
    private Integer providerId;
    private String templateName;
    private Boolean isActual;

}
