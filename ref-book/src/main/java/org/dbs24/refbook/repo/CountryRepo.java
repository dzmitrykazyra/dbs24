package org.dbs24.refbook.repo;

import org.dbs24.refbook.entity.Country;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface CountryRepo extends ApplicationJpaRepository<Country, Integer>, JpaSpecificationExecutor<Country>, PagingAndSortingRepository<Country, Integer> {

    Optional<Country> findByCountryIso(String countryIso);
}
