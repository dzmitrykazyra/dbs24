package org.dbs24.tik.mobile.dao.impl;

import org.dbs24.tik.mobile.constant.CacheKey;
import org.dbs24.tik.mobile.constant.reference.ActionTypeDefine;
import org.dbs24.tik.mobile.dao.OrderActionTypeDao;
import org.dbs24.tik.mobile.entity.domain.OrderActionType;
import org.dbs24.tik.mobile.repo.OrderActionTypeRepo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderActionTypeDaoImpl implements OrderActionTypeDao {

    private final OrderActionTypeRepo orderActionTypeRepo;

    public OrderActionTypeDaoImpl(OrderActionTypeRepo orderActionTypeRepo) {
        this.orderActionTypeRepo = orderActionTypeRepo;
    }

    @Override
    @Cacheable(CacheKey.CACHE_ACTION_TYPE_ID)
    public OrderActionType findActionTypeById(Integer actionTypeId) {
        return orderActionTypeRepo.findByOrderActionTypeId(actionTypeId)
                .orElseThrow(() -> new RuntimeException("Cannot find action type with id = " + actionTypeId));
    }

    @Override
    public List<OrderActionType> findAll() {
        return orderActionTypeRepo.findAll();
    }

    @Override
    @Cacheable(CacheKey.CACHE_FOLLOW_ACTION_TYPE)
    public OrderActionType findFollowActionType() {
        return orderActionTypeRepo.findByOrderActionTypeName(ActionTypeDefine.AT_FOLLOWERS.getOrderActionTypeName())
                .orElseThrow(() -> new RuntimeException("Cannot find 'follow' action type"));
    }
}
