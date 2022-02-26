/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.repo;

import org.dbs24.insta.tmp.entity.Bot;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.Collection;
import org.dbs24.insta.tmp.entity.BotStatus;
public interface BotRepo extends ApplicationJpaRepository<Bot, Integer>, JpaSpecificationExecutor<Bot>, PagingAndSortingRepository<Bot, Integer> {
    
    Collection<Bot> findByBotStatus(BotStatus botStatus);
}
