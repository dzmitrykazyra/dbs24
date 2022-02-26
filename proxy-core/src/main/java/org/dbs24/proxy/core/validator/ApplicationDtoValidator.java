package org.dbs24.proxy.core.validator;

import org.dbs24.proxy.core.dao.ApplicationDao;
import org.dbs24.proxy.core.dao.ApplicationNetworkDao;
import org.dbs24.proxy.core.entity.dto.ApplicationDto;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.validator.*;
import org.dbs24.validator.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static org.dbs24.proxy.core.consts.ProxyConsts.ErrMsg.INVALID_FIELD_VALUE;
import static org.dbs24.validator.Field.*;

@Component
public class ApplicationDtoValidator extends AbstractValidatorService<ApplicationDto> implements EntityInfoValidator<ApplicationDto> {

    private final ApplicationDao applicationDao;
    private final ApplicationNetworkDao applicationNetworkDao;

    @Autowired
    public ApplicationDtoValidator(ApplicationDao applicationDao, ApplicationNetworkDao applicationNetworkDao) {
        this.applicationDao = applicationDao;
        this.applicationNetworkDao = applicationNetworkDao;
    }


    @Override
    public Collection<ErrorInfo> validate(ApplicationDto applicationDto) {
        return StmtProcessor.<ErrorInfo>fillCollection(buildErrorCollection(),
                errors -> {
                    /**
                     * APPLICATION STATUS ID
                     */
                    final Integer applicationStatusId = applicationDto.getApplicationStatusId();

                    if (applicationDao.findApplicationStatusOptionalById(applicationStatusId).isEmpty()) {
                        errors.add(buildErrorInfo(APPLICATION_STATUS_ID, String.valueOf(applicationStatusId), "not exist"));
                    }

                    /**
                     * APPLICATION ID
                     */
                    final Integer applicationId = applicationDto.getApplicationId();

                    if(applicationId != null && applicationDao.findOptionalByApplicationId(applicationId).isEmpty()) {
                        errors.add(buildErrorInfo(APPLICATION_ID, String.valueOf(applicationId), "not exist"));
                    }

                    /**
                     * APPLICATION NAME
                     */
                    final String applicationName = applicationDto.getName();

                    if (applicationId == null && applicationDao.findOptionalByApplicationName(applicationName).isPresent()) {
                        errors.add(buildErrorInfo(APPLICATION_NAME, applicationName, "already exist"));
                    }

                    /**
                     * APPLICATION NETWORK NAME
                     */
                    final String networkName = applicationDto.getApplicationNetworkName();

                    if (networkName == null || applicationNetworkDao.findApplicationNetworkOptionalByName(networkName).isEmpty()) {
                        errors.add(buildErrorInfo(APPLICATION_NETWORK_NAME, networkName, "does not exist"));
                    }
                });
    }

    private ErrorInfo buildErrorInfo(Field field, String fieldValue, String errorMessage) {

        return ErrorInfo.create(Error.INVALID_ENTITY_ATTR,
                field,
                INVALID_FIELD_VALUE.concat(String.format("%s = {%s} %s", field.getValue(), fieldValue, errorMessage))
        );
    }
}