package org.dbs24.rest.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.rest.api.dto.OperationResult;

@EqualsAndHashCode
@Data
public class PrepareSubscriptionAnswer extends OperationResult {
    private Integer pageSize;
}
