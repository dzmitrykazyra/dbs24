package org.dbs24.ad.server.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.rest.api.dto.OperationResult;

@Data
@EqualsAndHashCode
public class CreatedAdsSettings extends OperationResult {
    private Integer adsSettingId;
}
