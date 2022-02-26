/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.card.pmt.component;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.card.pmt.repo.*;
import org.dbs24.card.pmt.entity.*;
import static org.dbs24.card.pmt.consts.CardPaymentConsts.Countries.*;
import static org.dbs24.card.pmt.consts.CardPaymentConsts.Currencies.*;
import static org.dbs24.card.pmt.consts.CardPaymentConsts.ApplicationEnum.*;
import static org.dbs24.card.pmt.consts.CardPaymentConsts.PaymentServiceEnum.*;
import static org.dbs24.card.pmt.consts.CardPaymentConsts.PaymentStatusEnum.*;
import static org.dbs24.card.pmt.consts.CardPaymentConsts.Caches.*;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Data
@Log4j2
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "refs")
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "card-pmt")
public class RefsService extends AbstractApplicationService {

    final CountryRepo countryRepo;
    final CurrencyRepo currencyRepo;
    final ApplicationRepo applicationRepo;
    final PaymentServiceRepo paymentServiceRepo;
    final PaymentStatusRepo paymentStatusRepo;

    private Map<Integer, String> countries;
    private Map<Integer, String> currencies;

    public RefsService(CountryRepo countryRepo, CurrencyRepo currencyRepo, ApplicationRepo applicationRepo, PaymentServiceRepo paymentServiceRepo, PaymentStatusRepo paymentStatusRepo) {
        this.countryRepo = countryRepo;
        this.currencyRepo = currencyRepo;
        this.applicationRepo = applicationRepo;
        this.paymentServiceRepo = paymentServiceRepo;
        this.paymentStatusRepo = paymentStatusRepo;
    }
//
    //==========================================================================

    @Cacheable(CACHE_COUNTRY)
    public Country findCountry(String countyIso) {

        return countryRepo.findById(countyIso).orElseThrow(() -> new RuntimeException("Country is unknown or not found - " + countyIso));
    }

    @Cacheable(CACHE_APPLICATION)
    public Application findApplication(Integer applicationId) {

        return applicationRepo.findById(applicationId).orElseThrow(() -> new RuntimeException("Application is unknown or not found - " + applicationId));
    }

    
    @Cacheable(CACHE_CURRENCIES)
    public Currency findCurrency(String currencyIso) {

        return currencyRepo.findById(currencyIso).orElseThrow(() -> new RuntimeException("Currency is unknown or not found - " + currencyIso));
    }    
    
    @Cacheable(CACHE_PAYMENT_SERVICE)
    public PaymentService findPaymentService(Integer paymentServiceId) {

        return paymentServiceRepo.findById(paymentServiceId).orElseThrow(() -> new RuntimeException("PaymentService is unknown or not found - " + paymentServiceId));
    } 
    
    @Cacheable(CACHE_PAYMENT_STATUS)
    public PaymentStatus findPaymentStatus(Integer paymentStatusId) {

        return paymentStatusRepo.findById(paymentStatusId).orElseThrow(() -> new RuntimeException("PaymentStatus is unknown or not found - " + paymentStatusId));
    }     
//
//    @Cacheable(CACHE_COUNTRY)
//    public Country findCountry(Integer countryId) {
//
//        return countryRepo.findById(countryId).orElseThrow(() -> new RuntimeException("Country is unknown or not found - " + countryId.toString()));
//    }
//
//    @Cacheable(CACHE_COUNTRY_NAME)
//    public Country findCountry(String countryName) {
//
//        return countryRepo.findByCountryName(countryName).orElseThrow(() -> new RuntimeException("Country is unknown or not found - " + countryName.toString()));
//    }
//
//    public Optional<Country> findCountryOptional(String countryName) {
//        return countryRepo.findByCountryName(countryName);
//    }
//
//    public Optional<Country> findCountryOptional(Integer countryId) {
//        return countryRepo.findById(countryId);
//    }
//
//    @Cacheable(CACHE_PROXY_TYPE)
//    public ProxyType findProxyType(Integer proxyTypeId) {
//
//        return proxyTypeRepo.findById(proxyTypeId).orElseThrow(() -> new RuntimeException("ProxyType is unknown or not found - " + proxyTypeId.toString()));
//    }
//
//    @Cacheable(CACHE_ALG_SELECTION)
//    public AlgSelection findAlgSelection(Integer algSelectionId) {
//
//        return algSelectionRepo.findById(algSelectionId).orElseThrow(() -> new RuntimeException("AlgSelection is unknown or not found - " + algSelectionId.toString()));
//    }
//
//    @Cacheable(CACHE_PROXY_PROVIDER)
//    public ProxyProvider findProxyProvider(Integer proxyProviderId) {
//
//        return proxyProviderRepo.findById(proxyProviderId).orElseThrow(() -> new RuntimeException("ProxyProvider is unknown or not found - " + proxyProviderId.toString()));
//    }
//
//    @Cacheable(CACHE_PROXY_PROVIDER_OPTIONAL)
//    public Optional<ProxyProvider> findProxyProviderOptonal(Integer proxyProviderId) {
//
//        return proxyProviderRepo.findById(proxyProviderId);
//    }
//
//    @Cacheable(CACHE_SOCKS_AUTH_METHOD)
//    public SocksAuthMethod findSocksAuthMethod(Integer socksAuthMethodId) {
//
//        return socksAuthMethodRepo.findById(socksAuthMethodId).orElseThrow(() -> new RuntimeException("SocksAuthMethod is unknown or not found - " + socksAuthMethodId.toString()));
//    }
//    
//    public ProxyProvider findProxyProviderOrReturnNull(Integer proxyProviderId) {
//
//        return StmtProcessor.notNull(proxyProviderId)
//                ? findProxyProvider(proxyProviderId)
//                : null;
//    }
//
//    public Country findCountryOrReturnNull(Integer countryId) {
//
//        return StmtProcessor.notNull(countryId)
//                ? findCountry(countryId)
//                : null;
//    }
//
//    public AlgSelection findAlgSelectionOrReturnNull(Integer algSelectionId) {
//
//        return StmtProcessor.notNull(algSelectionId)
//                ? findAlgSelection(algSelectionId)
//                : null;
//    }
    //==========================================================================
    @Transactional
    public void synchronizeRefs() {

        log.info("synchronize system references");

//        proxyStatusRepo.saveAll(PROXY_STATUSES);
//        socksAuthMethodRepo.saveAll(SOCKS_AUTH_METHOD);
//        proxyTypeRepo.saveAll(PROXY_TYPES);
//        algSelectionRepo.saveAll(PROXY_ALG_SELECTIONS);
//        proxyProviderRepo.saveAll(PROXY_PROVIDERS);
//
        applicationRepo.saveAll(APPLICATIONS_LIST);
        paymentServiceRepo.saveAll(PAYMENT_SERVICES_LIST);
        paymentStatusRepo.saveAll(PAYMENT_STATUSES_LIST);

        currencyRepo.saveAll(currencies
                .entrySet()
                .stream()
                .map(entry -> {

                    final String currencyInfo = entry.getValue();

                    final String[] recs = currencyInfo.split(";");

                    final String code = recs[0];
                    final String name = recs[1];

                    // init ids list
                    CURRENCIES_IDS.add(code);

                    return StmtProcessor.create(Currency.class, record -> {
                        record.setCurrencyId(entry.getKey());
                        record.setCurrencyIso(code);
                        record.setCurrencyName(name);
                    });

                }).collect(Collectors.toList()));

        countryRepo.saveAll(countries
                .entrySet()
                .stream()
                .map(entry -> {

                    final String countryInfo = entry.getValue();

                    final String[] recs = countryInfo.split(";");

                    final String code = recs[0];
                    final String name = recs[1];

                    // init ids list
                    COUNTRIES_IDS.add(code);

                    return StmtProcessor.create(Country.class, record -> {
                        record.setCountryId(entry.getKey());
                        record.setCountryIso(code);
                        record.setCountryName(name);
                    });

                }).collect(Collectors.toList()));
    }
}
