package org.dbs24.tik.assist.constant.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.ActionType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum ActionTypeDefine {

    AT_GET_LIKES(1, "AT.LIKES"),
    AT_GET_VIEWS(2, "AT.VIEWS"),
    AT_GET_COMMENTS(3, "AT.COMMENTS"),
    AT_GET_REPOSTS(4, "AT.REPOSTS"),
    AT_GET_FOLLOWERS(5, "AT.FOLLOWERS");

    private final Integer id;
    private final String actionTypeValue;

    public static List<ActionType> getAll() {
        return Arrays.stream(ActionTypeDefine.values()).map(
                orderTypeEnum -> StmtProcessor.create(
                        ActionType.class,
                        orderType -> {
                            orderType.setActionTypeId(orderTypeEnum.getId());
                            orderType.setActionTypeName(orderTypeEnum.getActionTypeValue());
                        }
                )
        ).collect(Collectors.toList());
    }
}