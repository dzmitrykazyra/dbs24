/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.dao;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.dbs24.tik.dev.entity.Device;
import org.dbs24.tik.dev.entity.DeviceHist;
import org.dbs24.tik.dev.repo.DeviceHistRepo;
import org.dbs24.tik.dev.repo.DeviceRepo;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Getter
@Log4j2
@Component
public class DeviceDao extends DaoAbstractApplicationService {

    final DeviceRepo contractRepo;
    final DeviceHistRepo contractHistRepo;

    public DeviceDao(DeviceRepo contractRepo, DeviceHistRepo contractHistRepo) {
        this.contractRepo = contractRepo;
        this.contractHistRepo = contractHistRepo;
    }

    //==========================================================================
    public Optional<Device> findOptionalDevice(Long contractId) {
        return contractRepo.findById(contractId);
    }

    public Device findDevice(Long contractId) {
        return findOptionalDevice(contractId).orElseThrow();
    }

    public void saveDeviceHist(DeviceHist contractHist) {
        contractHistRepo.save(contractHist);
    }

    public void saveDevice(Device contract) {
        contractRepo.save(contract);
    }

}
