/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.core.repo;

import org.dbs24.proxy.core.entity.domain.Application;
import org.dbs24.proxy.core.entity.domain.ProxyRequest;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface ProxyRequestRepo extends ApplicationJpaRepository<ProxyRequest, Integer>, JpaSpecificationExecutor<ProxyRequest>, PagingAndSortingRepository<ProxyRequest, Integer> {

    List<ProxyRequest> findByApplicationAndSessionFinishGreaterThan(Application application, LocalDateTime localDateTime);
}
