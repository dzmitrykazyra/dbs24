package org.dbs24.tik.mobile.repo;

import org.dbs24.tik.mobile.entity.domain.UserHist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHistRepo extends JpaRepository<UserHist, Integer> {
}
