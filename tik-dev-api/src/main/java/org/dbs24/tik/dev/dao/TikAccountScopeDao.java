/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.dao;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.dbs24.tik.dev.entity.TikAccountScope;
import org.dbs24.tik.dev.entity.TikAccountScopeHist;
import org.dbs24.tik.dev.repo.TikAccountScopeHistRepo;
import org.dbs24.tik.dev.repo.TikAccountScopeRepo;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Getter
@Log4j2
@Component
public class TikAccountScopeDao extends DaoAbstractApplicationService {

    final TikAccountScopeRepo tikAccountScopeRepo;
    final TikAccountScopeHistRepo tikAccountScopeHistRepo;

    public TikAccountScopeDao(TikAccountScopeRepo tikAccountScopeRepo, TikAccountScopeHistRepo tikAccountScopeHistRepo) {
        this.tikAccountScopeRepo = tikAccountScopeRepo;
        this.tikAccountScopeHistRepo = tikAccountScopeHistRepo;
    }

    //==========================================================================
    public Optional<TikAccountScope> findOptionalTikAccountScope(Long grantId) {
        return tikAccountScopeRepo.findById(grantId);
    }

    public TikAccountScope findTikAccountScope(Long grantId) {
        return findOptionalTikAccountScope(grantId).orElseThrow();
    }

    public void saveTikAccountScopeHist(TikAccountScopeHist tikAccountScopeHist) {
        tikAccountScopeHistRepo.save(tikAccountScopeHist);
    }

    public void saveTikAccountScope(TikAccountScope tikAccountScope) {
        tikAccountScopeRepo.save(tikAccountScope);
    }

}
