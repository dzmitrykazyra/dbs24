/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.core.component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.proxy.core.dao.ReferenceDao;
import org.dbs24.proxy.core.entity.domain.*;
import org.dbs24.proxy.core.entity.dto.SingleValueListDto;
import org.dbs24.proxy.core.entity.dto.refapi.CountryDto;
import org.dbs24.proxy.core.entity.dto.refapi.CountryListDto;
import org.dbs24.proxy.core.entity.dto.response.SingleValuesDtoResponse;
import org.dbs24.rest.api.consts.RestApiConst;
import org.dbs24.spring.core.api.AbstractApplicationService;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.dbs24.proxy.core.consts.ProxyConsts.ProxyStatusEnum;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;

@Data
@Log4j2
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "refs")
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "proxy-core")
public class ReferenceService extends AbstractApplicationService {

    final ReferenceDao referenceDao;

    private Map<Integer, String> currencies;

    @Value("${refs.api.host}")
    private String refBookUrl;
    @Value("${refs.api.endpoint.countries}")
    private String countriesEndpointUrl;

    private WebClient webClient;

    public ReferenceService(ReferenceDao referenceDao) {

        this.referenceDao = referenceDao;
    }

    @PostConstruct
    public void webClientInit() {

        webClient = WebClient.builder()
                .baseUrl(refBookUrl)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build();
    }

    public void refreshCountriesRepo() {

        webClient.get()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(countriesEndpointUrl)
                        .build())
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CountryListDto.class)
                .subscribe(
                        countryListDto -> {
                            log.info("GOT COUNTRIES LIST FROM REF API : {}", countryListDto.getCountryDtoList().size());

                            referenceDao
                                    .saveCountries(
                                            countryListDto
                                                    .getCountryDtoList()
                                                    .stream()
                                                    .map(CountryDto::toCountry)
                                                    .collect(Collectors.toList())
                                    );
                        }
                );
    }

    public SingleValuesDtoResponse getProxyTypes() {

        return StmtProcessor.create(
                SingleValuesDtoResponse.class,
                singleValuesDtoResponse -> {
                    setSuccessAttributesToResponse(singleValuesDtoResponse);
                    singleValuesDtoResponse.setCreatedEntity(
                            StmtProcessor.create(
                                    SingleValueListDto.class,
                                    singleValueListDto -> singleValueListDto
                                            .setReferenceValues(
                                                    referenceDao
                                                            .findAllProxyTypes()
                                                            .stream()
                                                            .map(ProxyType::getProxyTypeName)
                                                            .collect(Collectors.toList())
                                            )
                            )
                    );
                }
        );
    }

    public SingleValuesDtoResponse getProxyProviders() {

        return StmtProcessor.create(
                SingleValuesDtoResponse.class,
                singleValuesDtoResponse -> {
                    setSuccessAttributesToResponse(singleValuesDtoResponse);
                    singleValuesDtoResponse.setCreatedEntity(
                            StmtProcessor.create(
                                    SingleValueListDto.class,
                                    singleValueListDto -> singleValueListDto
                                            .setReferenceValues(
                                                    referenceDao
                                                            .findAllProxyProviders()
                                                            .stream()
                                                            .map(ProxyProvider::getProviderName)
                                                            .collect(Collectors.toList())
                                            )
                            )
                    );
                }
        );
    }

    public SingleValuesDtoResponse getCountries() {

        return StmtProcessor.create(
                SingleValuesDtoResponse.class,
                singleValuesDtoResponse -> {
                    setSuccessAttributesToResponse(singleValuesDtoResponse);
                    singleValuesDtoResponse.setCreatedEntity(
                            StmtProcessor.create(
                                    SingleValueListDto.class,
                                    singleValueListDto -> singleValueListDto
                                            .setReferenceValues(
                                                    referenceDao
                                                            .findAllCountries()
                                                            .stream()
                                                            .map(Country::getCountryName)
                                                            .collect(Collectors.toList())
                                            )
                            )
                    );
                }
        );
    }

    public SingleValuesDtoResponse getAlgorithmSelections() {

        return StmtProcessor.create(
                SingleValuesDtoResponse.class,
                singleValuesDtoResponse -> {
                    setSuccessAttributesToResponse(singleValuesDtoResponse);
                    singleValuesDtoResponse.setCreatedEntity(
                            StmtProcessor.create(
                                    SingleValueListDto.class,
                                    singleValueListDto -> singleValueListDto
                                            .setReferenceValues(
                                                    referenceDao
                                                            .findAllAlgSelections()
                                                            .stream()
                                                            .map(AlgSelection::getAlgName)
                                                            .collect(Collectors.toList())
                                            )
                            )
                    );
                }
        );
    }

    private void setSuccessAttributesToResponse(SingleValuesDtoResponse response) {

        response.setCode(RestApiConst.RestOperCode.OC_OK);
        response.setMessage("Successfully founded required reference names");
        response.setErrors(ServiceFuncs.createCollection());
        response.complete();
    }

    public ProxyStatus findProxyStatus(ProxyStatusEnum proxyStatus) {

        return findProxyStatus(proxyStatus.getCode());
    }

    public ProxyStatus findProxyStatus(Integer proxyStatusId) {

        return referenceDao
                .findProxyStatusById(proxyStatusId)
                .orElseThrow(() -> new RuntimeException("ProxyStatus is unknown or not found"));
    }

    public Country findCountry(Integer countryId) {

        return referenceDao
                .findCountryById(countryId)
                .orElseThrow(() -> new RuntimeException("Country is unknown or not found"));
    }

    public Country findCountry(String countryName) {

        return referenceDao
                .findCountryByName(countryName)
                .orElseThrow(() -> new RuntimeException("Country is unknown or not found"));
    }

    public Country findCountryByIso(String countryIso) {

        return referenceDao
                .findCountryByIso(countryIso)
                .orElse(null);
    }

    public Optional<Country> findCountryOptional(String countryName) {

        return referenceDao
                .findCountryByName(countryName);
    }

    public Optional<Country> findCountryOptional(Integer countryId) {

        return referenceDao
                .findCountryById(countryId);
    }

    public ProxyType findProxyType(Integer proxyTypeId) {

        return referenceDao
                .findProxyTypeById(proxyTypeId)
                .orElseThrow(() -> new RuntimeException("ProxyType is unknown or not found"));
    }

    public Optional<ProxyType> findProxyTypeOptionalByName(String proxyTypeName) {

        return referenceDao.findProxyTypeByName(proxyTypeName);
    }

    public AlgSelection findAlgSelection(Integer algSelectionId) {

        return referenceDao
                .findAlgSelectionById(algSelectionId)
                .orElseThrow(() -> new RuntimeException("AlgSelection is unknown or not found"));
    }

    public Optional<AlgSelection> findAlgSelectionOptionalById(Integer algorithmId) {

        return referenceDao.findAlgSelectionById(algorithmId);
    }

    public ProxyProvider findProxyProvider(Integer proxyProviderId) {

        return referenceDao
                .findProxyProviderById(proxyProviderId)
                .orElseThrow(() -> new RuntimeException("ProxyProvider is unknown or not found"));
    }

    public Optional<ProxyProvider> findProxyProviderOptional(Integer proxyProviderId) {

        return referenceDao
                .findProxyProviderById(proxyProviderId);
    }

    public Optional<ProxyProvider> findProxyProviderOptionalByName(String providerName) {

        return referenceDao.findProxyProviderByName(providerName);
    }

    public ProxyProvider findProxyProviderByName(String providerName) {

        return referenceDao.findProxyProviderByNameIgnoreCase(providerName)
                .orElseThrow(() -> new RuntimeException(
                                String.format("Cannot find proxy provider with name = %s", providerName)
                        )
                );
    }

    public SocksAuthMethod findSocksAuthMethodById(Integer socksAuthMethodId) {

        return referenceDao.findSocksAuthMethodById(socksAuthMethodId).orElseThrow(() -> new RuntimeException("SocksAuthMethod is unknown or not found"));
    }

    @Transactional
    public void synchronizeRefs() {

        refreshCountriesRepo();

        referenceDao.saveAllReferences();
    }
}
