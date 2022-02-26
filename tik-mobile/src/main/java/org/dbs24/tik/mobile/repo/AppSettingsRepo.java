package org.dbs24.tik.mobile.repo;

import org.dbs24.tik.mobile.entity.domain.AppSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppSettingsRepo extends JpaRepository<AppSettings, Integer> {

    Optional<AppSettings> findByPackageName(String packageName);

}
