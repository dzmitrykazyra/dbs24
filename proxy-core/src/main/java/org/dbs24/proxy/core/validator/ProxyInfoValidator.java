/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.core.validator;

import org.dbs24.proxy.core.component.ReferenceService;
import org.dbs24.proxy.core.entity.dto.BookProxiesDto;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.validator.*;
import org.dbs24.validator.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static org.dbs24.proxy.core.consts.ProxyConsts.ErrMsg.INVALID_FIELD_VALUE;

@Component
public class ProxyInfoValidator extends AbstractValidatorService<BookProxiesDto> implements EntityInfoValidator<BookProxiesDto> {

    private final ReferenceService referenceService;

    @Autowired
    public ProxyInfoValidator(ReferenceService referenceService) {
        this.referenceService = referenceService;
    }

    @Override
    public Collection<ErrorInfo> validate(BookProxiesDto proxyInfo) {
        return StmtProcessor.<ErrorInfo>fillCollection(buildErrorCollection(),
                errors -> {
                    /**
                     * PROXY PROVIDER NAME
                     */
                    final String providerName = proxyInfo.getProviderName();

                    if (referenceService.findProxyProviderOptionalByName(providerName).isEmpty() && isFieldNotMatchAny(providerName)) {
                        errors.add(buildErrorInfo(providerName, Field.PROXY_PROVIDER_NAME));
                    }

                    /**
                     * PROXY TYPE NAME
                     */
                    final String proxyTypeName = proxyInfo.getProxyTypeName();

                    if (referenceService.findProxyTypeOptionalByName(proxyTypeName).isEmpty() && isFieldNotMatchAny(proxyTypeName)) {
                        errors.add(buildErrorInfo(proxyTypeName, Field.PROXY_TYPE_NAME));
                    }

                    /**
                     * COUNTRY NAME
                     */
                    final String countryName = proxyInfo.getCountryName();

                    if (referenceService.findCountryOptional(countryName).isEmpty() && isFieldNotMatchAny(countryName)) {
                        errors.add(buildErrorInfo(countryName, Field.COUNTRY_NAME));
                    }

                    /**
                     * ALG ID
                     */
                    final Integer algorithmId = proxyInfo.getAlgorithmId();

                    if (algorithmId != null && referenceService.findAlgSelectionOptionalById(algorithmId).isEmpty()) {
                        errors.add(buildErrorInfo(String.valueOf(algorithmId), Field.ALG_ID));
                    }

                    /**
                     * AMOUNT
                     */
                    final Integer proxyAmount = proxyInfo.getAmount();

                    if (proxyAmount == null || proxyAmount < 0) {
                        errors.add(
                                ErrorInfo.create(Error.INVALID_ENTITY_ATTR, Field.PROXY_AMOUNT,
                                        INVALID_FIELD_VALUE.concat(String.format("%s - %d may not be null and >= 0",
                                                Field.PROXY_AMOUNT.getValue(), proxyAmount))
                                )
                        );
                    }
                });
    }

    private ErrorInfo buildErrorInfo(String fieldValue, Field field) {

        return ErrorInfo.create(Error.INVALID_ENTITY_ATTR,
                field,
                INVALID_FIELD_VALUE.concat(String.format("%s = {%s} does not exist", field.getValue(), fieldValue))
        );

    }

    private boolean isFieldNotMatchAny(String field) {
        return !"any".equals(field);
    }
}
