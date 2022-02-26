package org.dbs24.tik.mobile.repo;

import org.dbs24.tik.mobile.entity.domain.UserDepositHist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDepositHistRepo extends JpaRepository<UserDepositHist, Integer> {
}
