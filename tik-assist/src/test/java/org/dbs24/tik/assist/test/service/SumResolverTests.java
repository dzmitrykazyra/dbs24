package org.dbs24.tik.assist.test.service;

import io.vavr.control.Either;
import lombok.extern.log4j.Log4j2;
import static org.dbs24.consts.SysConst.ALL_PACKAGES;

import org.dbs24.tik.assist.constant.reference.ActionTypeDefine;
import org.dbs24.tik.assist.dao.DiscountToAccountQuantityDao;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.entity.domain.*;
import org.dbs24.tik.assist.entity.dto.plan.CustomPlanConstraint;
import org.dbs24.tik.assist.service.hierarchy.resolver.CustomSumResolver;
import org.dbs24.tik.assist.service.hierarchy.resolver.SumResolver;
import org.dbs24.tik.assist.test.AbstractTikAssistTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.dbs24.tik.assist.TikAssist;
import org.dbs24.tik.assist.config.TikAssistConfig;

import java.math.BigDecimal;
import java.util.Optional;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TikAssist.class})
@Import({TikAssistConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
public class SumResolverTests extends AbstractTikAssistTest {

    private SumResolver sumResolver;
    private DiscountToAccountQuantityDao discountToAccountQuantityDao;
    private CustomSumResolver customSumResolver;
    private ReferenceDao referenceDao;

    @Autowired
    public SumResolverTests(SumResolver sumResolver, DiscountToAccountQuantityDao discountToAccountQuantityDao, CustomSumResolver customSumResolver, ReferenceDao referenceDao) {
        this.sumResolver = sumResolver;
        this.discountToAccountQuantityDao = discountToAccountQuantityDao;
        this.customSumResolver = customSumResolver;
        this.referenceDao = referenceDao;
    }

    @Order(100)
    @RepeatedTest(1)
    public void checkSubscriptionTemplateCostWithPromocode() {

        runTest(() -> {

            int accountsQuantity = 4;
            BigDecimal onePlanOriginalSum = BigDecimal.valueOf(175.0);

            BigDecimal sum = sumResolver.calculateSubscriptionSum(
                    Either.left(PlanTemplate.builder()
                            .planTemplateSum(onePlanOriginalSum)
                            .build()),
                    Optional.empty(),
                    accountsQuantity
            );

            DiscountToAccountsQuantity accountsDiscount = discountToAccountQuantityDao.findByAccountsQuantity(accountsQuantity);

            BigDecimal manuallyCalculatedSum = onePlanOriginalSum
                    .multiply(BigDecimal.valueOf(accountsQuantity))
                    .multiply(
                            BigDecimal.valueOf(1).subtract(
                                    BigDecimal.valueOf(Double.valueOf(accountsDiscount.getDiscount()) / 100)
                            )
                    );

            log.info("MANYALLY = {}", manuallyCalculatedSum);
            log.info("SUM = {}", sum);

            Assertions.assertEquals(manuallyCalculatedSum, sum);
        });
    }

    @Order(200)
    @RepeatedTest(1)
    public void checkCustomPlanTemplateSumWithPromocode() {

        runTest(() -> {

            int likesAmount = 112;
            int followersAmount = 135;
            int viewsAmount = 71;
            int commentsAmount = 56;
            int repostsAmount = 123;

            Promocode promocode = Promocode.builder().promocodeDiscount(12).build();

            UserPlan customUserPlan = UserPlan.builder()
                    .likesAmount(likesAmount)
                    .followersAmount(followersAmount)
                    .viewsAmount(viewsAmount)
                    .commentsAmount(commentsAmount)
                    .repostsAmount(repostsAmount)
                    .promocode(promocode)
                    .build();

            CustomPlanConstraint customPlanConstraint = CustomPlanConstraint.toDto(customUserPlan);

            BigDecimal calculatedSumWithoutDiscountConsider = customSumResolver.calculateCustomSum(customPlanConstraint);

            ActionType likesActionType = referenceDao.findActionTypeById(ActionTypeDefine.AT_GET_LIKES.getId());
            ActionType followersActionType = referenceDao.findActionTypeById(ActionTypeDefine.AT_GET_FOLLOWERS.getId());
            ActionType viewsActionType = referenceDao.findActionTypeById(ActionTypeDefine.AT_GET_VIEWS.getId());
            ActionType commentsActionType = referenceDao.findActionTypeById(ActionTypeDefine.AT_GET_COMMENTS.getId());
            ActionType repostsActionType = referenceDao.findActionTypeById(ActionTypeDefine.AT_GET_REPOSTS.getId());

            BigDecimal calculatedManuallyWithoutDiscountConsider = customSumResolver.calculateSingleActionTypeSum(likesAmount, likesActionType)
                    .add(customSumResolver.calculateSingleActionTypeSum(followersAmount, followersActionType))
                    .add(customSumResolver.calculateSingleActionTypeSum(viewsAmount, viewsActionType))
                    .add(customSumResolver.calculateSingleActionTypeSum(commentsAmount, commentsActionType))
                    .add(customSumResolver.calculateSingleActionTypeSum(repostsAmount, repostsActionType));

            log.info("MANUALLY {}", calculatedManuallyWithoutDiscountConsider);
            log.info("BY RESOLVER {}", calculatedSumWithoutDiscountConsider);

            Assertions.assertEquals(calculatedManuallyWithoutDiscountConsider, calculatedSumWithoutDiscountConsider);

            sumResolver.calculatePlanSum(Either.right(customPlanConstraint), Optional.of(customUserPlan.getPromocode()));

            Assertions.assertEquals(
                    calculatedManuallyWithoutDiscountConsider
                            .multiply(
                                    BigDecimal.ONE.subtract(BigDecimal.valueOf(promocode.getPromocodeDiscount() + discountToAccountQuantityDao.findByAccountsQuantity(1).getDiscount()).divide(BigDecimal.valueOf(100)))
                                            .setScale(2)),
                    sumResolver.calculateSubscriptionSum(Either.right(customPlanConstraint), Optional.of(promocode),1));
        });

    }
}
