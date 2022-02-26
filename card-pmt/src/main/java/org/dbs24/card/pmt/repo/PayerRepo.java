/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.card.pmt.repo;

import org.dbs24.card.pmt.entity.Payer;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface PayerRepo extends ApplicationJpaRepository<Payer, Integer>, JpaSpecificationExecutor<Payer> {
    public Optional<Payer> findByLoginToken(String loginToken);
}
