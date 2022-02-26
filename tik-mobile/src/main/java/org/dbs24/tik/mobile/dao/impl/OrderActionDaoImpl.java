package org.dbs24.tik.mobile.dao.impl;

import org.dbs24.tik.mobile.dao.OrderActionDao;
import org.dbs24.tik.mobile.entity.domain.OrderAction;
import org.dbs24.tik.mobile.repo.OrderActionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderActionDaoImpl implements OrderActionDao {

    private final OrderActionRepo orderActionRepo;

    @Autowired
    public OrderActionDaoImpl(OrderActionRepo orderActionRepo) {
        this.orderActionRepo = orderActionRepo;
    }

    @Override
    public void saveOrderAction(OrderAction orderAction) {
        orderActionRepo.save(orderAction);
    }
}
