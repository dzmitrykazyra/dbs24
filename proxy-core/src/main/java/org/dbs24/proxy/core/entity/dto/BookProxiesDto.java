package org.dbs24.proxy.core.entity.dto;

import lombok.Data;
import org.dbs24.proxy.core.entity.domain.*;
import org.dbs24.spring.core.api.EntityInfo;
import org.dbs24.stmt.StmtProcessor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class BookProxiesDto implements EntityInfo {

    private Integer amount;
    private String proxyTypeName;
    private String providerName;
    private String countryName;

    @NotNull
    private String applicationName;
    private Integer algorithmId;

    @NotNull
    private Integer bookingTimeMillis;

    public boolean isFieldValueBlank(String fieldValue) {
        return fieldValue == null
                || "any".equals(fieldValue)
                ||  "".equals(fieldValue);
    }

    public boolean isAmountValueValid(Integer amount) {
        return amount != null
                && amount > 0;
    }
}
