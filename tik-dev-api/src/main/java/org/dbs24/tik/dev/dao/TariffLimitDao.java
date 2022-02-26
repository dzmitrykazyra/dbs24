/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.dao;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.dbs24.tik.dev.entity.TariffLimit;
import org.dbs24.tik.dev.entity.TariffLimitHist;
import org.dbs24.tik.dev.repo.TariffLimitHistRepo;
import org.dbs24.tik.dev.repo.TariffLimitRepo;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Getter
@Log4j2
@Component
public class TariffLimitDao extends DaoAbstractApplicationService {

    final TariffLimitRepo tariffLimitRepo;
    final TariffLimitHistRepo tariffLimitHistRepo;

    public TariffLimitDao(TariffLimitRepo tariffLimitRepo, TariffLimitHistRepo tariffLimitHistRepo) {
        this.tariffLimitRepo = tariffLimitRepo;
        this.tariffLimitHistRepo = tariffLimitHistRepo;
    }

    //==========================================================================
    public Optional<TariffLimit> findOptionalTariffLimit(Long grantId) {
        return tariffLimitRepo.findById(grantId);
    }

    public TariffLimit findTariffLimit(Long tariffLimitId) {
        return findOptionalTariffLimit(tariffLimitId).orElseThrow();
    }

    public void saveTariffLimitHist(TariffLimitHist tariffLimitHist) {
        tariffLimitHistRepo.save(tariffLimitHist);
    }

    public void saveTariffLimit(TariffLimit tariffLimit) {
        tariffLimitRepo.save(tariffLimit);
    }

}
