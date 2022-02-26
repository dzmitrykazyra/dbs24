package org.dbs24.tik.mobile.dao.impl;

import org.dbs24.tik.mobile.constant.CacheKey;
import org.dbs24.tik.mobile.dao.CurrencyDao;
import org.dbs24.tik.mobile.entity.domain.Currency;
import org.dbs24.tik.mobile.repo.CurrencyRepo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class CurrencyDaoImpl implements CurrencyDao {

    private final CurrencyRepo currencyRepo;

    public CurrencyDaoImpl(CurrencyRepo currencyRepo) {

        this.currencyRepo = currencyRepo;
    }

    @Override
    @Cacheable(CacheKey.CACHE_CURRENCY_BY_ISO)
    public Currency findUsdCurrency() {

        return currencyRepo.findByCurrencyIso(USD_ISO)
                .orElseThrow(() -> new RuntimeException("Cannot find USD currency record in DAO"));
    }
}
