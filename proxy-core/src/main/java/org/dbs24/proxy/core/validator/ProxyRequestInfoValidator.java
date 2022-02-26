/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.core.validator;

import org.dbs24.proxy.core.component.ProxyService;
import org.dbs24.proxy.core.component.ReferenceService;
import org.dbs24.proxy.core.consts.ProxyConsts.AlgSelectionEnum;
import org.dbs24.proxy.core.entity.domain.Country;
import org.dbs24.proxy.core.entity.domain.Proxy;
import org.dbs24.proxy.core.entity.domain.ProxyProvider;
import org.dbs24.proxy.core.rest.api.request.ProxyRequestInfo;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.validator.AbstractValidatorService;
import org.dbs24.validator.EntityInfoValidator;
import org.dbs24.validator.ErrorInfo;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static org.dbs24.proxy.core.consts.ProxyConsts.ErrMsg.FIELD_NOT_FOUND;
import static org.dbs24.proxy.core.consts.ProxyConsts.ErrMsg.INVALID_FIELD_VALUE;
import static org.dbs24.validator.Error.INVALID_ENTITY_ATTR;
import static org.dbs24.validator.Field.*;

@Component
public class ProxyRequestInfoValidator extends AbstractValidatorService<ProxyRequestInfo> implements EntityInfoValidator<ProxyRequestInfo> {

    final ProxyService proxyService;
    final ReferenceService referenceService;

    public ProxyRequestInfoValidator(ProxyService proxyService, ReferenceService referenceService) {
        this.proxyService = proxyService;
        this.referenceService = referenceService;
    }

    //==========================================================================
    @Override
    public Collection<ErrorInfo> validate(ProxyRequestInfo proxyRequestInfo) {
        return StmtProcessor.<ErrorInfo>fillCollection(buildErrorCollection(),
                errors -> {

                    // algId
                    final Integer infoAlgId = proxyRequestInfo.getAlgId();

                    StmtProcessor.ifNull(infoAlgId, ()
                            -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, ALG_ID, FIELD_NOT_FOUND.concat(ALG_ID.getValue())))
                    );

                    StmtProcessor.ifNotNull(infoAlgId, algId -> {

                        if (!AlgSelectionEnum.PROXY_ALG_SELECTIONS_IDS.contains(algId)) {
                            errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, ALG_ID, INVALID_FIELD_VALUE.concat(String.format(" %s: '%d'", ALG_ID.getValue(), algId))));
                        }
                    });

                    // proxyId
                    final Integer infoProxyId = proxyRequestInfo.getProxyId();
                    StmtProcessor.ifNotNull(infoProxyId, proxyId -> {

                        final Proxy proxy = proxyService.findProxyOptional(proxyId).orElseGet(() -> null);

                        if (StmtProcessor.isNull(proxy)) {
                            errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, PROXY_ID, INVALID_FIELD_VALUE.concat(String.format(" %s: '%d'", PROXY_ID.getValue(), proxyId))));
                        }
                    });

                    // countryId
                    final Integer infoCountryId = proxyRequestInfo.getCountryId();
                    StmtProcessor.ifNotNull(infoCountryId, countryId -> {

                        final Country country = referenceService.findCountryOptional(countryId).orElseGet(() -> null);

                        if (StmtProcessor.isNull(country)) {
                            errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, COUNTRY_NAME, INVALID_FIELD_VALUE.concat(String.format(" %s: '%d'", COUNTRY_NAME.getValue(), countryId))));
                        }
                    });

                    // proxyProviderId
                    final Integer infoProxyProviderId = proxyRequestInfo.getProxyProviderId();
                    StmtProcessor.ifNotNull(infoProxyProviderId, proxyProviderId -> {

                        final ProxyProvider proxyProvider = referenceService.findProxyProviderOptional(proxyProviderId).orElseGet(() -> null);

                        if (StmtProcessor.isNull(proxyProvider)) {
                            errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, PROXY_PROVIDER_NAME, INVALID_FIELD_VALUE.concat(String.format(" %s: '%d'", PROXY_PROVIDER_NAME.getValue(), proxyProviderId))));
                        }
                    });
                });
    }
}
