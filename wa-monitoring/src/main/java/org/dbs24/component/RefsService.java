/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.consts.SysConst;
import org.dbs24.entity.*;
import org.dbs24.repository.*;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.dbs24.consts.WaConsts.Caches.*;
import static org.dbs24.consts.WaConsts.Classes.*;
import static org.dbs24.consts.WaConsts.References.*;

@Log4j2
@Component
@EqualsAndHashCode(callSuper = true)
public class RefsService extends AbstractApplicationService {

    final CountryRepository countryRepository;
    final DeviceTypeRepository deviceTypeRepository;
    final ContractStatusRepository contractStatusRepository;
    final ContractTypeRepository contractTypeRepository;
    final AgentStatusRepository agentStatusRepository;
    final SubscriptionStatusRepository subscriptionStatusRepository;
    final ModifyReasonRepository modifyReasonRepository;
    final TariffPlanStatusRepository tariffPlanStatusRepository;
    final AgentOsTypeRepository agentOsTypeRepository;

    public RefsService(SubscriptionStatusRepository subscriptionStatusRepository, AgentStatusRepository agentStatusRepository, ContractStatusRepository contractStatusRepository, ContractTypeRepository contractTypeRepository, CountryRepository countryRepository, DeviceTypeRepository deviceTypeRepository, ModifyReasonRepository modifyReasonRepository, TariffPlanStatusRepository tariffPlanStatusRepository, AgentOsTypeRepository agentOsTypeRepository) {
        this.contractStatusRepository = contractStatusRepository;
        this.contractTypeRepository = contractTypeRepository;
        this.countryRepository = countryRepository;
        this.deviceTypeRepository = deviceTypeRepository;
        this.agentStatusRepository = agentStatusRepository;
        this.subscriptionStatusRepository = subscriptionStatusRepository;
        this.modifyReasonRepository = modifyReasonRepository;
        this.tariffPlanStatusRepository = tariffPlanStatusRepository;
        this.agentOsTypeRepository = agentOsTypeRepository;
    }

    @Cacheable(CACHE_MODIFY_REASON)
    public ModifyReason findModifyReason(Integer modifyReason) {

        return findOptionalModifyReason(modifyReason).orElseThrow();
    }

    public Optional<ModifyReason> findOptionalModifyReason(Integer modifyReason) {

        return modifyReasonRepository
                .findById(modifyReason);
    }

    @Cacheable(CACHE_AGENT_OS_TYPE_REF)
    public AgentOsType findAgentOsType(Byte agentOsTypeId) {

        return agentOsTypeRepository
                .findById(agentOsTypeId).orElseThrow();
    }

    @Cacheable(CACHE_AGENT_STATUS_REF)
    public AgentStatus findAgentStatus(Byte agentStatusId) {

        return agentStatusRepository
                .findById(agentStatusId).orElseThrow();

    }

    @Cacheable(CACHE_COUNTRY)
    public Country findCountry(Integer countryId) {

        return countryRepository
                .getOne(countryId);
    }

    @Cacheable(CACHE_DEVICE_TYPE)
    public DeviceType findDeviceType(Integer deviceTypeId) {
        return deviceTypeRepository
                .findById(deviceTypeId).orElseThrow();
    }

    @Cacheable(CACHE_CONTRACT_TYPE)
    public ContractType findContractType(Integer contractTypeId) {

        return findOptionalContractType(contractTypeId).orElseThrow();
    }

    @Cacheable(CACHE_CONTRACT_STATUS)
    public ContractStatus findContractStatus(Byte contractStatusId) {
        log.info("findContractStatus {}", contractStatusId);
        return contractStatusRepository
                .findById(contractStatusId).orElseThrow();
    }

    @Cacheable(CACHE_SUBSRIPTION_STATUS_REF)
    public SubscriptionStatus findSubscriptionStatus(Byte subscriptionStatusId) {

        return subscriptionStatusRepository
                .findById(subscriptionStatusId).orElseThrow();

    }

    @Cacheable(CACHE_TARIFF_PLAN_STATUS_REF)
    public TariffPlanStatus findTariffPlanStatus(Integer tariffPlanStatusId) {

        return tariffPlanStatusRepository
                .findById(tariffPlanStatusId).orElseThrow();
    }

    public Optional<TariffPlanStatus> findOptionalTariffPlanStatus(Integer tariffPlanStatusId) {

        return tariffPlanStatusRepository
                .findById(tariffPlanStatusId);
    }

    public Optional<ContractType> findOptionalContractType(Integer contractTypeId) {

        return contractTypeRepository
                .findById(contractTypeId);
    }

    public Optional<DeviceType> findOptionalDeviceType(Integer deviceTypeId) {

        return deviceTypeRepository
                .findById(deviceTypeId);
    }

    @Transactional
    public void synchronizeRefs() {

        countryRepository.saveAll(
                Arrays.stream(new String[][]{
                                {String.valueOf(0), SysConst.NOT_DEFINED, SysConst.NOT_DEFINED},
                                {String.valueOf(112), "BY", "BELARUS"},
                                {String.valueOf(643), "RU", "RUSSIAN"}})
                        .map(stringRow -> StmtProcessor.create(COUNTRY_CLASS, record -> {
                            record.setCountryId(Integer.valueOf(stringRow[0]));
                            record.setCountryIso(stringRow[1]);
                            record.setCountryName(stringRow[2]);
                        })).collect(Collectors.toList()));

        deviceTypeRepository.saveAll(
                Arrays.stream(ALL_DEVICE_TYPES).map(stringRow -> StmtProcessor.create(DEVICE_TYPE_CLASS, record -> {
                    record.setDeviceTypeId(Integer.valueOf(stringRow[0]));
                    record.setDeviceTypeName(stringRow[1]);
                })).collect(Collectors.toList()));

        contractTypeRepository.saveAll(Arrays.stream(ALL_CONTRACT_TYPES)
                .map(stringRow -> StmtProcessor.create(CONTRACT_TYPE_CLASS, record -> {
                    record.setContractTypeId(Integer.valueOf(stringRow[0]));
                    record.setContractTypeName(stringRow[1]);
                })).collect(Collectors.toList()));

        contractStatusRepository.saveAll(Arrays.stream(ALL_CONTRACT_STATUSES)
                .map(stringRow -> StmtProcessor.create(CONTRACT_STATUS_CLASS, record -> {
                    record.setContractStatusId(Byte.valueOf(stringRow[0]));
                    record.setContractStatusName(stringRow[1]);
                })).collect(Collectors.toList()));

        modifyReasonRepository.saveAll(Arrays.stream(ALL_MODIFY_REASONS_TYPES)
                .map(stringRow -> StmtProcessor.create(ModifyReason.class, record -> {
                    record.setModifyReasonId(Integer.valueOf(stringRow[0]));
                    record.setModifyReasonName(stringRow[1]);
                })).collect(Collectors.toList()));

        agentStatusRepository.saveAll(Arrays.stream(new String[][]{
                {String.valueOf(AS_RESERVE), "reserved"},
                {String.valueOf(AS_SUPPORT), "supported"},
                {String.valueOf(AS_TRACKNG), "tracking"},
                {String.valueOf(AS_BANNED), "banned"},
                {String.valueOf(AS_QUARANTINE), "quarantine"},
                {String.valueOf(AS_MESSAGING), "messaging"},
                {String.valueOf(AS_INSURANCE), "insurance"}
        }).map(stringRow -> StmtProcessor.create(AGENT_STATUS_CLASS, record -> {
            record.setAgentStatusId(Byte.valueOf(stringRow[0]));
            record.setAgentStatusName(stringRow[1]);
        })).collect(Collectors.toList()));

        agentOsTypeRepository.saveAll(Arrays.stream(new String[][]{
                {String.valueOf(OS_BASIC), "Basic"},
                {String.valueOf(OS_BUSINESS), "Business"}
        }).map(stringRow -> StmtProcessor.create(AgentOsType.class, record -> {
            record.setAgentOsTypeId(Byte.valueOf(stringRow[0]));
            record.setAgentOsTypeName(stringRow[1]);
        })).collect(Collectors.toList()));

        subscriptionStatusRepository.saveAll(Arrays.stream(ALL_SUBSCRIPTION_STATUSES)
                .map(stringRow -> StmtProcessor.create(SubscriptionStatus.class, record -> {
                    record.setSubscriptionStatusId(Byte.valueOf(stringRow[0]));
                    record.setSubscriptionStatusName(stringRow[1]);
                })).collect(Collectors.toList()));

        tariffPlanStatusRepository.saveAll(Arrays.stream(ALL_TARIFF_PLAN_STATUSES)
                .map(stringRow -> StmtProcessor.create(TariffPlanStatus.class, record -> {
                    record.setTariffPlanStatusId(Integer.valueOf(stringRow[0]));
                    record.setTariffPlanStatusName(stringRow[1]);
                })).collect(Collectors.toList()));

    }
}
