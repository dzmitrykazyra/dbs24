/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.dao;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.dbs24.tik.dev.entity.TikAccount;
import org.dbs24.tik.dev.entity.TikAccountHist;
import org.dbs24.tik.dev.repo.TikAccountHistRepo;
import org.dbs24.tik.dev.repo.TikAccountRepo;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Getter
@Log4j2
@Component
public class TikAccountDao extends DaoAbstractApplicationService {

    final TikAccountRepo tikAccountRepo;
    final TikAccountHistRepo tikAccountHistRepo;

    public TikAccountDao(TikAccountRepo tikAccountRepo, TikAccountHistRepo tikAccountHistRepo) {
        this.tikAccountRepo = tikAccountRepo;
        this.tikAccountHistRepo = tikAccountHistRepo;
    }

    //==========================================================================
    public Optional<TikAccount> findOptionalTikAccount(Long tikAccountId) {
        return tikAccountRepo.findById(tikAccountId);
    }

    public TikAccount findTikAccount(Long tikAccountId) {
        return findOptionalTikAccount(tikAccountId).orElseThrow();
    }

    public void saveTikAccountHist(TikAccountHist tikAccountHist) {
        tikAccountHistRepo.save(tikAccountHist);
    }

    public void saveTikAccount(TikAccount tikAccount) {
        tikAccountRepo.save(tikAccount);
    }

}
