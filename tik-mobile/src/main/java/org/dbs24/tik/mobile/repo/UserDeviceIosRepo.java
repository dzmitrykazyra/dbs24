package org.dbs24.tik.mobile.repo;

import org.dbs24.tik.mobile.entity.domain.UserDeviceIos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDeviceIosRepo extends JpaRepository<UserDeviceIos, Integer> {

    Optional<UserDeviceIos> findByIdentifierForVendor(String identifier);

}
