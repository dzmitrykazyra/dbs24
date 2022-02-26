package org.dbs24.proxy.core.dao;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.consts.ProxyConsts;
import org.dbs24.proxy.core.entity.domain.*;
import org.dbs24.proxy.core.repo.*;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.dbs24.proxy.core.consts.ProxyConsts.Caches.*;

@Data
@Log4j2
@Component
public class ReferenceDao extends DaoAbstractApplicationService {

    final AlgSelectionRepo algSelectionRepo;
    final CountryRepo countryRepo;
    final ErrorTypeRepo errorTypeRepo;
    final ProxyProviderRepo proxyProviderRepo;
    final ProxyStatusRepo proxyStatusRepo;
    final ProxyTypeRepo proxyTypeRepo;
    final SocksAuthMethodRepo socksAuthMethodRepo;
    final ApplicationStatusRepo applicationStatusRepo;

    public ReferenceDao(AlgSelectionRepo algSelectionRepo,
                        CountryRepo countryRepo,
                        ErrorTypeRepo errorTypeRepo,
                        ProxyProviderRepo proxyProviderRepo,
                        ProxyStatusRepo proxyStatusRepo,
                        ProxyTypeRepo proxyTypeRepo,
                        SocksAuthMethodRepo socksAuthMethodRepo, ApplicationStatusRepo applicationStatusRepo) {
        this.algSelectionRepo = algSelectionRepo;
        this.countryRepo = countryRepo;
        this.errorTypeRepo = errorTypeRepo;
        this.proxyProviderRepo = proxyProviderRepo;
        this.proxyStatusRepo = proxyStatusRepo;
        this.proxyTypeRepo = proxyTypeRepo;
        this.socksAuthMethodRepo = socksAuthMethodRepo;
        this.applicationStatusRepo = applicationStatusRepo;
    }

    public void saveAllReferences() {

        log.info("Synchronize system references");

        applicationStatusRepo.saveAll(ProxyConsts.ApplicationStatusEnum.APPLICATION_STATUSES);
        proxyStatusRepo.saveAll(ProxyConsts.ProxyStatusEnum.PROXY_STATUSES);
        socksAuthMethodRepo.saveAll(ProxyConsts.SocksAuthMethods.SOCKS_AUTH_METHOD);
        proxyTypeRepo.saveAll(ProxyConsts.ProxyTypeEnum.PROXY_TYPES);
        algSelectionRepo.saveAll(ProxyConsts.AlgSelectionEnum.PROXY_ALG_SELECTIONS);
        proxyProviderRepo.saveAll(ProxyConsts.ProxyProviderEnum.PROXY_PROVIDERS);
        errorTypeRepo.saveAll(ProxyConsts.ErrorTypeEnum.USAGE_ERROR_TYPES);
    }

    public void saveCountries(List<Country> countries) {

        countryRepo.saveAllAndFlush(countries);
    }

    private List<Country> convertCountriesMapToList(Map<Integer, String> countriesToSave) {

        return  countriesToSave
                .entrySet()
                .stream()
                .map(entry -> {

                    final String countryInfo = entry.getValue();

                    final String[] recs = countryInfo.split(";");

                    final String code = recs[0];
                    final String name = recs[1];

                    // init ids list
                    ProxyConsts.Countries.COUNTRIES_IDS.add(entry.getKey());

                    return StmtProcessor.create(Country.class, record -> {
                        record.setCountryId(entry.getKey());
                        record.setCountryIso(code);
                        record.setCountryName(name);
                    });

                }).collect(Collectors.toList());
    }

    public Optional<ProxyType> findProxyTypeByName(String proxyTypeName) {
        return proxyTypeRepo.findByProxyTypeName(proxyTypeName);
    }

    @Cacheable(CACHE_PROXY_TYPE)
    public Optional<ProxyType> findProxyTypeById(Integer proxyTypeId) {
        return proxyTypeRepo.findById(proxyTypeId);
    }

    public List<ProxyType> findAllProxyTypes() {
        return proxyTypeRepo.findAll();
    }

    public Optional<ProxyProvider> findProxyProviderByName(String proxyProviderName) {
        return proxyProviderRepo.findByProviderNameIgnoreCase(proxyProviderName);
    }

    public Optional<ProxyProvider> findProxyProviderByNameIgnoreCase(String proxyProviderName) {
        return proxyProviderRepo.findByProviderNameIgnoreCase(proxyProviderName);
    }

    @Cacheable(CACHE_PROXY_PROVIDER_OPTIONAL)
    public Optional<ProxyProvider> findProxyProviderById(Integer proxyProviderId) {
        return proxyProviderRepo.findById(proxyProviderId);
    }
    public List<ProxyProvider> findAllProxyProviders() {
        return proxyProviderRepo.findAll();
    }


    @Cacheable(CACHE_COUNTRY_NAME)
    public Optional<Country> findCountryByName(String countryName) {
        return countryRepo.findByCountryName(countryName);
    }
    @Cacheable(CACHE_COUNTRY)
    public Optional<Country> findCountryById(Integer countryId) {
        return countryRepo.findById(countryId);
    }

    @Cacheable(CACHE_COUNTRY_ISO)
    public Optional<Country> findCountryByIso(String countryIso) {
        return countryRepo.findByCountryIso(countryIso);
    }

    public List<Country> findAllCountries() {
        return countryRepo.findAll();
    }

    public Optional<AlgSelection> findAlgSelectionByName(String algSelectionName) {
        return algSelectionRepo.findByAlgName(algSelectionName);
    }
    @Cacheable(CACHE_ALG_SELECTION)
    public Optional<AlgSelection> findAlgSelectionById(Integer algSelectionId) {
        return algSelectionRepo.findById(algSelectionId);
    }
    public List<AlgSelection> findAllAlgSelections() {
        return algSelectionRepo.findAll();
    }

    @Cacheable(CACHE_PROXY_STATUS)
    public Optional<ProxyStatus> findProxyStatusById(Integer proxyStatusId) {
        return proxyStatusRepo.findById(proxyStatusId);
    }

    @Cacheable(CACHE_SOCKS_AUTH_METHOD)
    public Optional<SocksAuthMethod> findSocksAuthMethodById(Integer socksAuthMethodId) {
        return socksAuthMethodRepo.findById(socksAuthMethodId);
    }

    public Optional<ErrorType> findErrorTypeByName(String errorTypeName) {
        return errorTypeRepo.findByErrorTypeName(errorTypeName);
    }
}
