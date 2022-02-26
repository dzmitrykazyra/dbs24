package org.dbs24.tik.mobile.dao.impl;

import org.dbs24.tik.mobile.dao.FireBaseApplicationDao;
import org.dbs24.tik.mobile.entity.domain.FireBaseApplication;
import org.dbs24.tik.mobile.repo.FireBaseApplicationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class FireBaseApplicationDaoImpl implements FireBaseApplicationDao {

    private final FireBaseApplicationRepo fireBaseApplicationRepo;

    @Autowired
    public FireBaseApplicationDaoImpl(FireBaseApplicationRepo fireBaseApplicationRepo) {
        this.fireBaseApplicationRepo = fireBaseApplicationRepo;
    }

    @Override
    public List<FireBaseApplication> findAllActiveApps() {
        return fireBaseApplicationRepo.findAllByIsActual(Boolean.TRUE);
    }

    @Override
    public FireBaseApplication findApplicationById(Integer fireBaseAppId) {
        return fireBaseApplicationRepo.findById(fireBaseAppId)
                .orElseThrow(() -> new RuntimeException("Cannot find fire base app with id = " + fireBaseAppId));
    }

    @Override
    public FireBaseApplication save(FireBaseApplication fireBaseApplication) {
        return fireBaseApplicationRepo.save(fireBaseApplication);
    }
}
