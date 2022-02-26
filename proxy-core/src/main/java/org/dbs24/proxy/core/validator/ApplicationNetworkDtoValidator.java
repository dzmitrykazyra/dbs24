package org.dbs24.proxy.core.validator;

import org.dbs24.proxy.core.entity.dto.ApplicationNetworkDto;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.validator.AbstractValidatorService;
import org.dbs24.validator.EntityInfoValidator;
import org.dbs24.validator.ErrorInfo;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class ApplicationNetworkDtoValidator extends AbstractValidatorService<ApplicationNetworkDto>
        implements EntityInfoValidator<ApplicationNetworkDto> {

    @Override
    public Collection<ErrorInfo> validate(ApplicationNetworkDto applicationNetworkDto) {
        return StmtProcessor.<ErrorInfo>fillCollection(buildErrorCollection(),
                errors -> {
                    //todo
//                    errors.add(ErrorInfo.create(ErrorType.INVALID_ENTITY_ATTR, "fld1", "testErrMsg"));
//                    errors.add(ErrorInfo.create(ErrorType.INVALID_ENTITY_ATTR, "fld2", "testErrMsg"));
                });
    }
}
