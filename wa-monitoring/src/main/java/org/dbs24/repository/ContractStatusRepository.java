/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.repository;

import org.dbs24.entity.ContractStatus;
import org.dbs24.spring.core.data.ApplicationJpaRepository;

public interface ContractStatusRepository extends ApplicationJpaRepository<ContractStatus, Byte> {
    
}
