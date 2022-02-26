package org.dbs24.app.promo.repo;

import org.dbs24.app.promo.entity.BotDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface BotDetailRepo extends JpaRepository<BotDetail, Integer> {
}
