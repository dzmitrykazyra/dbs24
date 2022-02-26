/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.rest.dto.tariff.plan;

import lombok.Data;
import org.dbs24.spring.core.api.EntityInfo;

@Data
public class TariffPlanInfo implements EntityInfo {

    private Long tariffPlanId;
    private Long actualDate;
    private Integer tariffPlanStatusId;
    private Integer tariffPlanTypeId;
    private Long tariffLimitId;
    private String tpName;
    private String tpNote;

}
