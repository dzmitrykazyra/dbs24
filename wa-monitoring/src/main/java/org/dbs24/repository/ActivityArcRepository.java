/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.repository;

import org.dbs24.entity.SubscriptionActivity;
import org.dbs24.entity.SubscriptionActivityArc;
import org.dbs24.spring.core.data.ApplicationJpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;

public interface ActivityArcRepository extends ApplicationJpaRepository<SubscriptionActivityArc, Long> {

    Collection<SubscriptionActivityArc> findByActualDateBefore(LocalDateTime localDateTime);

}
