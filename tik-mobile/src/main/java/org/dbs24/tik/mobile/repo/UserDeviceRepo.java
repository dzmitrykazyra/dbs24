package org.dbs24.tik.mobile.repo;

import org.dbs24.tik.mobile.entity.domain.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDeviceRepo extends JpaRepository<UserDevice, Integer> {

    List<UserDevice> findAllByUserId(Integer userId);

}
