package org.dbs24.validator;


import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.component.RefsService;
import org.dbs24.entity.ContractType;
import org.dbs24.entity.TariffPlanStatus;
import org.dbs24.rest.api.TariffPlanInfo;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import static org.dbs24.stmt.StmtProcessor.ifNull;
import static org.dbs24.stmt.StmtProcessor.ifTrue;
import static org.dbs24.validator.Error.INVALID_ENTITY_ATTR;
import static org.dbs24.validator.Field.*;

@Component
@Log4j2
public class TariffPlanValidator extends AbstractValidatorService<TariffPlanInfo> implements Validator<TariffPlanInfo> {

    @Value("${config.wa.tariff.max-duration:2001}")
    private Integer maxDuration;

    @Value("${config.wa.tariff.max-subs-amount:201}")
    private Integer maxSubs;

    final RefsService refsService;

    public TariffPlanValidator(RefsService refsService) {
        this.refsService = refsService;
    }

    @Override
    protected void assignDefaults(TariffPlanInfo tariffPlanInfo) {

        ifNull(tariffPlanInfo.getActualDate(), () -> {
            final LocalDateTime ldtNow = LocalDateTime.now();
            log.debug("{}: assign defaults actualDate: {}", getClass().getSimpleName(), ldtNow);
            tariffPlanInfo.setActualDate(NLS.localDateTime2long(ldtNow));
        });
    }

    @Override
    public Collection<ErrorInfo> validate(TariffPlanInfo tariffPlanInfo) {
        return StmtProcessor.<ErrorInfo>fillCollection(buildErrorCollection(),
                errors -> {

                    final Optional<TariffPlanStatus> optionalTariffPlanStatus = Optional.ofNullable(tariffPlanInfo.getTariffPlanStatusId()).map(statusId -> refsService.findOptionalTariffPlanStatus(statusId)).orElseGet(Optional::empty);
                    ifTrue(optionalTariffPlanStatus.isEmpty(),
                            () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, TARIFF_PLAN_STATUS, String.format("unknown tariff plan status: '%d'", tariffPlanInfo.getTariffPlanStatusId()))));

                    final Optional<ContractType> contractTypeOptional = Optional.ofNullable(tariffPlanInfo.getContractTypeId()).map(contractTypeid -> refsService.findOptionalContractType(contractTypeid)).orElseGet(Optional::empty);
                    ifTrue(contractTypeOptional.isEmpty(),
                            () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, TARIFF_CONTRACT_TYPE, String.format("unknown tariff contract type: '%d'", tariffPlanInfo.getContractTypeId()))));

                    ifNull(tariffPlanInfo.getSubscriptionsAmount(),
                            () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, TARIFF_SUBSCRIPTION_AMOUNT, "Subscription amount not defined")),
                            () -> ifTrue(!(tariffPlanInfo.getSubscriptionsAmount() > 0 && tariffPlanInfo.getSubscriptionsAmount() <= maxSubs),
                                    () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, TARIFF_SUBSCRIPTION_AMOUNT,
                                            String.format("Illegal Subscription amount - %d: (maximum %d subsAmount are allowed) ", tariffPlanInfo.getSubscriptionsAmount(), maxSubs)))));

                    ifNull(tariffPlanInfo.getDurationHours(),
                            () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, TARIFF_DURATION, "duration not defined")),
                            () -> ifTrue(!(tariffPlanInfo.getDurationHours() > 0 && tariffPlanInfo.getDurationHours() <= maxDuration),
                                    () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, TARIFF_DURATION,
                                            String.format("Illegal duration days - %d  (maximum %d days are allowed) ", tariffPlanInfo.getDurationHours(), maxDuration)))));

//                    ifNull(tariffPlanInfo.getSku(),
//                            () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, TARIFF_DURATION, "sku not defined")),
//                            () -> ifTrue(tariffPlanInfo.getSku().isEmpty(), () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, TARIFF_DURATION, "sku is empty"))));

                    StmtProcessor.ifNotNull(tariffPlanInfo.getDeviceTypeId(),
                            dt -> refsService.findOptionalDeviceType(dt)
                                    .orElseGet(() -> {
                                        errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, TARIFF_DEVICE_TYPE,
                                                String.format("unknown device type: '%d'", tariffPlanInfo.getDeviceTypeId())));
                                        return null;
                                    }));

                });
    }
}
