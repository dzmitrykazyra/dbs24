package org.dbs24.proxy.core.dao;

import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.consts.ProxyConsts;
import org.dbs24.proxy.core.entity.domain.ErrorType;
import org.dbs24.proxy.core.entity.domain.ProxyUsageError;
import org.dbs24.proxy.core.entity.dto.ProxyUsageErrorDto;
import org.dbs24.proxy.core.repo.ErrorTypeRepo;
import org.dbs24.proxy.core.repo.ProxyUsageErrorRepo;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Log4j2
@Component
public class ProxyUsageErrorDao {

    final ProxyUsageDao proxyUsageDao;
    final ReferenceDao referenceDao;
    final ApplicationDao applicationDao;

    final ProxyUsageErrorRepo proxyUsageErrorRepo;
    final ErrorTypeRepo errorTypeRepo;

    private final String OTHER_ERROR_TYPE_NAME = ProxyConsts.ErrorTypeEnum.ET_OTHER.getValue();

    public ProxyUsageErrorDao(ProxyUsageDao proxyUsageDao, ReferenceDao referenceDao, ApplicationDao applicationDao, ProxyUsageErrorRepo proxyUsageErrorRepo, ErrorTypeRepo errorTypeRepo) {
        this.proxyUsageDao = proxyUsageDao;
        this.referenceDao = referenceDao;
        this.applicationDao = applicationDao;
        this.proxyUsageErrorRepo = proxyUsageErrorRepo;
        this.errorTypeRepo = errorTypeRepo;
    }

    public ProxyUsageError save(ProxyUsageError proxyUsageError) {

        return proxyUsageErrorRepo.save(proxyUsageError);
    }

    public ProxyUsageError toProxyUsageError(ProxyUsageErrorDto proxyUsageErrorDto) {

        return StmtProcessor.create(
                ProxyUsageError.class,
                proxyUsageError -> {

                    proxyUsageError.setProxyUsage(
                            proxyUsageDao.findByProxyUsageId(proxyUsageErrorDto.getProxyUsageId())
                                    .orElseThrow(() -> new RuntimeException("No usage with such id " + proxyUsageErrorDto.getProxyUsageId()))
                    );
                    proxyUsageError.setActualDate(LocalDateTime.now());
                    proxyUsageError.setErrorType(referenceDao.findErrorTypeByName(proxyUsageErrorDto.getErrorTypeName()).orElse(findDefaultErrorType()));
                    proxyUsageError.setLog(proxyUsageErrorDto.getLog());
                    proxyUsageError.setApplication(applicationDao.findByProxyUsageId(proxyUsageErrorDto.getProxyUsageId()));
                }
        );
    }

    public ErrorType findDefaultErrorType() {

        return errorTypeRepo.findByErrorTypeName(OTHER_ERROR_TYPE_NAME)
                .orElseThrow(() -> new RuntimeException("Default error type record does not exist in db"));
    }
}
