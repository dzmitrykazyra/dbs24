/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.entity.dto.order;

import java.math.BigDecimal;
import lombok.Data;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.UserOrder;
import org.dbs24.tik.assist.entity.dto.Dto;

@Data
public class UserOrderDto implements Dto {

    private Integer orderId;
    private Integer planId;
    private Long actualDate;
    private Integer orderStatusId;
    private Integer userId;
    private Integer actionTypeId;
    private String currencyIso;
    private Long beginDate;
    private Long endDate;
    private Long cancelDate;
    private Integer actionsAmount;
    private BigDecimal orderSum;
    private String tiktokUri;
    private Integer accountId;
    private String promocodeValue;
    private String awemeId;
    private String cid;

    public static UserOrderDto toDto(UserOrder userOrder) {

        return StmtProcessor.create(
                UserOrderDto.class,
                userOrderDto -> {
                    userOrderDto.setActionsAmount(userOrder.getActionsAmount());
                    userOrderDto.setPlanId(userOrder.getUserPlan().getPlanId());
                    userOrderDto.setActualDate(NLS.localDateTime2long(userOrder.getActualDate()));
                    userOrderDto.setAwemeId(userOrder.getAwemeId());
                    userOrderDto.setBeginDate(NLS.localDateTime2long(userOrder.getBeginDate()));
                    userOrderDto.setCancelDate(NLS.localDateTime2long(userOrder.getCancelDate()));
                    userOrderDto.setCid(userOrder.getCid());
                    userOrderDto.setOrderId(userOrder.getOrderId());
                    userOrderDto.setOrderStatusId(userOrder.getOrderStatus().getOrderStatusId());
                    userOrderDto.setOrderSum(userOrder.getOrderSum());
                    userOrderDto.setActionTypeId(userOrder.getActionType().getActionTypeId());
                    userOrderDto.setCurrencyIso(userOrder.getCurrency().getCurrencyIso());
                    userOrderDto.setEndDate(NLS.localDateTime2long(userOrder.getEndDate()));
                    userOrderDto.setTiktokUri(userOrder.getTiktokUri());
                    userOrderDto.setUserId(userOrder.getUser().getUserId());
                    userOrderDto.setAccountId(userOrder.getTiktokAccount().getAccountId());
                    userOrderDto.setPromocodeValue(userOrder.getPromocode().getPromocodeValue());
                }
        );
    }

    /**
     * Method allows creating userOrder from dto with null fields:
     *      - userPlan
     *      - orderStatus
     *      - actionType
     *      - user
     *      - currency
     *      - tiktokAccount
     *      - promocode
     */
    public static UserOrder createDefaultEntity(UserOrderDto userOrderDto) {

        return UserOrder.builder()
                .orderId(userOrderDto.getOrderId())
                .actionsAmount(userOrderDto.getActionsAmount())
                .actualDate(NLS.long2LocalDateTime(userOrderDto.getActualDate()))
                .awemeId(userOrderDto.getAwemeId())
                .beginDate(NLS.long2LocalDateTime(userOrderDto.getBeginDate()))
                .cancelDate(NLS.long2LocalDateTime(userOrderDto.getCancelDate()))
                .endDate(NLS.long2LocalDateTime(userOrderDto.getEndDate()))
                .cid(userOrderDto.getCid())
                .orderSum(userOrderDto.getOrderSum())
                .tiktokUri(userOrderDto.getTiktokUri())
                .build();
    }
}
