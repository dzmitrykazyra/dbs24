package org.dbs24.tik.mobile.repo;

import org.dbs24.tik.mobile.entity.domain.Currency;
import org.dbs24.tik.mobile.entity.domain.HeartPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeartPriceRepo extends JpaRepository<HeartPrice, Integer> {

    List<HeartPrice> findByCurrencyAndHeartsAmountLessThanEqual(Currency currency, Integer heartsAmount);
}
