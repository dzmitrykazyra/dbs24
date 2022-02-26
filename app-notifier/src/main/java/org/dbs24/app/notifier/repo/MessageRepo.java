/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.notifier.repo;

import org.dbs24.app.notifier.entity.Message;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;

public interface MessageRepo extends ApplicationJpaRepository<Message, Integer> {

    @Query(value = "select * from note_messages where create_date >= :SD or :CD between actual_date_from and actual_date_to ", nativeQuery = true)
    Collection<Message> findLatestMessages(@Param("SD") LocalDateTime sinceDate, @Param("CD") LocalDateTime currentDate);

}
