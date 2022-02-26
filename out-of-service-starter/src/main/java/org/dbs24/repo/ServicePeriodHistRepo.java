/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.repo;

import org.dbs24.entity.ServicePeriodHist;
import org.dbs24.spring.core.data.ApplicationJpaRepository;

import java.time.LocalDateTime;

public interface ServicePeriodHistRepo extends ApplicationJpaRepository<ServicePeriodHist, LocalDateTime> {

}
