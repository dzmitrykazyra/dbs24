/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.dao;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.dbs24.tik.dev.entity.TariffPlan;
import org.dbs24.tik.dev.entity.TariffPlanHist;
import org.dbs24.tik.dev.repo.TariffPlanHistRepo;
import org.dbs24.tik.dev.repo.TariffPlanRepo;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Getter
@Log4j2
@Component
public class TariffPlanDao extends DaoAbstractApplicationService {

    final TariffPlanRepo tariffPlanRepo;
    final TariffPlanHistRepo tariffPlanHistRepo;

    public TariffPlanDao(TariffPlanRepo tariffPlanRepo, TariffPlanHistRepo tariffPlanHistRepo) {
        this.tariffPlanRepo = tariffPlanRepo;
        this.tariffPlanHistRepo = tariffPlanHistRepo;
    }

    //==========================================================================
    public Optional<TariffPlan> findOptionalTariffPlan(Long grantId) {
        return tariffPlanRepo.findById(grantId);
    }

    public TariffPlan findTariffPlan(Long tariffPlanId) {
        return findOptionalTariffPlan(tariffPlanId).orElseThrow();
    }

    public void saveTariffPlanHist(TariffPlanHist tariffPlanHist) {
        tariffPlanHistRepo.save(tariffPlanHist);
    }

    public void saveTariffPlan(TariffPlan tariffPlan) {
        tariffPlanRepo.save(tariffPlan);
    }

}
