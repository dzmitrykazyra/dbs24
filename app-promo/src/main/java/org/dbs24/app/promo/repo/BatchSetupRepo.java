/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.repo;

import org.dbs24.app.promo.entity.BatchSetup;
import org.dbs24.app.promo.entity.BatchTemplate;
import org.dbs24.spring.core.data.ApplicationJpaRepository;

import java.util.Collection;

public interface BatchSetupRepo extends ApplicationJpaRepository<BatchSetup, Integer> {

    Collection<BatchSetup> findByBatchTemplate(BatchTemplate batchTemplate);
    
}
