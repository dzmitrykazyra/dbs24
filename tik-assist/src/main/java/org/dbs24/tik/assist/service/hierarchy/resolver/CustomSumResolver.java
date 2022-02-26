package org.dbs24.tik.assist.service.hierarchy.resolver;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.constant.reference.ActionTypeDefine;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.dao.SumToActionsQuantityDao;
import org.dbs24.tik.assist.entity.domain.ActionType;
import org.dbs24.tik.assist.entity.dto.plan.CustomPlanConstraint;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Log4j2
@Service
public class CustomSumResolver {

    private final ReferenceDao referenceDao;
    private final SumToActionsQuantityDao sumToActionsQuantityDao;

    public CustomSumResolver(ReferenceDao referenceDao, SumToActionsQuantityDao sumToActionsQuantityDao) {

        this.referenceDao = referenceDao;
        this.sumToActionsQuantityDao = sumToActionsQuantityDao;
    }

    public BigDecimal calculateCustomSum(CustomPlanConstraint customPlanConstraint) {

        ActionType likesActionType = referenceDao.findActionTypeById(ActionTypeDefine.AT_GET_LIKES.getId());
        ActionType followersActionType = referenceDao.findActionTypeById(ActionTypeDefine.AT_GET_FOLLOWERS.getId());
        ActionType viewsActionType = referenceDao.findActionTypeById(ActionTypeDefine.AT_GET_VIEWS.getId());
        ActionType commentsActionType = referenceDao.findActionTypeById(ActionTypeDefine.AT_GET_COMMENTS.getId());
        ActionType repostsActionType = referenceDao.findActionTypeById(ActionTypeDefine.AT_GET_REPOSTS.getId());

        BigDecimal sumByLikesQuantity = calculateSingleActionTypeSum(customPlanConstraint.getLikesAmount(), likesActionType);
        BigDecimal sumByFollowersQuantity = calculateSingleActionTypeSum(customPlanConstraint.getFollowersAmount(), followersActionType);
        BigDecimal sumByViewsQuantity = calculateSingleActionTypeSum(customPlanConstraint.getViewsAmount(), viewsActionType);
        BigDecimal sumByCommentsQuantity = calculateSingleActionTypeSum(customPlanConstraint.getCommentsAmount(), commentsActionType);
        BigDecimal sumByRepostsQuantity = calculateSingleActionTypeSum(customPlanConstraint.getRepostsAmount(), repostsActionType);

        return sumByLikesQuantity
                .add(sumByFollowersQuantity)
                .add(sumByViewsQuantity)
                .add(sumByCommentsQuantity)
                .add(sumByRepostsQuantity);
    }

    public BigDecimal calculateSingleActionTypeSum(Integer actionsQuantity, ActionType actionType) {

        return sumToActionsQuantityDao
                .findSumByQuantity(actionsQuantity, actionType)
                .multiply(BigDecimal.valueOf(actionsQuantity));
    }
}
