package org.dbs24.tik.assist.dao;

import org.dbs24.tik.assist.entity.domain.ActionType;
import org.dbs24.tik.assist.entity.domain.SumToActionsQuantity;

import java.math.BigDecimal;
import java.util.List;

public interface SumToActionsQuantityDao {

    SumToActionsQuantity saveSumToActionsQuantityDao(SumToActionsQuantity sumToActionsQuantity);
    List<SumToActionsQuantity> findAll();
    BigDecimal findSumByQuantity(Integer actionsQuantity, ActionType actionType);
    SumToActionsQuantity findMaximumSumByActionType(ActionType actionType);
    SumToActionsQuantity findMaxQuantityByActionType(ActionType actionType);
}
