package org.dbs24.app.promo.repo;

import org.dbs24.app.promo.entity.PhoneConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface PhoneConfigRepo extends JpaRepository<PhoneConfig, Integer> {
}
