package org.dbs24.tik.mobile.service.impl;

import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.dao.FireBaseApplicationDao;
import org.dbs24.tik.mobile.entity.domain.FireBaseApplication;
import org.dbs24.tik.mobile.entity.dto.firebase.FireBaseApplicationIdDto;
import org.dbs24.tik.mobile.entity.dto.firebase.FireBaseApplicationInfoDto;
import org.dbs24.tik.mobile.entity.dto.firebase.FireBaseApplicationListDto;
import org.dbs24.tik.mobile.entity.dto.firebase.FireBaseApplicationRequestDto;
import org.dbs24.tik.mobile.service.FireBaseApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@Log4j2
public class FireBaseApplicationServiceImpl implements FireBaseApplicationService {

    private final FireBaseApplicationDao fireBaseApplicationDao;

    @Autowired
    public FireBaseApplicationServiceImpl(FireBaseApplicationDao fireBaseApplicationDao) {
        this.fireBaseApplicationDao = fireBaseApplicationDao;
    }

    @Override
    public Mono<FireBaseApplicationListDto> getAllApplications() {

        return Mono.just(
                FireBaseApplicationListDto.of(
                        fireBaseApplicationDao.findAllActiveApps()
                )
        );
    }

    @Override
    @Transactional
    public Mono<FireBaseApplicationIdDto> createOrUpdateFireBaseApp(Mono<FireBaseApplicationRequestDto> fireBaseAppDtoMono) {

        return fireBaseAppDtoMono.map(fireBaseAppDto -> {

            FireBaseApplication fireBaseApplication = findOrCreateFireBaseApp(fireBaseAppDto.getFirebaseAppId());

            fireBaseApplication.setAdminSdk(fireBaseAppDto.getAdminSdk());
            fireBaseApplication.setDbUrl(fireBaseAppDto.getDbUrl());
            fireBaseApplication.setName(fireBaseAppDto.getName());
            fireBaseApplication.setPackageName(fireBaseAppDto.getPackageName());
            fireBaseApplication.setIsActual(fireBaseAppDto.getIsActual());
            fireBaseApplication.setActualDate(LocalDateTime.now());

            final FireBaseApplication savedApp = fireBaseApplicationDao.save(fireBaseApplication);

            return StmtProcessor.create(FireBaseApplicationIdDto.class,
                    idDto -> idDto.setFireBaseApplicationId(savedApp.getFirebaseAppId())
            );
        });
    }

    private FireBaseApplication findOrCreateFireBaseApp(Integer fireBaseAppId) {

        return fireBaseAppId == null
                ? StmtProcessor.create(FireBaseApplication.class)
                : fireBaseApplicationDao.findApplicationById(fireBaseAppId);
    }

}
