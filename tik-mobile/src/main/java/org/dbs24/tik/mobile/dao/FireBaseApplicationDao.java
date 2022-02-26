package org.dbs24.tik.mobile.dao;

import org.dbs24.tik.mobile.entity.domain.FireBaseApplication;

import java.util.List;

public interface FireBaseApplicationDao {

    List<FireBaseApplication> findAllActiveApps();

    FireBaseApplication findApplicationById(Integer appId);

    FireBaseApplication save(FireBaseApplication fireBaseApplication);

}
