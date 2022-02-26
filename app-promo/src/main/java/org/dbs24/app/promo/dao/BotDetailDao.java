package org.dbs24.app.promo.dao;

import lombok.extern.log4j.Log4j2;
import org.dbs24.app.promo.entity.BotDetail;
import org.dbs24.app.promo.repo.BotDetailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class BotDetailDao {
    private final BotDetailRepo botDetailRepo;

    @Autowired
    public BotDetailDao(BotDetailRepo botDetailRepo) {
        this.botDetailRepo = botDetailRepo;
    }

    public BotDetail findDetailsByBotId(Integer botId) {
        return botDetailRepo.findById(botId)
                .orElseThrow(() -> new RuntimeException(String.format("Cannot find details with bot id = %d", botId)));
    }
}