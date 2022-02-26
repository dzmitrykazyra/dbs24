package org.dbs24.tik.mobile.dao;

import org.dbs24.tik.mobile.entity.domain.Currency;
import org.dbs24.tik.mobile.entity.domain.HeartPrice;

import java.util.List;

public interface HeartPriceDao {

    HeartPrice save(HeartPrice heartPriceToSave);

    List<HeartPrice> getAll();

    HeartPrice getByHeartsAmount(Currency currency, Integer heartsAmount);
}
