package org.dbs24.validator;

import lombok.Getter;
import org.dbs24.component.RefsService;
import org.dbs24.entity.dto.ModifyContractInfo;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Getter
@Component
public class ModifyContractValidator extends AbstractValidatorService<ModifyContractInfo> implements Validator<ModifyContractInfo> {

    final RefsService refsService;

    public ModifyContractValidator(RefsService refsService) {
        this.refsService = refsService;
    }

    //==========================================================================
    @Override
    public Collection<ErrorInfo> validate(ModifyContractInfo modifyContractInfo) {
        return StmtProcessor.<ErrorInfo>fillCollection(buildErrorCollection(),
                errors -> {

//                    ifNull(userContractsService, () -> userContractsService = ServiceLocator.findService(UserContractsService.class));
//
//                    // contractId
//                    ifNull(modifyContractInfo.getContractId(), () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, CONTRACT_ID, "ContractId not defined")));
//
//                    StmtProcessor.ifNotNull(modifyContractInfo.getContractId(), contractId ->
//                            ifTrue(userContractsService.findOptionalUserContract(contractId).isEmpty(),
//                                    () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, CONTRACT_ID, String.format("ContractId not found or not defined (contractId=%d)", contractId)))));
//
//                    // modifyReason
//                    ifNull(modifyContractInfo.getModifyReasonId(), () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, MODIFY_REASON, "Modify reason not defined")),
//                            () -> ifTrue(refsService.findOptionalModifyReason(modifyContractInfo.getModifyReasonId()).isEmpty(),
//                                    () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, MODIFY_REASON, String.format("Unknown modifyReason (%d)", modifyContractInfo.getModifyReasonId())))));
//
//                    // contractTypeId
//                    StmtProcessor.ifNotNull(modifyContractInfo.getContractTypeId(), () -> ifTrue(refsService.findOptionalContractType(modifyContractInfo.getContractTypeId()).isEmpty(),
//                            () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, CONTRACT_TYPE,
//                                    String.format("ContractTypeId not found or not defined (contractTypeId=%d)", modifyContractInfo.getContractTypeId())))));
//
//                    // editNote
//                    ifNull(modifyContractInfo.getEditNote(), () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, MODIFY_REASON_NOTE, "Edit note not defined")));
//
//                    // daysAmount
//                    StmtProcessor.ifNotNull(modifyContractInfo.getDaysAmount(),
//                            () -> ifTrue(modifyContractInfo.getDaysAmount().compareTo(INTEGER_ZERO) < INTEGER_ZERO,
//                                    () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, DAYS_AMOUNT, String.format("Invalid dayAmount value: %d ", modifyContractInfo.getDaysAmount())))));
//
//                    // subscriptionAmount
//                    StmtProcessor.ifNotNull(modifyContractInfo.getSubscriptionAmount(),
//                            () -> ifTrue(modifyContractInfo.getSubscriptionAmount().compareTo(INTEGER_ZERO) <= INTEGER_ZERO,
//                                    () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, SUBSRIPTION_AMOUNT, String.format("Invalid subscription amount value: %d ", modifyContractInfo.getSubscriptionAmount())))));

                });
    }
}
