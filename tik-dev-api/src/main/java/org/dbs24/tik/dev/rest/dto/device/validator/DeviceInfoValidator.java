/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.rest.dto.device.validator;

import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.dev.rest.dto.device.DeviceInfo;
import org.dbs24.tik.dev.service.RefsService;
import org.dbs24.validator.AbstractValidatorService;
import org.dbs24.validator.EntityInfoValidator;
import org.dbs24.validator.ErrorInfo;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class DeviceInfoValidator extends AbstractValidatorService<DeviceInfo> implements EntityInfoValidator<DeviceInfo> {

    final RefsService refsService;

    public DeviceInfoValidator(RefsService refsService) {
        this.refsService = refsService;
    }

    //==========================================================================
    @Override
    public Collection<ErrorInfo> validate(DeviceInfo botInfo) {
        return StmtProcessor.<ErrorInfo>fillCollection(buildErrorCollection(),
                errors -> {
//
//                    // algId
//                    final Integer infoAlgId = proxyRequestInfo.getAlgId();
//
//                    StmtProcessor.ifNull(infoAlgId, ()
//                            -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, ALG_ID, FIELD_NOT_FOUND.concat(ALG_ID.getValue())))
//                    );
//
//                    StmtProcessor.ifNotNull(infoAlgId, algId -> {
//
//                        if (!AlgSelectionEnum.PROXY_ALG_SELECTIONS_IDS.contains(algId)) {
//                            errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, ALG_ID, INVALID_FIELD_VALUE.concat(String.format(" %s: '%d'", ALG_ID.getValue(), algId))));
//                        }
//                    });
//
//                    // proxyId
//                    final Integer infoProxyId = proxyRequestInfo.getProxyId();
//                    StmtProcessor.ifNotNull(infoProxyId, proxyId -> {
//
//                        final Proxy proxy = proxiesService.findProxyOptional(proxyId).orElseGet(() -> null);
//
//                        if (StmtProcessor.isNull(proxy)) {
//                            errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, PROXY_ID, INVALID_FIELD_VALUE.concat(String.format(" %s: '%d'", PROXY_ID.getValue(), proxyId))));
//                        }
//                    });
//
//                    // countryId
//                    final Integer infoCountryId = proxyRequestInfo.getCountryId();
//                    StmtProcessor.ifNotNull(infoCountryId, countryId -> {
//
//                        final Country country = refsService.findCountryOptional(countryId).orElseGet(() -> null);
//
//                        if (StmtProcessor.isNull(country)) {
//                            errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, COUNTRY_ID, INVALID_FIELD_VALUE.concat(String.format(" %s: '%d'", COUNTRY_ID.getValue(), countryId))));
//                        }
//                    });
//
//                    // proxyProviderId
//                    final Integer infoProxyProviderId = proxyRequestInfo.getProxyProviderId();
//                    StmtProcessor.ifNotNull(infoProxyProviderId, proxyProviderId -> {
//
//                        final ProxyProvider proxyProvider = refsService.findProxyProviderOptonal(proxyProviderId).orElseGet(() -> null);
//
//                        if (StmtProcessor.isNull(proxyProvider)) {
//                            errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, PROXY_PROVIDER_ID, INVALID_FIELD_VALUE.concat(String.format(" %s: '%d'", PROXY_PROVIDER_ID.getValue(), proxyProviderId))));
//                        }
//                    });
                });
    }
}
