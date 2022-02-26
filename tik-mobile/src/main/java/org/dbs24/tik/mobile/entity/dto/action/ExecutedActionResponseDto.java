package org.dbs24.tik.mobile.entity.dto.action;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dbs24.stmt.StmtProcessor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExecutedActionResponseDto {
    private Integer orderId;
    private boolean isSuccessCompleted;

    public static ExecutedActionResponseDto of (Integer orderId, Boolean isSuccessCompleted) {
        return StmtProcessor.create(ExecutedActionResponseDto.class, response -> {
            response.setOrderId(orderId);
            response.setSuccessCompleted(isSuccessCompleted);
        });
    }
}