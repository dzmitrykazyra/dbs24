package org.dbs24.tik.mobile.repo;

import org.dbs24.tik.mobile.entity.domain.FireBaseApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FireBaseApplicationRepo extends JpaRepository<FireBaseApplication, Integer> {

    List<FireBaseApplication> findAllByIsActual(Boolean isActual);

}
