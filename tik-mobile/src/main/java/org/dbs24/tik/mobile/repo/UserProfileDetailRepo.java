package org.dbs24.tik.mobile.repo;

import org.dbs24.tik.mobile.entity.domain.UserProfileDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileDetailRepo extends JpaRepository<UserProfileDetail, Integer> {

    Optional<UserProfileDetail> findByUserId(Integer userId);
}
