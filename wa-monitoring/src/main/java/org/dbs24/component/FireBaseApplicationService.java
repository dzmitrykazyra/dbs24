/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.entity.FireBaseApplication;
import org.dbs24.repository.FireBaseApplicationRepository;
import org.dbs24.rest.api.AllFirebaseApplications;
import org.dbs24.rest.api.CreatedFireBaseApplication;
import org.dbs24.rest.api.FireBaseApplicationInfo;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collection;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static org.dbs24.stmt.StmtProcessor.create;
import static org.dbs24.stmt.StmtProcessor.nvl;
import static reactor.core.publisher.Mono.just;

@Data
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "wa-monitoring")
public class FireBaseApplicationService extends AbstractApplicationService {

    final FireBaseApplicationRepository fireBaseApplicationRepository;

    public FireBaseApplicationService(FireBaseApplicationRepository fireBaseApplicationRepository) {
        this.fireBaseApplicationRepository = fireBaseApplicationRepository;
    }

    //==========================================================================
    @Deprecated
    @Transactional
    public CreatedFireBaseApplication createOrUpdateFireBaseApplication(FireBaseApplicationInfo fireBaseApplicationInfo) {

        final FireBaseApplication fireBaseApplication = findOrCreateFireBaseApplication(fireBaseApplicationInfo.getFirebaseAppId());

        fireBaseApplication.setAdminSdk(fireBaseApplicationInfo.getAdminSdk());
        fireBaseApplication.setDbUrl(fireBaseApplicationInfo.getDbUrl());
        fireBaseApplication.setIsActual((Boolean) nvl(fireBaseApplicationInfo.getIsActual(), Boolean.TRUE));
        fireBaseApplication.setName(fireBaseApplicationInfo.getName());
        fireBaseApplication.setActualDate((LocalDateTime) nvl(NLS.long2LocalDateTime(fireBaseApplicationInfo.getActualDate()), now()));
        fireBaseApplication.setPackageName(fireBaseApplicationInfo.getPackageName());

        fireBaseApplicationRepository.save(fireBaseApplication);

        return create(CreatedFireBaseApplication.class, ca -> {

            ca.setFirebaseAppId(fireBaseApplication.getFirebaseAppId());

            log.debug("created/update fireBaseApplication: {}", fireBaseApplication.getFirebaseAppId());

        });
    }

    @Transactional
    public Mono<CreatedFireBaseApplication> couFireBaseApplication(FireBaseApplicationInfo fireBaseApplicationInfo) {

        final FireBaseApplication fireBaseApplication = findOrCreateFireBaseApplication(fireBaseApplicationInfo.getFirebaseAppId());

        fireBaseApplication.setAdminSdk(fireBaseApplicationInfo.getAdminSdk());
        fireBaseApplication.setDbUrl(fireBaseApplicationInfo.getDbUrl());
        fireBaseApplication.setIsActual(nvl(fireBaseApplicationInfo.getIsActual(), Boolean.TRUE));
        fireBaseApplication.setName(fireBaseApplicationInfo.getName());
        fireBaseApplication.setActualDate(nvl(NLS.long2LocalDateTime(fireBaseApplicationInfo.getActualDate()), now()));
        fireBaseApplication.setPackageName(fireBaseApplicationInfo.getPackageName());

        fireBaseApplicationRepository.save(fireBaseApplication);

        return just(create(CreatedFireBaseApplication.class, ca -> {

            ca.setFirebaseAppId(fireBaseApplication.getFirebaseAppId());

            log.debug("created/update fireBaseApplication: {}", fireBaseApplication.getFirebaseAppId());

        }));
    }

    //==========================================================================
    public AllFirebaseApplications getAllApplications() {

        return create(AllFirebaseApplications.class, afa -> findActualApplications()
                .forEach(app -> afa.getApps().add(create(FireBaseApplicationInfo.class, fbai -> fbai.assign(app)))));
    }

    //==========================================================================
    public FireBaseApplication createFireBaseApplication() {
        return create(FireBaseApplication.class, a -> a.setActualDate(now()));
    }

    public Collection<FireBaseApplication> findActualApplications() {

        return fireBaseApplicationRepository.findByIsActual(Boolean.TRUE);

    }

    public FireBaseApplication findOrCreateFireBaseApplication(Integer applicationId) {
        return ofNullable(applicationId)
                .map(this::findFireBaseApplicationAction)
                .orElseGet(this::createFireBaseApplication);
    }

    public FireBaseApplication findFireBaseApplicationAction(Integer applicationId) {

        return fireBaseApplicationRepository.getById(applicationId);
    }
}
