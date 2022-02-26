package org.dbs24.proxy.core.entity.dto;

import lombok.Data;
import org.dbs24.spring.core.api.EntityInfo;

@Data
public class UpdateBookedProxiesDto implements EntityInfo {
    private Integer requestId;
    private Integer amount;
    private Integer bookingTimeMills;
}
