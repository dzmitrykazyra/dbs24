/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.dao;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.dbs24.tik.dev.entity.reference.*;
import org.dbs24.tik.dev.repo.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import static org.dbs24.tik.dev.consts.TikDevApiConsts.Caches.*;
import static org.dbs24.tik.dev.consts.enums.ContractStatusEnum.CONTRACTS_STATUSES_LIST;
import static org.dbs24.tik.dev.consts.enums.DeveloperStatusEnum.DEVELOPERS_STATUSES_LIST;
import static org.dbs24.tik.dev.consts.enums.DeviceStatusEnum.DEVICE_STATUSES_LIST;
import static org.dbs24.tik.dev.consts.enums.EndpointEnum.ENDPOINTS_LIST;
import static org.dbs24.tik.dev.consts.enums.EndpointResultEnum.ENDPOINTS_RESULTS_LIST;
import static org.dbs24.tik.dev.consts.enums.EndpointScopeEnum.ENDPOINTS_SCOPES_LIST;
import static org.dbs24.tik.dev.consts.enums.TariffPlanStatusEnum.TP_STATUSES_LIST;
import static org.dbs24.tik.dev.consts.enums.TariffPlanTypeEnum.TP_TYPES_LIST;
import static org.dbs24.tik.dev.consts.enums.TikAccountStatusEnum.TIK_ACCOUNT_STATUSES_LIST;

@Getter
@Log4j2
@Component
public class ReferencesDao extends DaoAbstractApplicationService {

    final EndpointScopeRepo endpointScopeRepo;
    final EndpointResultRepo endpointResultRepo;
    final DeveloperStatusRepo developerStatusRepo;
    final TikAccountStatusRepo tikAccountStatusRepo;
    final TariffPlanStatusRepo tariffPlanStatusRepo;
    final TariffPlanTypeRepo tariffPlanTypeRepo;
    final ContractStatusRepo contractStatusRepo;
    final EndpointRepo endpointRepo;
    final DeviceStatusRepo deviceStatusRepo;

    public ReferencesDao(EndpointScopeRepo endpointScopeRepo, EndpointResultRepo endpointResultRepo, DeveloperStatusRepo developerStatusRepo, TikAccountStatusRepo tikAccountStatusRepo, TariffPlanStatusRepo tariffPlanStatusRepo, TariffPlanTypeRepo tariffPlanTypeRepo, ContractStatusRepo contractStatusRepo, EndpointRepo endpointRepo, DeviceStatusRepo deviceStatusRepo) {

        this.endpointScopeRepo = endpointScopeRepo;
        this.endpointResultRepo = endpointResultRepo;
        this.developerStatusRepo = developerStatusRepo;
        this.tikAccountStatusRepo = tikAccountStatusRepo;
        this.tariffPlanStatusRepo = tariffPlanStatusRepo;
        this.tariffPlanTypeRepo = tariffPlanTypeRepo;
        this.contractStatusRepo = contractStatusRepo;
        this.endpointRepo = endpointRepo;
        this.deviceStatusRepo = deviceStatusRepo;
    }

    public void saveAllReferences() {

        endpointScopeRepo.saveAll(ENDPOINTS_SCOPES_LIST);
        endpointResultRepo.saveAll(ENDPOINTS_RESULTS_LIST);
        developerStatusRepo.saveAll(DEVELOPERS_STATUSES_LIST);
        tikAccountStatusRepo.saveAll(TIK_ACCOUNT_STATUSES_LIST);
        tariffPlanStatusRepo.saveAll(TP_STATUSES_LIST);
        tariffPlanTypeRepo.saveAll(TP_TYPES_LIST);
        contractStatusRepo.saveAll(CONTRACTS_STATUSES_LIST);
        endpointRepo.saveAll(ENDPOINTS_LIST);
        deviceStatusRepo.saveAll(DEVICE_STATUSES_LIST);
    }

    @Cacheable(CACHE_DEVELOPER_STATUS)
    public DeveloperStatus findDeveloperStatus(Integer developerStatusId) {

        return developerStatusRepo.findById(developerStatusId).orElseThrow();
    }

    @Cacheable(CACHE_CONTRACT_STATUS)
    public ContractStatus findContractStatus(Integer contractStatusId) {

        return contractStatusRepo.findById(contractStatusId).orElseThrow();
    }

    @Cacheable(CACHE_TIK_ACCOUNT_STATUS)
    public TikAccountStatus findTikAccountStatus(Integer tikAccountStatusId) {

        return tikAccountStatusRepo.findById(tikAccountStatusId).orElseThrow();
    }

    @Cacheable(CACHE_TIK_ENDPOINT_SCOPE)
    public EndpointScope findEndpointScope(Integer endpointScopeId) {

        return endpointScopeRepo.findById(endpointScopeId).orElseThrow();
    }

    @Cacheable(CACHE_TP_STATUS)
    public TariffPlanStatus findTariffPlanStatus(Integer tariffPlanStatusId) {

        return tariffPlanStatusRepo.findById(tariffPlanStatusId).orElseThrow();
    }

    @Cacheable(CACHE_TP_TYPE)
    public TariffPlanType findTariffPlanType(Integer tariffPlanTypeId) {

        return tariffPlanTypeRepo.findById(tariffPlanTypeId).orElseThrow();
    }

    @Cacheable(CACHE_DEVICE_STATUS)
    public DeviceStatus findDeviceStatus(Integer deviceStatusId) {

        return deviceStatusRepo.findById(deviceStatusId).orElseThrow();
    }

    @Cacheable(CACHE_ENDPOINT)
    public Endpoint findEndpoint(Integer endpointId) {

        return endpointRepo.findById(endpointId).orElseThrow();
    }

    @Cacheable(CACHE_ENDPOINT_RESULT)
    public EndpointResult findEndpointResult(Integer endpointResultId) {

        return endpointResultRepo.findById(endpointResultId).orElseThrow();
    }


}
