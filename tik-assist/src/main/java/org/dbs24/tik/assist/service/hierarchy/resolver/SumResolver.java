package org.dbs24.tik.assist.service.hierarchy.resolver;

import io.vavr.control.Either;
import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.dao.DiscountToAccountQuantityDao;
import org.dbs24.tik.assist.entity.domain.ActionType;
import org.dbs24.tik.assist.entity.domain.PlanTemplate;
import org.dbs24.tik.assist.entity.domain.Promocode;
import org.dbs24.tik.assist.entity.dto.order.AbstractCreateOrderDto;
import org.dbs24.tik.assist.entity.dto.plan.CustomPlanConstraint;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Log4j2
@Component
public class SumResolver {

    private final CustomSumResolver customSumResolver;

    private final DiscountToAccountQuantityDao discountToAccountQuantityDao;

    public SumResolver(CustomSumResolver customSumResolver, DiscountToAccountQuantityDao discountToAccountQuantityDao) {

        this.customSumResolver = customSumResolver;
        this.discountToAccountQuantityDao = discountToAccountQuantityDao;
    }

    /**
     * Method allows calculating total subscription(recognizing custom/template cases) sum depending on
     *      custom/template defined costs
     *      promocode discount (if exist)
     *      subscription accounts quantity
     */
    public BigDecimal calculateSubscriptionSum(Either<PlanTemplate, CustomPlanConstraint> templateOrCustomInfo,
                                               Optional<Promocode> promocodeOptional,
                                               Integer accountsQuantity) {
        BigDecimal totalSum;
        BigDecimal singleAccountSubscriptionSum = calculateSumSingleAccount(templateOrCustomInfo);

        totalSum = calculateSumBasedOnPromocode(promocodeOptional, singleAccountSubscriptionSum)
                .subtract(calculateSumAccountQuantityDiscount(singleAccountSubscriptionSum, accountsQuantity))
                .multiply(BigDecimal.valueOf(accountsQuantity));

        return totalSum;
    }


    /**
     * Method allows calculating total plan(recognizing custom/template cases) sum depending on
     *      custom/template defined costs
     *      promocode discount (if exist)
     */
    public BigDecimal calculatePlanSum(Either<PlanTemplate, CustomPlanConstraint> templateOrCustomInfo,
                                       Optional<Promocode> promocodeOptional) {

        BigDecimal singleAccountSubscriptionSum = calculateSumSingleAccount(templateOrCustomInfo);
        return calculateSumBasedOnPromocode(promocodeOptional, singleAccountSubscriptionSum);
    }

    public BigDecimal calculateOrderSum(AbstractCreateOrderDto abstractCreateOrderDto, ActionType actionType) {

        return customSumResolver.calculateSingleActionTypeSum(
                abstractCreateOrderDto.getOrderActionsQuantity(),
                actionType
        );
    }

    private BigDecimal calculateSumSingleAccount(Either<PlanTemplate, CustomPlanConstraint> templateOrCustomInfo) {

        BigDecimal singleAccountSubscriptionSum;

        if (templateOrCustomInfo.isLeft()) {
            singleAccountSubscriptionSum = templateOrCustomInfo.getLeft().getPlanTemplateSum();
        } else {
            singleAccountSubscriptionSum = customSumResolver.calculateCustomSum(templateOrCustomInfo.get());
        }

        return singleAccountSubscriptionSum;
    }

    private BigDecimal calculateSumBasedOnPromocode(Optional<Promocode> promocodeOptional, BigDecimal originalSum) {

        return promocodeOptional
                .map(promocode -> originalSum.subtract(calculateSumPromocodeDiscount(originalSum, promocode)))
                .orElse(originalSum);
    }

    private BigDecimal calculateSumPromocodeDiscount(BigDecimal originalSum, Promocode promocode) {

        return calculateSumDiscount(originalSum, promocode.getPromocodeDiscount());
    }

    private BigDecimal calculateSumAccountQuantityDiscount(BigDecimal originalSum, int accountQuantity) {

        Integer accountQuantityDiscount = discountToAccountQuantityDao.findByAccountsQuantity(accountQuantity).getDiscount();

        return calculateSumDiscount(originalSum, accountQuantityDiscount);
    }

    private BigDecimal calculateSumDiscount(BigDecimal originalSum, Integer discount) {

        return originalSum.multiply(
                BigDecimal.valueOf(discount)
                        .divide(BigDecimal.valueOf(100))
        );
    }
}
