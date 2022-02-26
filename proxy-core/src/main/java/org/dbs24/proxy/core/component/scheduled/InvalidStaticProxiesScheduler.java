package org.dbs24.proxy.core.component.scheduled;

import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.dao.ProxyDao;
import org.dbs24.proxy.core.entity.domain.Proxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
public class InvalidStaticProxiesScheduler {

    private final ProxyDao proxyDao;

    @Autowired
    public InvalidStaticProxiesScheduler(ProxyDao proxyDao) {
        this.proxyDao = proxyDao;
    }


//    @Scheduled(fixedRateString = "${config.proxy.static.invalid-refresh}")
    public void invalidStaticProxies() {

        log.info("Start invalid static proxies scheduler");

        final List<Proxy> expiredStaticProxies = proxyDao.findExpiredStaticProxies();

        log.info("Find {} expired static proxies", expiredStaticProxies.size());

        expiredStaticProxies.forEach(proxy -> proxy.setProxyStatus(proxyDao.findExpiredProxyStatus()));

        proxyDao.batchSaveProxies(expiredStaticProxies);

    }
}
