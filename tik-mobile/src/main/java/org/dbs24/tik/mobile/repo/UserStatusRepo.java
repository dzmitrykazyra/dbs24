package org.dbs24.tik.mobile.repo;

import org.dbs24.tik.mobile.entity.domain.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserStatusRepo extends JpaRepository<UserStatus, Integer> {

    Optional<UserStatus> findByUserStatusName(String userStatusName);
}
