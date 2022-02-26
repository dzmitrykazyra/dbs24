package org.dbs24.tik.assist.dao.impl;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.dao.SumToActionsQuantityDao;
import org.dbs24.tik.assist.entity.domain.ActionType;
import org.dbs24.tik.assist.entity.domain.SumToActionsQuantity;
import org.dbs24.tik.assist.repo.SumToActionsQuantityRepo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Data
@Log4j2
@Component
public class SumToActionsQuantityDaoImpl implements SumToActionsQuantityDao {

    final ReferenceDao referenceDao;

    final SumToActionsQuantityRepo sumToActionsQuantityRepo;

    @Override
    public SumToActionsQuantity saveSumToActionsQuantityDao(SumToActionsQuantity sumToActionsQuantity) {

        return sumToActionsQuantityRepo.save(sumToActionsQuantity);
    }

    @Override
    public List<SumToActionsQuantity> findAll() {

        return sumToActionsQuantityRepo.findAll();
    }

    @Override
    public BigDecimal findSumByQuantity(Integer actionsQuantity, ActionType actionType) {

        return sumToActionsQuantityRepo.findByActionTypeAndUpToActionQuantityGreaterThan(actionType, actionsQuantity)
                .stream()
                .min(Comparator.comparing(SumToActionsQuantity::getUpToActionQuantity))
                .orElseGet(() -> findMaximumSumByActionType(actionType))
                .getSum();
    }

    @Override
    public SumToActionsQuantity findMaximumSumByActionType(ActionType actionType) {

        return sumToActionsQuantityRepo.findByActionTypeAndUpToActionQuantityGreaterThan(actionType, 0)
                .stream()
                .findAny()
                .orElseThrow(() -> new RuntimeException("No 0 action quantity records to find sum-to-quantity of actions"));
    }

    @Override
    public SumToActionsQuantity findMaxQuantityByActionType(ActionType actionType) {

        return sumToActionsQuantityRepo.findByActionType(actionType)
                .stream()
                .max(Comparator.comparing(SumToActionsQuantity::getUpToActionQuantity))
                .stream()
                .findFirst()
                .orElseGet(() -> findMaximumSumByActionType(actionType));
    }
}
