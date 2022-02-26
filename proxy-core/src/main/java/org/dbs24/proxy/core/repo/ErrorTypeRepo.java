package org.dbs24.proxy.core.repo;

import org.dbs24.proxy.core.entity.domain.ErrorType;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ErrorTypeRepo extends ApplicationJpaRepository<ErrorType, Integer>, JpaSpecificationExecutor<ErrorType>, PagingAndSortingRepository<ErrorType, Integer> {

    Optional<ErrorType> findByErrorTypeName(String errorTypeName);
}