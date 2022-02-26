package org.dbs24.tik.mobile.dao;

import org.dbs24.tik.mobile.entity.domain.Currency;

public interface CurrencyDao {

    String USD_ISO = "USD";

    Currency findUsdCurrency();
}
