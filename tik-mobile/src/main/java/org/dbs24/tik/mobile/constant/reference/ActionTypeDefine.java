package org.dbs24.tik.mobile.constant.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.entity.domain.OrderActionType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum ActionTypeDefine {
    AT_LIKES(1, "AT.LIKES"),
    AT_VIEWS(2, "AT.VIEWS"),
    AT_COMMENTS(3, "AT.COMMENTS"),
    AT_FOLLOWERS(4, "AT.FOLLOWERS");

    private Integer orderActionTypeId;
    private String orderActionTypeName;

    public static List<OrderActionType> getAll() {
        return Arrays.stream(ActionTypeDefine.values()).map(
                actionTypeEnum -> StmtProcessor.create(
                        OrderActionType.class,
                        actionType -> {
                            actionType.setOrderActionTypeId(actionTypeEnum.getOrderActionTypeId());
                            actionType.setOrderActionTypeName(actionTypeEnum.getOrderActionTypeName());
                        }
                )
        ).collect(Collectors.toList());
    }

    public static ActionTypeDefine getById(Integer id) {

        return Arrays.stream(ActionTypeDefine.values())
                .filter(value -> value.getOrderActionTypeId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Cannot find action type with id = %d", id)));
    }
}