package org.dbs24.proxy.core.repo;

import org.dbs24.proxy.core.entity.domain.ApplicationNetwork;
import org.dbs24.proxy.core.entity.domain.Proxy;
import org.dbs24.proxy.core.entity.domain.ProxyUsageBan;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProxyUsageBanRepo extends ApplicationJpaRepository<ProxyUsageBan, Integer>, JpaSpecificationExecutor<ProxyUsageBan>, PagingAndSortingRepository<ProxyUsageBan, Integer> {

    List<ProxyUsageBan> findByApplicationNetworkAndBannedUntilDateGreaterThan(ApplicationNetwork applicationNetwork, LocalDateTime localDateTime);

    List<ProxyUsageBan> findByProxyAndBannedUntilDateGreaterThan(Proxy proxy, LocalDateTime localDateTime);
}