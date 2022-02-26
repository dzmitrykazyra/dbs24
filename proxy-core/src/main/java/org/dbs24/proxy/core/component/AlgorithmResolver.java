package org.dbs24.proxy.core.component;

import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.consts.ProxyConsts;
import org.dbs24.proxy.core.dao.ProxyDao;
import org.dbs24.proxy.core.entity.domain.Proxy;
import org.dbs24.proxy.core.entity.domain.ProxyRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
class AlgorithmResolver {

    public static final ProxyConsts.AlgSelectionEnum DEFAULT_ALG_SELECTION_NAME = ProxyConsts.AlgSelectionEnum.ALG_UNKNOWN;
    final ProxyDao proxyDao;

    AlgorithmResolver(ProxyDao proxyDao) {
        this.proxyDao = proxyDao;
    }

    public List<Proxy> findByAlgSelection(ProxyRequest proxyRequest) {
        List<Proxy> proxyListByAlgorithm;
        log.info("ALGORITHM_RESOLVER.FIND_BY_ALG_SELECTION: started resolving algorithm with name: {}", proxyRequest.getAlgSelection());

        if (ProxyConsts.AlgSelectionEnum.
                ALG_MIN_USAGE.isEqualByProxyRequest(proxyRequest)) {

            proxyListByAlgorithm = proxyDao.findMinUsedProxy(proxyRequest);
        } else if (ProxyConsts.AlgSelectionEnum.
                ALG_LONGEST_NOT_USED.isEqualByProxyRequest(proxyRequest)) {

            proxyListByAlgorithm = proxyDao.findLongestNotUsedProxy(proxyRequest);
        } else if (ProxyConsts.AlgSelectionEnum.
                ALG_NEWEST_ADDED.isEqualByProxyRequest(proxyRequest)) {

            proxyListByAlgorithm = proxyDao.findNewestAddedProxy(proxyRequest);
        } else {

            proxyListByAlgorithm = proxyDao.findProxy(proxyRequest);
        }

        return proxyListByAlgorithm;
    }
}