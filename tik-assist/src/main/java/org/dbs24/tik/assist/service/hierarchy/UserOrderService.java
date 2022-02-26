/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.service.hierarchy;

import java.time.LocalDateTime;
import java.util.*;

import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.tik.assist.constant.reference.ActionTypeDefine;
import org.dbs24.tik.assist.entity.dto.tiktok.TiktokUserDto;
import org.dbs24.tik.assist.entity.dto.tiktok.TiktokUserPostDto;
import org.dbs24.tik.assist.service.DtoDomainTransferable;
import org.dbs24.tik.assist.service.exception.ActionTypeMismatchException;
import org.dbs24.tik.assist.service.exception.InvalidOrderDataException;
import org.dbs24.tik.assist.service.exception.QuantityOutOfConstraintException;
import org.dbs24.tik.assist.service.hierarchy.resolver.ConstraintResolver;
import org.dbs24.tik.assist.service.hierarchy.resolver.SumResolver;
import org.dbs24.tik.assist.service.hierarchy.split.OrderToActionSplitter;
import org.dbs24.tik.assist.dao.*;
import org.dbs24.tik.assist.entity.domain.*;

import org.dbs24.tik.assist.entity.dto.order.*;
import org.dbs24.tik.assist.service.tiktok.resolver.TiktokInteractor;
import org.dbs24.tik.assist.service.tiktok.resolver.TiktokLinkHelper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
public class UserOrderService extends AbstractApplicationService implements DtoDomainTransferable<UserOrderDto, UserOrder> {

    private final UserDao                   userDao;
    private final UserPlanDao               userPlanDao;
    private final PromocodeDao              promocodeDao;
    private final ReferenceDao              referenceDao;
    private final UserOrderDao              userOrderDao;
    private final OrderExecutionProgressDao orderExecutionProgressDao;

    private final SumResolver                  sumResolver;
    private final TiktokInteractor             tiktokInteractor;
    private final OrderToActionSplitter        orderToActionSplitter;
    private final ConstraintResolver           constraintResolver;

    public UserOrderService(UserOrderDao userOrderDao,
                            UserDao userDao,
                            UserPlanDao userPlanDao,
                            PromocodeDao promocodeDao,
                            ReferenceDao referenceDao,
                            OrderExecutionProgressDao orderExecutionProgressDao,
                            ConstraintResolver constraintResolver,
                            SumResolver sumResolver,
                            OrderToActionSplitter orderToActionSplitter,
                            TiktokInteractor tiktokInteractor) {
        this.userOrderDao = userOrderDao;
        this.userDao = userDao;
        this.userPlanDao = userPlanDao;
        this.promocodeDao = promocodeDao;
        this.referenceDao = referenceDao;
        this.orderExecutionProgressDao = orderExecutionProgressDao;
        this.constraintResolver = constraintResolver;
        this.sumResolver = sumResolver;
        this.orderToActionSplitter = orderToActionSplitter;
        this.tiktokInteractor = tiktokInteractor;
    }

    @Override
    public UserOrder transfer(UserOrderDto userOrderDto) {

        UserOrder userOrder = UserOrderDto.createDefaultEntity(userOrderDto);

        userOrder.setUserPlan(userPlanDao.findUserPlanById(userOrderDto.getPlanId()));
        userOrder.setOrderStatus(referenceDao.findOrderStatusById(userOrderDto.getOrderStatusId()));
        userOrder.setActionType(referenceDao.findActionTypeById(userOrderDto.getActionTypeId()));
        userOrder.setUser(userDao.findUserById(userOrderDto.getUserId()));
        userOrder.setPromocode(promocodeDao.activatePromocode(
                userOrderDto.getUserId(),
                userOrderDto.getPromocodeValue())
        );

        return userOrder;
    }

    public List<UserOrder> createUserOrders(List<UserOrder> userOrders) {

        List<UserOrder> savedUserOrders = userOrderDao.saveOrders(userOrders);

        orderToActionSplitter.splitAll(savedUserOrders);
        orderExecutionProgressDao.saveZeroProgressesByUserOrders(savedUserOrders);

        return savedUserOrders;
    }

    @Transactional
    public CreatedUserOrderDto createOrUpdateOrder(UserOrderDto userOrderDto) {

        UserOrder userOrder = transfer(userOrderDto);

        if (userOrderDto.getOrderId() != null) {
            userOrderDao.saveOrderHistoryByUserOrder(userOrder);
        }

        userOrderDao.saveOrder(userOrder);

        return CreatedUserOrderDto.toDto(userOrder);
    }

    @Transactional
    public CreatedUserOrderDto createLikesOrder(Integer userId, CreateVideoOrderDto createLikesOrderDto) {

        if (createLikesOrderDto.getOrderActionsQuantity() > constraintResolver.getMaxLikesConstraint()) {
            throw new QuantityOutOfConstraintException(HttpStatus.BAD_REQUEST);
        }

        return CreatedUserOrderDto.toDto(
                saveVideoOrder(
                        userId,
                        createLikesOrderDto,
                        ActionTypeDefine.AT_GET_LIKES
                )
        );
    }

    @Transactional
    public CreatedUserOrderDto createViewsOrder(Integer userId, CreateVideoOrderDto createViewsOrderDto) {

        if (createViewsOrderDto.getOrderActionsQuantity() > constraintResolver.getMaxViewsConstraint()) {
            throw new QuantityOutOfConstraintException(HttpStatus.BAD_REQUEST);
        }

        return CreatedUserOrderDto.toDto(
                saveVideoOrder(
                        userId,
                        createViewsOrderDto,
                        ActionTypeDefine.AT_GET_VIEWS
                )
        );
    }

    private UserOrder saveVideoOrder(Integer userId, CreateVideoOrderDto videoOrderDto, ActionTypeDefine actionTypeDefine) {

        String tiktokUsername = TiktokLinkHelper.extractUsernameFromLink(videoOrderDto.getVideoLink());
        User user = userDao.findUserById(userId);

        UserOrder createdViewsOrder = createActiveUserOrderByActionType(
                videoOrderDto,
                user,
                referenceDao.findActionTypeById(actionTypeDefine.getId())
        );

        TiktokUserPostDto userPost = tiktokInteractor.searchPostByWebLink(videoOrderDto.getVideoLink()).getTiktokUserPostDto();

        createdViewsOrder.setAwemeId(userPost.getAwemeId());
        createdViewsOrder.setTiktokAccount(tiktokInteractor.getTiktokAccount(tiktokUsername, user));
        createdViewsOrder.setTiktokUri(userPost.getWebLink());

        UserOrder savedViewsOrder = userOrderDao.saveOrder(createdViewsOrder);
        orderExecutionProgressDao.saveZeroProgressByUserOrder(savedViewsOrder);
        orderToActionSplitter.split(savedViewsOrder);

        return savedViewsOrder;
    }

    @Transactional
    public CreatedUserOrderDto createFollowersOrder(Integer userId, CreateFollowersOrderDto createFollowersOrderDto) {

        if (createFollowersOrderDto.getOrderActionsQuantity() > constraintResolver.getMaxFollowersConstraint()) {
            throw new QuantityOutOfConstraintException(HttpStatus.BAD_REQUEST);
        }

        User user = userDao.findUserById(userId);

        UserOrder createdFollowersOrder = createActiveUserOrderByActionType(
                createFollowersOrderDto,
                user,
                referenceDao.findActionTypeById(ActionTypeDefine.AT_GET_FOLLOWERS.getId())
        );
        createdFollowersOrder.setTiktokAccount(tiktokInteractor.getTiktokAccount(createFollowersOrderDto.getTiktokAccountUsername(), user));

        UserOrder savedFollowersOrder = userOrderDao.saveOrder(createdFollowersOrder);
        orderExecutionProgressDao.saveZeroProgressByUserOrder(savedFollowersOrder);
        orderToActionSplitter.split(savedFollowersOrder);

        return CreatedUserOrderDto.toDto(savedFollowersOrder);
    }

    private UserOrder createActiveUserOrderByActionType(AbstractCreateOrderDto abstractCreateOrderDto, User user,  ActionType actionType) {

        return UserOrder.builder()
                .actionsAmount(abstractCreateOrderDto.getOrderActionsQuantity())
                .actualDate(LocalDateTime.now())
                .beginDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(abstractCreateOrderDto.getExecutionDaysQuantity()))
                .orderSum(sumResolver.calculateOrderSum(abstractCreateOrderDto, actionType))
                .orderStatus(referenceDao.findActualOrderStatus())
                .promocode(promocodeDao.activatePromocode(user.getUserId(), abstractCreateOrderDto.getPromocode()))
                .actionType(actionType)
                .user(user)
                .build();
    }

    public UserVideoOrderDetailsDto getVideoUserOrderDetails(Integer userOrderId) {

        UserOrder videoUserOrder = userOrderDao.findOrderById(userOrderId);

        if (referenceDao.findVideoActionTypes().stream()
                .noneMatch(
                        actionType -> actionType.getActionTypeId()
                                .equals(videoUserOrder.getActionType().getActionTypeId()))) {
            throw new ActionTypeMismatchException(HttpStatus.BAD_REQUEST);
        }
        TiktokUserPostDto userPost = tiktokInteractor.searchPostByWebLink(videoUserOrder.getTiktokUri()).getTiktokUserPostDto();

        UserVideoOrderDetailsDto orderDetails = UserVideoOrderDetailsDto.of(videoUserOrder);
        orderDetails.setTiktokPostData(userPost);

        return orderDetails;
    }

    public UserAccountOrderDetailsDto getAccountUserOrderDetails(Integer userOrderId) {

        UserOrder accountUserOrder = userOrderDao.findOrderById(userOrderId);

        if (referenceDao.findAccountActionTypes().stream()
                .noneMatch(
                        actionType -> actionType.getActionTypeId()
                                .equals(accountUserOrder.getActionType().getActionTypeId()))) {
            throw new ActionTypeMismatchException(HttpStatus.BAD_REQUEST);
        }

        String accountUsername = accountUserOrder.getTiktokAccount().getAccountUsername();

        TiktokUserDto tiktokAccount = tiktokInteractor.searchTiktokUserByUsername(accountUsername);

        UserAccountOrderDetailsDto orderDetails = UserAccountOrderDetailsDto.of(accountUserOrder);
        orderDetails.setAccountData(tiktokAccount);

        return orderDetails;
    }

    public OrderValidityDto verifyFollowersOrder(Integer userId, CreateFollowersOrderDto createFollowersOrderDto) {

        verifyPromocode(userId, createFollowersOrderDto);

        if (tiktokInteractor.searchTiktokUserByUsername(createFollowersOrderDto.getTiktokAccountUsername()).getName().isBlank()) {
            throw new InvalidOrderDataException(HttpStatus.BAD_REQUEST);
        }

        return OrderValidityDto.empty();
    }

    public OrderValidityDto verifyVideoOrder(Integer userId, CreateVideoOrderDto verifyVideoOrderDto) {

        verifyPromocode(userId, verifyVideoOrderDto);

        if (tiktokInteractor.searchPostByWebLink(verifyVideoOrderDto.getVideoLink()).getTiktokUserPostDto().getWebLink() == null) {
            throw new InvalidOrderDataException(HttpStatus.BAD_REQUEST);
        }

        return OrderValidityDto.empty();
    }

    private void verifyPromocode(Integer userId, AbstractCreateOrderDto verifyOrderDto) {


        String promocode = verifyOrderDto.getPromocode();

        if (promocode != null
                && !promocode.isBlank()
                && !promocodeDao.verifyPromocodeValueForUser(promocode, userId)) {

            throw new InvalidOrderDataException(HttpStatus.BAD_REQUEST);
        }
    }
}
