package org.dbs24.tik.mobile.repo;

import org.dbs24.tik.mobile.entity.domain.UserDeviceAndroid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDeviceAndroidRepo extends JpaRepository<UserDeviceAndroid, Integer> {

    Optional<UserDeviceAndroid> findByGsfId(String uid);

}
