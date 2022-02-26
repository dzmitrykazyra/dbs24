/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.core.repo;

import java.util.Collection;
import java.util.List;

import org.dbs24.proxy.core.entity.domain.ProxyRequest;
import org.dbs24.proxy.core.entity.domain.ProxyUsage;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ProxyUsageRepo extends ApplicationJpaRepository<ProxyUsage, Integer>, JpaSpecificationExecutor<ProxyUsage>, PagingAndSortingRepository<ProxyUsage, Integer> {

    @Query(value = "select usage_id, request_id, proxy_id, session_start, session_finish, is_success, error, used_ip    from (\n"
            + "            select pu.*,\n"
            + "                   row_number() over ( partition by proxy_id order by session_start ) lv\n"
            + "              from prx_proxy_usage pu\n"
            + "              where proxy_id in (select proxy_id from prx_proxies where proxy_status_id=1)) pus\n"
            + "              where pus.lv=1\n"
            + "              order by session_start", nativeQuery = true)
    Collection<ProxyUsage> findActualProxyUsages();

    List<ProxyUsage> findByProxyRequest(ProxyRequest proxyRequest);
}
