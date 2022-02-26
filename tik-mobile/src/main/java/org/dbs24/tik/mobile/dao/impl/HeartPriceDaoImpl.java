package org.dbs24.tik.mobile.dao.impl;

import org.dbs24.tik.mobile.dao.HeartPriceDao;
import org.dbs24.tik.mobile.entity.domain.Currency;
import org.dbs24.tik.mobile.entity.domain.HeartPrice;
import org.dbs24.tik.mobile.repo.HeartPriceRepo;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class HeartPriceDaoImpl implements HeartPriceDao {

    private final HeartPriceRepo heartPriceRepo;

    public HeartPriceDaoImpl(HeartPriceRepo heartPriceRepo) {

        this.heartPriceRepo = heartPriceRepo;
    }

    @Override
    public HeartPrice save(HeartPrice heartPriceToSave) {

        return heartPriceRepo.save(heartPriceToSave);
    }

    @Override
    public List<HeartPrice> getAll() {

        return heartPriceRepo.findAll();
    }

    @Override
    public HeartPrice getByHeartsAmount(Currency currency, Integer heartsAmount) {

        return heartPriceRepo.findByCurrencyAndHeartsAmountLessThanEqual(currency, heartsAmount)
                .stream()
                .max(Comparator.comparing(HeartPrice::getPrice))
                .orElseThrow(() -> new RuntimeException("No hearts cost proportion record in db"));
    }
}
