package org.dbs24.tik.assist.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.tik.assist.entity.domain.Promocode;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface PromocodeRepo extends ApplicationJpaRepository<Promocode, Integer>, JpaSpecificationExecutor<Promocode>, PagingAndSortingRepository<Promocode, Integer> {

    Optional<Promocode> findByPromocodeValue(String promocodeValue);
}