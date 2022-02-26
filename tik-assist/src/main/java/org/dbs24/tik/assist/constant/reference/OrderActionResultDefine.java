package org.dbs24.tik.assist.constant.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.OrderActionResult;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum OrderActionResultDefine {
    /**
     * 4 - Created task for order but with null value to available bot
     * 5 - Order action, which was executed with error and recreated to execute again
     */

    AR_CREATED              (0, "CREATED"),
    AR_OK                   (1, "OK (finished)"),
    AR_NOT_STARTED          (2, "WARN (not started)"),
    AR_IN_PROGRESS          (3, "IN PROGRESS"),
    AR_WAITING_FOR_BOT      (4, "WAITING FOR BOT"),
    NEED_REMAKE             (5, "NEED REMAKE"),
    AR_FAIL                 (-1, "WARN (finished)"),
    AR_WARN                 (-2, "FAIL (uri not exist)"),
    AR_PAGE_NOT_EXIST       (-3, "FAIL (finished)"),
    AR_UNKNOWN_ACTION       (-4, "FAIL (unknown action)"),
    AR_BOT_IS_EXPIRED       (-5, "FAIL (bot is expired)"),
    AR_TASK_NOT_EXISTS      (-6, "FAIL (task not exists)");

    private final Integer id;
    private final String resultValue;

    public static List<OrderActionResult> getAll() {
        return Arrays.stream(OrderActionResultDefine.values()).map(
                actionResultEnum -> StmtProcessor.create(
                        OrderActionResult.class,
                        orderActionResult -> {
                            orderActionResult.setOrderActionResultId(actionResultEnum.getId());
                            orderActionResult.setOrderActionResultName(actionResultEnum.getResultValue());
                        }
                )
        ).collect(Collectors.toList());
    }
}