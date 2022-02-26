/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.service;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.tik.dev.dao.ReferencesDao;
import org.dbs24.tik.dev.entity.reference.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;


@Getter
@Log4j2
@Component
@Order(HIGHEST_PRECEDENCE)
@EqualsAndHashCode
public class RefsService extends AbstractApplicationService {

    final ReferencesDao referencesDao;

    public RefsService(ReferencesDao referencesDao) {
        this.referencesDao = referencesDao;
    }

    public DeveloperStatus findDeveloperStatus(Integer developerStatusId) {

        return referencesDao.findDeveloperStatus(developerStatusId);
    }

    public ContractStatus findContractStatus(Integer contractStatusId) {

        return referencesDao.findContractStatus(contractStatusId);
    }

    public TikAccountStatus findTikAccountStatus(Integer tikAccountId) {

        return referencesDao.findTikAccountStatus(tikAccountId);
    }

    public EndpointScope findEndpointScope(Integer endpointScopeId) {

        return referencesDao.findEndpointScope(endpointScopeId);
    }

    public TariffPlanType findTariffPlanType(Integer tariffPlanTypeId) {

        return referencesDao.findTariffPlanType(tariffPlanTypeId);
    }

    public DeviceStatus findDeviceStatus(Integer deviceStatusId) {

        return referencesDao.findDeviceStatus(deviceStatusId);
    }

    public Endpoint findEndpoint(Integer endpointId) {

        return referencesDao.findEndpoint(endpointId);
    }

    public EndpointResult findEndpointResult(Integer endpointResultId) {
        return referencesDao.findEndpointResult(endpointResultId);
    }
}
