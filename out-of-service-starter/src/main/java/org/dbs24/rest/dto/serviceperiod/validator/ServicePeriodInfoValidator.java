/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.dto.serviceperiod.validator;

import org.dbs24.application.core.locale.NLS;
import org.dbs24.rest.dto.serviceperiod.ServicePeriodInfo;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.validator.AbstractValidatorService;
import org.dbs24.validator.EntityInfoValidator;
import org.dbs24.validator.ErrorInfo;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static org.dbs24.validator.Error.INVALID_ENTITY_ATTR;
import static org.dbs24.validator.Field.BEGIN_DATE;
import static org.dbs24.validator.Field.CONTRACT_ID;

@Component
public class ServicePeriodInfoValidator extends AbstractValidatorService<ServicePeriodInfo> implements EntityInfoValidator<ServicePeriodInfo> {

    //==========================================================================
    @Override
    public Collection<ErrorInfo> validate(ServicePeriodInfo servicePeriodInfo) {
        return StmtProcessor.<ErrorInfo>fillCollection(buildErrorCollection(),
                errors -> {

                    StmtProcessor.ifTrue(StmtProcessor.notNull(servicePeriodInfo.getServiceDateStart()) && StmtProcessor.notNull(servicePeriodInfo.getServiceDateFinish()),
                            () -> {

                                if (!(servicePeriodInfo.getServiceDateStart().compareTo(servicePeriodInfo.getServiceDateFinish()) < 0)) {
                                    errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, BEGIN_DATE, String.format("Invalid period interval (%s:%s)",
                                            NLS.long2LocalDateTime(servicePeriodInfo.getServiceDateStart()),
                                            NLS.long2LocalDateTime(servicePeriodInfo.getServiceDateFinish()
                                            ))));
                                }
                            });
                });
    }
}
