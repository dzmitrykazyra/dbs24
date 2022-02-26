package org.dbs24.email.spammer.dao.impl;

import lombok.extern.log4j.Log4j2;
import org.dbs24.email.spammer.constant.ApplicationDefine;
import org.dbs24.email.spammer.dao.ReferenceDao;
import org.dbs24.email.spammer.entity.domain.Application;
import org.dbs24.email.spammer.repo.ApplicationRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
public class ReferenceDaoImpl implements ReferenceDao {

    private final ApplicationRepository applicationRepository;

    public ReferenceDaoImpl(ApplicationRepository applicationRepository) {

        this.applicationRepository = applicationRepository;
    }

    @Override
    public List<Application> saveAllApplications() {

        return applicationRepository.saveAll(ApplicationDefine.getAllApplications());
    }

    @Override
    public Application findApplicationById(Integer applicationId) {

        return applicationRepository.findById(applicationId).orElseThrow(() -> new RuntimeException("No application with such id in repo layer"));
    }

    @Override
    public List<Application> findAllApplications() {

        return applicationRepository.findAll();
    }
}
