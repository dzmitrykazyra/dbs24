package org.dbs24.entity.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.rest.api.dto.OperationResult;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ActualMessagesCountDtoResponse extends OperationResult {

    @EqualsAndHashCode.Include
    private Integer messagesCount;

}
