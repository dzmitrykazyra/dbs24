package org.dbs24.tik.mobile.repo;

import org.dbs24.tik.mobile.entity.domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepo extends JpaRepository<Currency, Integer> {

    Optional<Currency> findByCurrencyIso(String currencyIso);
}
