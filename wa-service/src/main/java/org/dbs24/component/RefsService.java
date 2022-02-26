/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import lombok.extern.log4j.Log4j2;
import org.dbs24.entity.*;
import org.dbs24.repository.*;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.dbs24.consts.WaConsts.Caches.*;
import static org.dbs24.consts.WaConsts.References.*;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "wa-monitoring")
public class RefsService extends AbstractApplicationService {

    final DeviceTypeRepository deviceTypeRepository;
    final ContractTypeRepository contractTypeRepository;
    final AgentStatusRepository agentStatusRepository;
    final ModifyReasonRepository modifyReasonRepository;
    final TariffPlanStatusRepository tariffPlanStatusRepository;

    public RefsService(AgentStatusRepository agentStatusRepository, ContractTypeRepository contractTypeRepository, DeviceTypeRepository deviceTypeRepository, ModifyReasonRepository modifyReasonRepository, TariffPlanStatusRepository tariffPlanStatusRepository) {
        this.contractTypeRepository = contractTypeRepository;
        this.deviceTypeRepository = deviceTypeRepository;
        this.agentStatusRepository = agentStatusRepository;
        this.modifyReasonRepository = modifyReasonRepository;
        this.tariffPlanStatusRepository = tariffPlanStatusRepository;
    }

    @Cacheable(CACHE_MODIFY_REASON)
    public ModifyReason findModifyReason(Integer modifyReason) {

        return findOptionalModifyReason(modifyReason).orElseThrow();
    }

    public Optional<ModifyReason> findOptionalModifyReason(Integer modifyReason) {

        return modifyReasonRepository
                .findById(modifyReason);
    }

    @Cacheable(CACHE_AGENT_STATUS_REF)
    public AgentStatus findAgentStatus(Byte agentStatusId) {

        return agentStatusRepository
                .findById(agentStatusId).orElseThrow();

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

        deviceTypeRepository.saveAll(
                Arrays.stream(ALL_DEVICE_TYPES).map(stringRow -> StmtProcessor.create(DeviceType.class, record -> {
                    record.setDeviceTypeId(Integer.valueOf(stringRow[0]));
                    record.setDeviceTypeName(stringRow[1]);
                })).collect(Collectors.toList()));

        contractTypeRepository.saveAll(Arrays.stream(ALL_CONTRACT_TYPES)
                .map(stringRow -> StmtProcessor.create(ContractType.class, record -> {
                    record.setContractTypeId(Integer.valueOf(stringRow[0]));
                    record.setContractTypeName(stringRow[1]);
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
                {String.valueOf(AS_QUARANTINE), "quarantine"}
        }).map(stringRow -> StmtProcessor.create(AgentStatus.class, record -> {
            record.setAgentStatusId(Byte.valueOf(stringRow[0]));
            record.setAgentStatusName(stringRow[1]);
        })).collect(Collectors.toList()));

        tariffPlanStatusRepository.saveAll(Arrays.stream(ALL_TARIFF_PLAN_STATUSES)
                .map(stringRow -> StmtProcessor.create(TariffPlanStatus.class, record -> {
                    record.setTariffPlanStatusId(Integer.valueOf(stringRow[0]));
                    record.setTariffPlanStatusName(stringRow[1]);
                })).collect(Collectors.toList()));

    }
}
