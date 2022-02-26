package org.dbs24.tik.mobile.dao.impl;

import org.dbs24.tik.mobile.dao.OrderPriceDao;
import org.dbs24.tik.mobile.entity.domain.OrderPrice;
import org.dbs24.tik.mobile.entity.domain.OrderActionType;
import org.dbs24.tik.mobile.repo.OrderPriceRepo;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class OrderPriceDaoImpl implements OrderPriceDao {

    private final OrderPriceRepo orderPriceRepo;

    public OrderPriceDaoImpl(OrderPriceRepo orderPriceRepo) {

        this.orderPriceRepo = orderPriceRepo;
    }

    @Override
    public OrderPrice save(OrderPrice costToQuantityToSave) {

        return orderPriceRepo.save(costToQuantityToSave);
    }

    @Override
    public Integer findByActionTypeAndActionsQuantity(OrderActionType orderActionType, Integer actionsQuantity) {

        return orderPriceRepo.findByOrderActionTypeAndUpToActionsQuantityGreaterThanEqual(orderActionType, actionsQuantity)
                .stream()
                .max(Comparator.comparing(OrderPrice::getSum))
                .orElseThrow(() -> new RuntimeException("No order cost proportion record in db"))
                .getSum();
    }

    @Override
    public List<OrderPrice> findAll() {

        return orderPriceRepo.findAll();
    }
}
