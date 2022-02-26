package org.dbs24.email.spammer.dao;

import org.dbs24.email.spammer.entity.domain.Application;

import java.util.List;

public interface ReferenceDao {

    List<Application> saveAllApplications();

    Application findApplicationById(Integer applicationId);
    List<Application> findAllApplications();
}
