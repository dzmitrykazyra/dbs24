package org.dbs24.tik.assist.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.tik.assist.entity.domain.ActionType;
import org.dbs24.tik.assist.entity.domain.SumToActionsQuantity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SumToActionsQuantityRepo extends ApplicationJpaRepository<SumToActionsQuantity, Integer>, JpaSpecificationExecutor<SumToActionsQuantity>, PagingAndSortingRepository<SumToActionsQuantity, Integer> {

    List<SumToActionsQuantity> findByActionTypeAndUpToActionQuantityGreaterThan(ActionType actionType, Integer constraint);

    List<SumToActionsQuantity> findByActionType(ActionType actionType);
}
