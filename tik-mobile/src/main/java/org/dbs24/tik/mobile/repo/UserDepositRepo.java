package org.dbs24.tik.mobile.repo;

import org.dbs24.tik.mobile.entity.domain.UserDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDepositRepo extends JpaRepository<UserDeposit, Integer> {

    Optional<UserDeposit> findUserDepositByUserId(Integer userId);

}
