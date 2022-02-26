package org.dbs24.tik.mobile.service.impl;

import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.constant.reference.ActionTypeDefine;
import org.dbs24.tik.mobile.dao.OrderPriceDao;
import org.dbs24.tik.mobile.dao.OrderActionTypeDao;
import org.dbs24.tik.mobile.entity.domain.OrderPrice;
import org.dbs24.tik.mobile.entity.domain.OrderActionType;
import org.dbs24.tik.mobile.entity.dto.proportion.order.OrderPriceDto;
import org.dbs24.tik.mobile.entity.dto.proportion.order.OrderPriceListDto;
import org.dbs24.tik.mobile.service.OrderPriceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class OrderPriceServiceImpl implements OrderPriceService {

    @Value("${action.cost.watch}")
    private Integer watchActionCost;

    @Value("${action.cost.like}")
    private Integer likeActionCost;

    @Value("${action.cost.comment}")
    private Integer commentActionCost;

    @Value("${action.cost.subscribe}")
    private Integer subscribeActionCost;

    private final OrderPriceDao orderPriceDao;
    private final OrderActionTypeDao orderActionTypeDao;

    public OrderPriceServiceImpl(OrderPriceDao orderPriceDao, OrderActionTypeDao orderActionTypeDao) {

        this.orderPriceDao = orderPriceDao;
        this.orderActionTypeDao = orderActionTypeDao;
    }

    @Override
    public Mono<OrderPriceDto> create(Mono<OrderPriceDto> orderPriceDtoMono) {

        return orderPriceDtoMono.map(
                orderPriceDto -> {
                    OrderPrice costToQuantityToSave = orderPriceDto.toDefault();
                    costToQuantityToSave.setOrderActionType(orderActionTypeDao.findActionTypeById(orderPriceDto.getActionTypeId()));
                    OrderPrice savedRecord = orderPriceDao.save(costToQuantityToSave);

                    return OrderPriceDto.of(savedRecord);
                }
        );
    }

    @Override
    public Mono<OrderPriceDto> getOrderPriceDtoByActionsQuantityAndActionTypeId(Integer actionsQuantity, Integer actionTypeId) {

        return Mono.just(
                StmtProcessor.create(
                        OrderPriceDto.class,
                        orderPriceDto -> {
                            Integer orderCost = getOrderCostByActionsAmountAndActionType(
                                    actionsQuantity,
                                    orderActionTypeDao.findActionTypeById(actionTypeId)
                            );

                            orderPriceDto.setHeartsSum(orderCost);
                            orderPriceDto.setUpToActionsQuantity(actionsQuantity);
                            orderPriceDto.setActionTypeId(actionTypeId);
                        }
                )
        );
    }

    @Override
    public Integer getActionCost(OrderActionType orderActionType) {
        ActionTypeDefine actionTypeDefine = ActionTypeDefine.getById(orderActionType.getOrderActionTypeId());

        return switch (actionTypeDefine) {
            case AT_LIKES -> likeActionCost;
            case AT_VIEWS -> watchActionCost;
            case AT_COMMENTS -> commentActionCost;
            case AT_FOLLOWERS -> subscribeActionCost;
        };
    }

    @Override
    public Integer getOrderCostByActionsAmountAndActionType(Integer actionsQuantity, OrderActionType orderActionType) {

        Integer singleOrderActionCost = orderPriceDao.findByActionTypeAndActionsQuantity(orderActionType, actionsQuantity);

        return singleOrderActionCost * actionsQuantity;
    }

    @Override
    public Mono<OrderPriceListDto> getOrderPrices() {

        return Mono.just(
                OrderPriceListDto.of(
                        orderPriceDao.findAll()
                )
        );
    }
}
