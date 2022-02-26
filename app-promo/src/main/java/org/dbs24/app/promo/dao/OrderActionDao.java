/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.dao;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.app.promo.entity.ActionResult;
import org.dbs24.app.promo.entity.Order;
import org.dbs24.app.promo.entity.OrderAction;
import org.dbs24.app.promo.entity.OrderActionHist;
import org.dbs24.app.promo.repo.OrderActionHistRepo;
import org.dbs24.app.promo.repo.OrderActionRepo;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.dbs24.spring.core.data.PageLoader;
import org.springframework.stereotype.Component;
import javax.persistence.criteria.Predicate;
import java.util.Collection;
import java.util.Optional;

import static org.dbs24.google.api.OrderActionsConsts.ActionResultEnum.AR_FUTURED;

@Getter
@Log4j2
@Component
public class OrderActionDao extends DaoAbstractApplicationService {

    final OrderActionRepo orderActionRepo;
    final OrderActionHistRepo orderActionHistRepo;

    public OrderActionDao(OrderActionRepo orderActionRepo, OrderActionHistRepo orderActionHistRepo) {
        this.orderActionRepo = orderActionRepo;
        this.orderActionHistRepo = orderActionHistRepo;
    }

    //==========================================================================
    public Optional<OrderAction> findOptionalOrderAction(Integer orderId) {
        return orderActionRepo.findById(orderId);
    }

    public OrderAction findOrderAction(Integer orderActionId) {
        return findOptionalOrderAction(orderActionId).orElseThrow(()-> new RuntimeException(String.format("Unknown orderAction (%d)", orderActionId)));
    }

    public void saveOrderAction(OrderAction orderAction) {
        orderActionRepo.save(orderAction);
    }

    public void saveOrderActionHist(OrderActionHist orderActionHist) {
        orderActionHistRepo.save(orderActionHist);
    }

    //==================================================================================================================

    public Collection<OrderAction> loadFuturedActions() {

        final Collection<OrderAction> orderActions = ServiceFuncs.createConcurencyCollection();

        (new PageLoader<OrderAction>() {
            @Override
            public void addPage(Collection<OrderAction> actions) {
                log.debug("add actual orders actions: {} records(s)", actions.size());
                orderActions.addAll(actions);
            }
        }).loadRecords(orderActionRepo, () -> (r, cq, cb) -> {

            final Predicate predicate = cb.conjunction();

            predicate.getExpressions().add(cb.equal(r.get("actionResult"), AR_FUTURED.getCode()));

            return predicate;

        });

        return orderActions;
    }

    public Collection<OrderAction> findOrderActions(Order order) {

        return orderActionRepo.findByOrder(order);

    }

    public Collection<OrderAction> findOrderActions(Order order, ActionResult actionResult) {

        return orderActionRepo.findByOrderAndActionResult(order, actionResult);

    }
}
