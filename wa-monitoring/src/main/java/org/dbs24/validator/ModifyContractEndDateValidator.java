package org.dbs24.validator;

import lombok.Getter;
import org.dbs24.component.RefsService;
import org.dbs24.component.UserContractsService;
import org.dbs24.entity.dto.ModifyContractEndDateInfo;
import org.dbs24.spring.core.api.ServiceLocator;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static org.dbs24.consts.SysConst.INTEGER_ZERO;
import static org.dbs24.stmt.StmtProcessor.ifNull;
import static org.dbs24.stmt.StmtProcessor.ifTrue;
import static org.dbs24.validator.Error.INVALID_ENTITY_ATTR;
import static org.dbs24.validator.Field.DAYS_AMOUNT;
import static org.dbs24.validator.Field.LOGIN_TOKEN;

@Getter
@Component
public class ModifyContractEndDateValidator extends AbstractValidatorService<ModifyContractEndDateInfo> implements Validator<ModifyContractEndDateInfo> {

    private UserContractsService userContractsService;
    final RefsService refsService;

    public ModifyContractEndDateValidator(RefsService refsService) {
        this.refsService = refsService;
    }

    //==========================================================================
    @Override
    public Collection<ErrorInfo> validate(ModifyContractEndDateInfo modifyContractEndDateInfo) {
        return StmtProcessor.<ErrorInfo>fillCollection(buildErrorCollection(),
                errors -> {

                    ifNull(userContractsService, () -> userContractsService = ServiceLocator.findService(UserContractsService.class));

                    try {
                        // LoginToken
                        ifNull(modifyContractEndDateInfo.getLoginToken(), () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, LOGIN_TOKEN, "loginToken not defined")),
                                () -> ifTrue(userContractsService.findAllUserContracts(modifyContractEndDateInfo.getLoginToken())
                                                .stream()
                                                .max((a, b) -> a.getEndDate().compareTo(b.getEndDate())).isEmpty(),
                                        () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, LOGIN_TOKEN, String.format("No contracts found with applied loginToken (%s) ", modifyContractEndDateInfo.getLoginToken())))));
                    } catch (Throwable throwable) {
                        errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, LOGIN_TOKEN, String.format("No contracts found with applied loginToken (%s): %s ", modifyContractEndDateInfo.getLoginToken(), throwable.getMessage())));
                    }

                    // daysAmount
                    StmtProcessor.ifNotNull(modifyContractEndDateInfo.getDaysAmount(),
                            () -> ifTrue(modifyContractEndDateInfo.getDaysAmount().compareTo(INTEGER_ZERO) <= INTEGER_ZERO,
                                    () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, DAYS_AMOUNT, String.format("Invalid daysAmount value: %d ", modifyContractEndDateInfo.getDaysAmount())))));

                });
    }
}
