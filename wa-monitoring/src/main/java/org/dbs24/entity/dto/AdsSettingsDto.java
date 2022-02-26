/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.ad.server.dto.AdSettingDetails;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class AdsSettingsDto {

    @EqualsAndHashCode.Include
    private Integer settingId;
    private Long createDate;
    private Long actualDate;
    @JsonIgnore
    private Integer applicationId;
    private Boolean isActual;
    private AdSettingDetails adSettingDetails;
    private String appPackages;
}
