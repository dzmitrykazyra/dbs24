/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.validator;

import org.dbs24.component.RefsService;
import org.dbs24.rest.api.FutureTrialUserContractsInfo;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class FutureTrialContractValidator extends AbstractValidatorService<FutureTrialUserContractsInfo> implements Validator<FutureTrialUserContractsInfo> {

    final RefsService refsService;

    public FutureTrialContractValidator(RefsService refsService) {
        this.refsService = refsService;
    }

    //==========================================================================
    @Override
    public Collection<ErrorInfo> validate(FutureTrialUserContractsInfo futureUserContractInfo) {
        return StmtProcessor.<ErrorInfo>fillCollection(buildErrorCollection(),
                errors -> {

//                    final UserContractsService userContractsService = ServiceLocator.findService(UserContractsService.class);
//
//                    // loginToken
//                    final Optional<User> optUser = Optional.ofNullable(futureUserContractInfo.getLoginToken()).map(loginToken -> usersService.findOptionalUserByLoginToken(loginToken)).orElseGet(Optional::empty);
//
//                    ifTrue(optUser.isEmpty(), () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, LOGIN_TOKEN, String.format("login token is empty or not found  - '%s'", futureUserContractInfo.getLoginToken()))));
//
//                    ifNull(futureUserContractInfo.getContractId(), ()
//                            -> optUser.ifPresent(user
//                            -> // findActualContract
//                            userContractsService.findActualUserContracts(user)
//                                    .stream()
//                                    .findFirst()
//                                    .ifPresent(contract -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, END_DATE, String.format("There is actual contract (contract_id=%d) with endDate = (%s)",
//                                            contract.getContractId(), contract.getEndDate().toString()))))
//                    ));
//
//                    StmtProcessor.ifNotNull(futureUserContractInfo.getContractId(), () -> {
//
//                        try {
//                            userContractsService.findUserContract(futureUserContractInfo.getContractId());
//                        } catch (Throwable throwable) {
//                            errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, CONTRACT_ID, throwable.getMessage()));
//                        }
//                    });
//
//                    ifNull(futureUserContractInfo.getDeviceTypeId(), () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, PLATFORM, "DeviceTypeId is not specified")));

                });
    }
}
