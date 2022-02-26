/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.dao;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.dbs24.tik.dev.entity.TariffPlanPrice;
import org.dbs24.tik.dev.entity.TariffPlanPriceHist;
import org.dbs24.tik.dev.repo.TariffPlanPriceHistRepo;
import org.dbs24.tik.dev.repo.TariffPlanPriceRepo;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Getter
@Log4j2
@Component
public class TariffPlanPriceDao extends DaoAbstractApplicationService {

    final TariffPlanPriceRepo tariffPlanPriceRepo;
    final TariffPlanPriceHistRepo tariffPlanPriceHistRepo;

    public TariffPlanPriceDao(TariffPlanPriceRepo tariffPlanPriceRepo, TariffPlanPriceHistRepo tariffPlanPriceHistRepo) {
        this.tariffPlanPriceRepo = tariffPlanPriceRepo;
        this.tariffPlanPriceHistRepo = tariffPlanPriceHistRepo;
    }

    //==========================================================================
    public Optional<TariffPlanPrice> findOptionalTariffPlanPrice(Long grantId) {
        return tariffPlanPriceRepo.findById(grantId);
    }

    public TariffPlanPrice findTariffPlanPrice(Long tariffPlanPriceId) {
        return findOptionalTariffPlanPrice(tariffPlanPriceId).orElseThrow();
    }

    public void saveTariffPlanPriceHist(TariffPlanPriceHist tariffPlanPriceHist) {
        tariffPlanPriceHistRepo.save(tariffPlanPriceHist);
    }

    public void saveTariffPlanPrice(TariffPlanPrice tariffPlanPrice) {
        tariffPlanPriceRepo.save(tariffPlanPrice);
    }

}
