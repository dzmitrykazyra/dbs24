package org.dbs24.proxy.core.repo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.dbs24.proxy.core.consts.ProxyConsts;
import org.dbs24.proxy.core.entity.domain.Proxy;
import org.dbs24.proxy.core.entity.domain.ProxyStatus;
import org.dbs24.proxy.core.entity.domain.ProxyProvider;
import org.dbs24.proxy.core.entity.domain.ProxyType;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ProxyRepo extends ApplicationJpaRepository<Proxy, Integer>, JpaSpecificationExecutor<Proxy>, PagingAndSortingRepository<Proxy, Integer> {

    /**
     * Query to select proxy_ids which now using by target network and PT_MOBILE proxies(pt_id = 3) using by any network.
     * Can be used to exclude using the same proxies by the same network application cases
     * Also can be used to monopolize using PT_MOBILE proxy in current time
     */
    String EXCLUDE_APPLICATION_NETWORK_BY_ID =
            "(select proxy_id from prx_proxy_requests right join prx_proxy_usages ppu on ppu.request_id = prx_proxy_requests.request_id " +
            "where " +
            "session_finish > :localDateTime " +
            "and " +
            "application_id in (select application_id from prx_applications where application_network_id = :applicationNetworkId)" +
            "and " +
            "proxy_id in (select proxy_id from prx_proxies where proxy_type_id != 3))";

    /**
     * Query to select proxy_ids which now banned for target network.
     * Can be used to exclude using banned for usage by common network application cases
     */
    String EXCLUDE_BANNED_PROXIES_BY_ID =
            "(select proxy_id from prx_proxy_usage_bans " +
            "where application_network_id = :applicationNetworkId " +
            "and banned_until_date > :localDateTime)";


    Collection<Proxy> findByProxyStatus(ProxyStatus proxyStatus);

    Collection<Proxy> findByProxyStatusAndProxyProvider(ProxyStatus proxyStatus, ProxyProvider proxyProvider);

    @Query(value =
            "select * from prx_proxies where " +
            "external_ip_address = :externalIpAddress " +
            "and " +
            "socks_port = :socksPort " +
            "and " +
            "proxy_status_id = 1", nativeQuery = true)
    Optional<Proxy> findActualByExternalIpAddressAndSocksPort(@Param("externalIpAddress") String externalIpAddress,
                                                              @Param("socksPort") Integer socksPort);

    Optional<Proxy> findByExternalIpAddressAndSocksPortAndProxyStatus(String externalIpAddress, Integer socksPort, ProxyStatus proxyStatus);

    Optional<Proxy> findByUrlAndPort(String url, Integer port);
    
    Optional<Proxy> findByUrlAndSocksPort(String url, Integer port);

    List<Proxy> findAllByDateEndLessThanAndProxyType(LocalDateTime localDateTime, ProxyType proxyType);

    @Query(value =
            "select * from prx_proxies where " +
            "(proxy_type_id = :proxyTypeId or :isProxyTypeSearchNotEnabled = true) " +
            "and (provider_id = :providerId or :isProviderSearchNotEnabled = true) " +
            "and (country_id = :countryId or :isCountrySearchNotEnabled = true) " +
            "and proxy_status_id = 1" +
            "and proxy_id not in " + EXCLUDE_APPLICATION_NETWORK_BY_ID +
            "and proxy_id not in " + EXCLUDE_BANNED_PROXIES_BY_ID +
            "limit :proxiesAmount", nativeQuery = true)
    List<Proxy> findWithAnyValuesLimit(@Param("proxyTypeId") Integer proxyTypeId, @Param("isProxyTypeSearchNotEnabled") Boolean isProxyTypeSearchNotEnabled,
                                       @Param("providerId") Integer providerId, @Param("isProviderSearchNotEnabled") Boolean isProviderSearchNotEnabled,
                                       @Param("countryId") Integer countryId, @Param("isCountrySearchNotEnabled") Boolean isCountrySearchNotEnabled,
                                       @Param("applicationNetworkId") Integer applicationNetworkId,
                                       @Param("localDateTime") LocalDateTime localDateTime,
                                       @Param("proxiesAmount") Integer proxiesAmount);

    @Query(value =
            "select * from prx_proxies where " +
            "(proxy_type_id = :proxyTypeId or :isProxyTypeSearchNotEnabled = true) " +
            "and (provider_id = :providerId or :isProviderSearchNotEnabled = true) " +
            "and (country_id = :countryId or :isCountrySearchNotEnabled = true) " +
            "and proxy_status_id = 1" +
            "and proxy_id not in " + EXCLUDE_APPLICATION_NETWORK_BY_ID +
            "and proxy_id not in " + EXCLUDE_BANNED_PROXIES_BY_ID +
            "order by actual_date " +
            "limit :proxiesAmount", nativeQuery = true)
    List<Proxy> findWithAnyValuesOrderByActualDateLimit(@Param("proxyTypeId") Integer proxyTypeId, @Param("isProxyTypeSearchNotEnabled") Boolean isProxyTypeSearchNotEnabled,
                                                        @Param("providerId") Integer providerId, @Param("isProviderSearchNotEnabled") Boolean isProviderSearchNotEnabled,
                                                        @Param("countryId") Integer countryId, @Param("isCountrySearchNotEnabled") Boolean isCountrySearchNotEnabled,
                                                        @Param("applicationNetworkId") Integer applicationNetworkId,
                                                        @Param("localDateTime") LocalDateTime localDateTime,
                                                        @Param("proxiesAmount") Integer proxiesAmount);

    @Query(value =
            "select * from prx_proxies " +
            "left join " +
            "    (select proxy_id, max(session_finish) as last_usage_time " +
            "    from prx_proxy_usages " +
            "    natural join prx_proxy_requests " +
            "    where session_finish > :localDate " +
            "    group by proxy_id) as tab " +
            "on (prx_proxies.proxy_id=tab.proxy_id) " +
            "where (country_id = :countryId or :isCountrySearchNotEnabled = true) " +
            "and (proxy_type_id = :proxyTypeId or :isProxyTypeSearchNotEnabled = true) " +
            "and (provider_id = :providerId or :isProviderSearchNotEnabled = true) " +
            "and proxy_status_id = 1" +
            "and prx_proxies.proxy_id not in " + EXCLUDE_APPLICATION_NETWORK_BY_ID +
            "and prx_proxies.proxy_id not in " + EXCLUDE_BANNED_PROXIES_BY_ID +
            "order by last_usage_time asc " +
            "nulls first " +
            "limit :proxiesAmount", nativeQuery = true)
    List<Proxy> findWithAnyValuesLimitByLastTimeUsage(@Param("localDate") LocalDate localDate,
                                                      @Param("proxyTypeId") Integer proxyTypeId, @Param("isProxyTypeSearchNotEnabled") Boolean isProxyTypeSearchNotEnabled,
                                                      @Param("providerId") Integer providerId, @Param("isProviderSearchNotEnabled") Boolean isProviderSearchNotEnabled,
                                                      @Param("countryId") Integer countryId, @Param("isCountrySearchNotEnabled") Boolean isCountrySearchNotEnabled,
                                                      @Param("applicationNetworkId") Integer applicationNetworkId,
                                                      @Param("localDateTime") LocalDateTime localDateTime,
                                                      @Param("proxiesAmount") Integer proxiesAmount);

    @Query(value =
            "select * " +
            "from prx_proxies left join " +
            "    (select proxy_id, count(*) as usage_amount " +
            "    from prx_proxy_usages natural join prx_proxy_requests " +
            "    where session_finish > :localDate " +
            "    group by proxy_id) as tab " +
            "    on (prx_proxies.proxy_id=tab.proxy_id) " +
            "where (country_id = :countryId or :isCountrySearchNotEnabled = true) " +
            "and (proxy_type_id = :proxyTypeId or :isProxyTypeSearchNotEnabled = true) " +
            "and (provider_id = :providerId or :isProviderSearchNotEnabled = true) " +
            "and proxy_status_id = 1" +
            "and prx_proxies.proxy_id not in " + EXCLUDE_APPLICATION_NETWORK_BY_ID +
            "and prx_proxies.proxy_id not in " + EXCLUDE_BANNED_PROXIES_BY_ID +
            "order by usage_amount asc nulls first " +
            "limit :proxiesAmount", nativeQuery = true)
    List<Proxy> findWithAnyValuesLimitByUsageAmount(@Param("localDate") LocalDate localDate,
                                                    @Param("proxyTypeId") Integer proxyTypeId, @Param("isProxyTypeSearchNotEnabled") Boolean isProxyTypeSearchNotEnabled,
                                                    @Param("providerId") Integer providerId, @Param("isProviderSearchNotEnabled") Boolean isProviderSearchNotEnabled,
                                                    @Param("countryId") Integer countryId, @Param("isCountrySearchNotEnabled") Boolean isCountrySearchNotEnabled,
                                                    @Param("applicationNetworkId") Integer applicationNetworkId,
                                                    @Param("localDateTime") LocalDateTime localDateTime,
                                                    @Param("proxiesAmount") Integer proxiesAmount);
}
