package org.dbs24.proxy.core.dao;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.consts.ProxyConsts;
import org.dbs24.proxy.core.entity.domain.ApplicationNetwork;
import org.dbs24.proxy.core.entity.domain.Proxy;
import org.dbs24.proxy.core.entity.domain.ProxyUsageBan;
import org.dbs24.proxy.core.entity.domain.ProxyUsageError;
import org.dbs24.proxy.core.entity.dto.ProxyUsageBanDto;
import org.dbs24.proxy.core.repo.ProxyUsageBanRepo;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Component
public class ProxyUsageBanDao extends DaoAbstractApplicationService {

    private final String BAN_ERROR_TYPE_NAME = ProxyConsts.ErrorTypeEnum.ET_BAN.getValue();

    final ProxyUsageBanRepo proxyUsageBanRepo;
    final ProxyUsageDao proxyUsageDao;
    final ApplicationDao applicationDao;
    final ProxyUsageErrorDao proxyUsageErrorDao;
    final ReferenceDao referenceDao;

    public ProxyUsageBanDao(ProxyUsageBanRepo proxyUsageBanRepo, ProxyUsageDao proxyUsageDao, ApplicationDao applicationDao, ProxyUsageErrorDao proxyUsageErrorDao, ReferenceDao referenceDao) {
        this.proxyUsageBanRepo = proxyUsageBanRepo;
        this.proxyUsageDao = proxyUsageDao;
        this.applicationDao = applicationDao;
        this.proxyUsageErrorDao = proxyUsageErrorDao;
        this.referenceDao = referenceDao;
    }

    public ProxyUsageBan save(ProxyUsageBan proxyUsageBan) {

        return proxyUsageBanRepo.save(proxyUsageBan);
    }

    public List<Proxy> findBannedProxyIdsByApplicationNetwork(ApplicationNetwork applicationNetwork) {

        return proxyUsageBanRepo.findByApplicationNetworkAndBannedUntilDateGreaterThan(applicationNetwork, LocalDateTime.now())
                .stream()
                .map(ProxyUsageBan::getProxy)
                .collect(Collectors.toList());
    }

    public boolean isBanned(Proxy proxy) {

        return !proxyUsageBanRepo.findByProxyAndBannedUntilDateGreaterThan(proxy, LocalDateTime.now()).isEmpty();
    }

    public ProxyUsageError createBanTemplateUsageError(ProxyUsageBanDto proxyUsageBanDto) {

        return StmtProcessor.create(
                ProxyUsageError.class,
                proxyUsageError -> {

                    proxyUsageError.setProxyUsage(
                            proxyUsageDao.findByProxyUsageId(proxyUsageBanDto.getProxyUsageId())
                                    .orElseThrow(() -> new RuntimeException("No usage with such id " + proxyUsageBanDto.getProxyUsageId()))
                    );
                    proxyUsageError.setActualDate(LocalDateTime.now());
                    proxyUsageError.setErrorType(referenceDao.findErrorTypeByName(BAN_ERROR_TYPE_NAME).orElseThrow(() -> new RuntimeException("Cannot find ban record in db")));
                    proxyUsageError.setLog(proxyUsageBanDto.getLog());
                    proxyUsageError.setApplication(applicationDao.findByProxyUsageId(proxyUsageBanDto.getProxyUsageId()));
                }
        );
    }
}
