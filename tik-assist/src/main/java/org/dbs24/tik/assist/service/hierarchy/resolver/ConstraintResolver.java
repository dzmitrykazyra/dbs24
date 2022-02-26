package org.dbs24.tik.assist.service.hierarchy.resolver;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.constant.reference.ActionTypeDefine;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.dao.SumToActionsQuantityDao;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
public class ConstraintResolver {

    private final SumToActionsQuantityDao sumToActionsQuantityDao;
    private final ReferenceDao referenceDao;

    private final String likesConstraintName = ActionTypeDefine.AT_GET_LIKES.getActionTypeValue();
    private final String followersConstraintName = ActionTypeDefine.AT_GET_FOLLOWERS.getActionTypeValue();
    private final String viewsConstraintName = ActionTypeDefine.AT_GET_VIEWS.getActionTypeValue();
    private final String commentsConstraintName = ActionTypeDefine.AT_GET_COMMENTS.getActionTypeValue();
    private final String repostsConstraintName = ActionTypeDefine.AT_GET_REPOSTS.getActionTypeValue();

    private final Map<String, Integer> constraintNameToValue;

    public ConstraintResolver(SumToActionsQuantityDao sumToActionsQuantityDao, ReferenceDao referenceDao) {

        this.sumToActionsQuantityDao = sumToActionsQuantityDao;
        this.referenceDao = referenceDao;
        constraintNameToValue = new HashMap<>();
    }

    @PostConstruct
    private void fillMap() {

        constraintNameToValue.put(likesConstraintName, getMaxLikesConstraint());
        constraintNameToValue.put(followersConstraintName, getMaxFollowersConstraint());
        constraintNameToValue.put(viewsConstraintName, getMaxViewsConstraint());
        constraintNameToValue.put(commentsConstraintName, getMaxCommentsConstraint());
        constraintNameToValue.put(repostsConstraintName, getMaxRepostsConstraint());
    }

    public Map<String, Integer> getMaxConstraints() {

        return constraintNameToValue;
    }

    public Integer getMaxLikesConstraint() {

        return getMaxConstraintByActionTypeName(likesConstraintName);
    }

    public Integer getMaxFollowersConstraint() {

        return getMaxConstraintByActionTypeName(followersConstraintName);
    }

    public Integer getMaxViewsConstraint() {

        return getMaxConstraintByActionTypeName(viewsConstraintName);
    }

    public Integer getMaxCommentsConstraint() {

        return getMaxConstraintByActionTypeName(commentsConstraintName);
    }

    public Integer getMaxRepostsConstraint() {

        return getMaxConstraintByActionTypeName(repostsConstraintName);
    }

    public Integer getMaxConstraintByActionTypeName(String actionTypeName) {

        return sumToActionsQuantityDao
                .findMaxQuantityByActionType(referenceDao.findActionTypeByName(actionTypeName))
                .getUpToActionQuantity();
    }
}
