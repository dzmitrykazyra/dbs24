package org.dbs24.tik.assist.service.hierarchy;

import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.service.hierarchy.resolver.ConstraintResolver;
import org.dbs24.tik.assist.dao.DiscountToAccountQuantityDao;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.dao.SumToActionsQuantityDao;
import org.dbs24.tik.assist.entity.domain.SumToActionsQuantity;
import org.dbs24.tik.assist.entity.dto.constraint.CustomPlanConstraintsDto;
import org.dbs24.tik.assist.entity.dto.proportion.*;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Log4j2
@Service
public class ProportionService {

    private final ConstraintResolver constraintResolver;

    private final DiscountToAccountQuantityDao discountToAccountQuantityDao;
    private final SumToActionsQuantityDao sumToActionsQuantityDao;
    private final ReferenceDao referenceDao;

    public ProportionService(ConstraintResolver constraintResolver, DiscountToAccountQuantityDao discountToAccountQuantityDao, SumToActionsQuantityDao sumToActionsQuantityDao, ReferenceDao referenceDao) {

        this.constraintResolver = constraintResolver;
        this.discountToAccountQuantityDao = discountToAccountQuantityDao;
        this.sumToActionsQuantityDao = sumToActionsQuantityDao;
        this.referenceDao = referenceDao;
    }

    public CreatedActionsProportionDto createOrUpdateActionsProportion(ActionsProportionDto actionsProportionDto) {

        SumToActionsQuantity sumToActionsQuantity = ActionsProportionDto.defaultSumToActionsQuantityFromDto(actionsProportionDto);
        sumToActionsQuantity.setActionType(referenceDao.findActionTypeByName(actionsProportionDto.getActionTypeName()));

        return StmtProcessor.create(
                CreatedActionsProportionDto.class,
                createdActionsProportionDto -> createdActionsProportionDto.setCreatedActionProportionId(
                        sumToActionsQuantityDao
                                .saveSumToActionsQuantityDao(sumToActionsQuantity)
                                .getSumId()
                )
        );
    }

    public CreatedAccountsProportionDto createOrUpdateAccountsProportion(AccountsProportionDto accountsProportionDto) {

        return StmtProcessor.create(
                CreatedAccountsProportionDto.class,
                createdAccountsProportionDto -> createdAccountsProportionDto.setCreatedAccountProportionId(
                        discountToAccountQuantityDao
                                .saveDiscountToAccountsQuantity(
                                        AccountsProportionDto.toDiscountToAccountsQuantity(accountsProportionDto)
                                )
                                .getDiscountId()
                )
        );
    }

    public ActionsProportionDtoList getAllActionsProportions() {

        return StmtProcessor.create(
                ActionsProportionDtoList.class,
                actionsProportionDtoList -> actionsProportionDtoList.setActionsProportionDtoList(
                        sumToActionsQuantityDao
                                .findAll()
                                .stream()
                                .map(ActionsProportionDto::toActionsProportionDto)
                                .collect(Collectors.toList())
                )
        );
    }

    public AccountsProportionDtoList getAllAccountsProportions() {

        return StmtProcessor.create(
                AccountsProportionDtoList.class,
                accountsProportionDtoList -> accountsProportionDtoList.setAccountsProportionDtoList(
                        discountToAccountQuantityDao
                                .findAll()
                                .stream()
                                .map(AccountsProportionDto::toAccountsProportionDto)
                                .collect(Collectors.toList())
                )
        );
    }

    public CustomPlanConstraintsDto getCustomPlanMaxConstraints() {

        return StmtProcessor.create(
                CustomPlanConstraintsDto.class,
                customPlanConstraintsDto -> customPlanConstraintsDto.setConstraintNameToValue(
                        constraintResolver.getMaxConstraints()
                )
        );
    }
}
