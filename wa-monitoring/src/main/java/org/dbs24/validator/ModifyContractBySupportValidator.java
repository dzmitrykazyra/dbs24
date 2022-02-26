package org.dbs24.validator;

import lombok.Getter;
import org.dbs24.component.RefsService;
import org.dbs24.component.UserContractsService;
import org.dbs24.entity.dto.ModifyContractBySupportInfo;
import org.dbs24.spring.core.api.ServiceLocator;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static org.dbs24.consts.SysConst.INTEGER_ZERO;
import static org.dbs24.stmt.StmtProcessor.ifNull;
import static org.dbs24.stmt.StmtProcessor.ifTrue;
import static org.dbs24.validator.Error.INVALID_ENTITY_ATTR;
import static org.dbs24.validator.Field.*;

@Getter
@Component
public class ModifyContractBySupportValidator extends AbstractValidatorService<ModifyContractBySupportInfo> implements Validator<ModifyContractBySupportInfo> {

    private UserContractsService userContractsService;
    final RefsService refsService;

    public ModifyContractBySupportValidator(RefsService refsService) {
        this.refsService = refsService;
    }

    //==========================================================================
    @Override
    public Collection<ErrorInfo> validate(ModifyContractBySupportInfo modifyContractBySupportInfo) {
        return StmtProcessor.<ErrorInfo>fillCollection(buildErrorCollection(),
                errors -> {

                    ifNull(userContractsService, () -> userContractsService = ServiceLocator.findService(UserContractsService.class));

                    // modifyReason
                    ifNull(modifyContractBySupportInfo.getModifyReasonId(), () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, MODIFY_REASON, "Modify reason not defined")),
                            () -> ifTrue(refsService.findOptionalModifyReason(modifyContractBySupportInfo.getModifyReasonId()).isEmpty(),
                                    () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, MODIFY_REASON, String.format("Unknown modifyReason (%d)", modifyContractBySupportInfo.getModifyReasonId())))));

                    // contractTypeId
                    StmtProcessor.ifNotNull(modifyContractBySupportInfo.getContractTypeId(), () -> ifTrue(refsService.findOptionalContractType(modifyContractBySupportInfo.getContractTypeId()).isEmpty(),
                            () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, CONTRACT_TYPE,
                                    String.format("ContractTypeId not found or not defined (contractTypeId=%d)", modifyContractBySupportInfo.getContractTypeId())))));

                    // editNote
                    ifNull(modifyContractBySupportInfo.getEditNote(), () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, MODIFY_REASON_NOTE, "Edit note not defined")));

                    // daysAmount
                    StmtProcessor.ifNotNull(modifyContractBySupportInfo.getDaysAmount(),
                            () -> ifTrue(modifyContractBySupportInfo.getDaysAmount().compareTo(INTEGER_ZERO) <= INTEGER_ZERO,
                                    () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, DAYS_AMOUNT, String.format("Invalid days_amount value: %d ", modifyContractBySupportInfo.getDaysAmount())))));

                    // subscriptionAmount
//                    StmtProcessor.ifNotNull(modifyContractBySupportInfo.getSubscriptionAmount(),
//                            () -> ifTrue(modifyContractBySupportInfo.getSubscriptionAmount().compareTo(INTEGER_ZERO) <= INTEGER_ZERO,
//                                    () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, SUBSRIPTION_AMOUNT, String.format("Invalid subscription amount value: %d ", modifyContractBySupportInfo.getSubscriptionAmount())))));

                });
    }
}
