package org.dbs24.app.notifier.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.rest.api.dto.OperationResult;

@Data
@EqualsAndHashCode
public class CreatedMessage extends OperationResult {
    private Integer messageId;
}
