package org.dbs24.proxy.core.dao;

import com.github.javafaker.App;
import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.consts.ProxyConsts;
import org.dbs24.proxy.core.entity.domain.Application;
import org.dbs24.proxy.core.entity.domain.ApplicationNetwork;
import org.dbs24.proxy.core.entity.domain.ApplicationStatus;
import org.dbs24.proxy.core.repo.*;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Log4j2
@Component
public class ApplicationDao extends DaoAbstractApplicationService {

    final ApplicationRepo applicationRepo;
    final ApplicationStatusRepo applicationStatusRepo;
    final ApplicationNetworkRepo applicationNetworkRepo;
    final ProxyUsageRepo proxyUsageRepo;
    final ProxyRequestRepo proxyRequestRepo;

    private final String DISABLED_APPLICATION_STATUS_NAME = ProxyConsts.ApplicationStatusEnum.AS_DISABLED.getValue();
    private final String ACTIVE_APPLICATION_STATUS_NAME = ProxyConsts.ApplicationStatusEnum.AS_ACTIVE.getValue();

    public ApplicationDao(ApplicationRepo applicationRepo, ApplicationStatusRepo applicationStatusRepo, ApplicationNetworkRepo applicationNetworkRepo, ProxyUsageRepo proxyUsageRepo, ProxyRequestRepo proxyRequestRepo) {
        this.applicationRepo = applicationRepo;
        this.applicationStatusRepo = applicationStatusRepo;
        this.applicationNetworkRepo = applicationNetworkRepo;
        this.proxyUsageRepo = proxyUsageRepo;
        this.proxyRequestRepo = proxyRequestRepo;
    }

    public Application findByApplicationId(Integer applicationId) {

        return applicationRepo
                .findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application with such id was not found"));
    }

    public Application save(Application applicationToSave) {

        return applicationRepo.save(applicationToSave);
    }

    public List<Application> findAllActiveApplications() {

        return applicationRepo.findByApplicationStatus(getActiveApplicationStatus());
    }

    public Application findByApplicationName(String applicationName) {

        return applicationRepo.findByName(applicationName)
                .stream()
                .findAny()
                .orElseThrow(() -> new RuntimeException("No such application with required name"));
    }

    public List<Application> findApplicationsByApplicationNetworkName(String applicationNetworkName) {

        ApplicationNetwork applicationNetwork = applicationNetworkRepo.findByApplicationNetworkName(applicationNetworkName).orElseThrow(() -> new RuntimeException("No application network with such name"));

        return applicationRepo.findByApplicationNetwork(applicationNetwork);
    }

    public Optional<Application> findOptionalByApplicationName(String applicationName) {

        return applicationRepo.findByName(applicationName).stream().findAny();
    }

    public Optional<Application> findOptionalByApplicationId(Integer applicationId) {

        return applicationRepo.findById(applicationId);
    }

    public Application findByProxyUsageId(Integer proxyUsageId) {

        return proxyRequestRepo.
                findById(
                        proxyUsageRepo
                                .findById(proxyUsageId)
                                .orElseThrow(() -> new RuntimeException("No usage by such id"))
                                .getProxyRequest()
                                .getRequestId()
                )
                .orElseThrow(() -> new RuntimeException("Request to use proxies has been lost"))
                .getApplication();
    }

    public void disableApplication(Application application) {

        application.setApplicationStatus(getDisabledApplicationStatus());
        applicationRepo.save(application);
    }

    public ApplicationStatus getDisabledApplicationStatus() {

        return applicationStatusRepo
                .findByApplicationStatusName(DISABLED_APPLICATION_STATUS_NAME)
                .stream()
                .findAny()
                .orElseThrow(() -> new RuntimeException("Cannot find disabled application status by name"));
    }

    public ApplicationStatus getActiveApplicationStatus() {

        return applicationStatusRepo
                .findByApplicationStatusName(ACTIVE_APPLICATION_STATUS_NAME)
                .stream()
                .findAny()
                .orElseThrow(() -> new RuntimeException("Cannot find active application status by name"));
    }

    public ApplicationStatus findApplicationStatusById(Integer statusId) {

        return applicationStatusRepo
                .findById(statusId)
                .orElseThrow(() -> new RuntimeException(String.format("Cannot find app status with id = %d", statusId)));
    }

    public Optional<ApplicationStatus> findApplicationStatusOptionalById(Integer statusId) {

        return applicationStatusRepo.findById(statusId);
    }
}
