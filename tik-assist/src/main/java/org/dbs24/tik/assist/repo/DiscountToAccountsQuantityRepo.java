package org.dbs24.tik.assist.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.tik.assist.entity.domain.DiscountToAccountsQuantity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DiscountToAccountsQuantityRepo extends ApplicationJpaRepository<DiscountToAccountsQuantity, String>, JpaSpecificationExecutor<DiscountToAccountsQuantity>, PagingAndSortingRepository<DiscountToAccountsQuantity, String> {

    List<DiscountToAccountsQuantity> findByUpToAccountsQuantityGreaterThan(Integer accountsQuantity);
}