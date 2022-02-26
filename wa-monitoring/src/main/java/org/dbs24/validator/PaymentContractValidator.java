/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.validator;

import org.dbs24.component.RefsService;
import org.dbs24.component.UsersService;
import org.dbs24.consts.SysConst;
import org.dbs24.entity.User;
import org.dbs24.rest.api.UserContractFromPaymentInfo;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

import static org.dbs24.consts.WaConsts.References.ALL_CONTRACT_TYPES_IDS;
import static org.dbs24.stmt.StmtProcessor.ifNull;
import static org.dbs24.stmt.StmtProcessor.ifTrue;
import static org.dbs24.validator.Error.INVALID_ENTITY_ATTR;
import static org.dbs24.validator.Field.*;

@Component
public class PaymentContractValidator extends AbstractValidatorService<UserContractFromPaymentInfo> implements Validator<UserContractFromPaymentInfo> {

    final UsersService usersService;
    final RefsService refsService;

    public PaymentContractValidator(UsersService usersService, RefsService refsService) {
        this.usersService = usersService;
        this.refsService = refsService;
    }

    //==========================================================================
    @Override
    public Collection<ErrorInfo> validate(UserContractFromPaymentInfo userContractFromPaymentInfo) {
        return StmtProcessor.<ErrorInfo>fillCollection(buildErrorCollection(),
                errors -> {

                    // loginToken
                    final Optional<User> user = Optional.ofNullable(userContractFromPaymentInfo.getLoginToken()).map(loginToken -> usersService.findOptionalUserByLoginToken(loginToken)).orElseGet(Optional::empty);

                    ifTrue(user.isEmpty(), () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, LOGIN_TOKEN, String.format("login token is empty or not found  - '%s'", userContractFromPaymentInfo.getLoginToken()))));

                    // contract type id
                    ifNull(userContractFromPaymentInfo.getContractTypeId(), () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, CONTRACT_TYPE, "contract type is empty or not defined")),
                            () -> ALL_CONTRACT_TYPES_IDS
                                    .stream()
                                    .filter(id -> id.equals(userContractFromPaymentInfo.getContractTypeId()))
                                    .findAny()
                                    .orElseGet(() -> {

                                        errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, CONTRACT_TYPE, String.format("invalid or unknown contract type id: %d", userContractFromPaymentInfo.getContractTypeId())));

                                        return SysConst.INTEGER_ZERO;
                                    }));

                    // subscription amount
                    ifNull(userContractFromPaymentInfo.getSubscriptionsAmount(), () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, SUBSRIPTION_AMOUNT, "subscription amount is empty or not defined")));

                    // beginDate
                    ifNull(userContractFromPaymentInfo.getBeginDate(), () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, BEGIN_DATE, "beginDate is empty or not defined")));

                    // endDate
                    ifNull(userContractFromPaymentInfo.getEndDate(), () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, END_DATE, "endDate is empty or not defined")));

                });
    }
}
