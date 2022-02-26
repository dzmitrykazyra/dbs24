package org.dbs24.proxy.core.dao;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.entity.domain.Application;
import org.dbs24.proxy.core.entity.domain.ProxyRequest;
import org.dbs24.proxy.core.repo.ProxyRequestRepo;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Component
public class RequestDao extends DaoAbstractApplicationService {

    final ProxyRequestRepo proxyRequestRepo;

    public RequestDao(ProxyRequestRepo proxyRequestRepo) {

        this.proxyRequestRepo = proxyRequestRepo;
    }

    public List<ProxyRequest> findActualProxyRequestsByApplicationId(Application application) {

        return proxyRequestRepo.findByApplicationAndSessionFinishGreaterThan(application, LocalDateTime.now());
    }

    public void setCompleteSessionAndSave(ProxyRequest proxyRequestToComplete) {

        proxyRequestToComplete.setSessionFinish(LocalDateTime.now());
        proxyRequestRepo.save(proxyRequestToComplete);
    }

    public ProxyRequest findProxyRequestById(Integer proxyRequestId) {

        return proxyRequestRepo
                .findById(proxyRequestId)
                .orElseThrow(() -> new RuntimeException(String.format("proxyRequestId not found (%d)", proxyRequestId)));
    }

    public ProxyRequest saveProxyRequest(ProxyRequest proxyRequest) {

        return proxyRequestRepo.save(proxyRequest);
    }
}
