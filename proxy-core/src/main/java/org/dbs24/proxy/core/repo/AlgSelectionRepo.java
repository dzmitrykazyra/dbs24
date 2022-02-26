/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.core.repo;

import org.dbs24.proxy.core.entity.domain.AlgSelection;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;


public interface AlgSelectionRepo extends ApplicationJpaRepository<AlgSelection, Integer>, JpaSpecificationExecutor<AlgSelection>, PagingAndSortingRepository<AlgSelection, Integer> {

    Optional<AlgSelection> findByAlgName(String algName);
}
