package org.dbs24.tik.mobile.dao;

import org.dbs24.tik.mobile.entity.domain.OrderActionType;

import java.util.List;

public interface OrderActionTypeDao {

    OrderActionType findActionTypeById(Integer actionTypeId);

    List<OrderActionType> findAll();

    OrderActionType findFollowActionType();
}
