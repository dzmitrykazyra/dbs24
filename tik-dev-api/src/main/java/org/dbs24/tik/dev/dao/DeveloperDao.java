/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.dao;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.dbs24.tik.dev.entity.Developer;
import org.dbs24.tik.dev.entity.DeveloperHist;
import org.dbs24.tik.dev.repo.DeveloperHistRepo;
import org.dbs24.tik.dev.repo.DeveloperRepo;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Getter
@Log4j2
@Component
public class DeveloperDao extends DaoAbstractApplicationService {

    final DeveloperRepo developerRepo;
    final DeveloperHistRepo developerHistRepo;

    public DeveloperDao(DeveloperRepo developerRepo, DeveloperHistRepo developerHistRepo) {
        this.developerRepo = developerRepo;
        this.developerHistRepo = developerHistRepo;
    }

    //==========================================================================
    public Optional<Developer> findOptionalDeveloper(Long developerId) {
        return developerRepo.findById(developerId);
    }

    public Developer findDeveloper(Long developerId) {
        return findOptionalDeveloper(developerId).orElseThrow();
    }

    public void saveDeveloperHist(DeveloperHist developerHist) {
        developerHistRepo.save(developerHist);
    }

    public void saveDeveloper(Developer developer) {
        developerRepo.save(developer);
    }

}
