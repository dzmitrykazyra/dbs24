/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.repo;

import java.time.LocalDateTime;
import org.dbs24.insta.tmp.entity.Source;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

public interface SourceRepo extends ApplicationJpaRepository<Source, Long>, JpaSpecificationExecutor<Source>, PagingAndSortingRepository<Source, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO SOURCES (SOURCE_ID, POST_ID, SOURCE_STATUS_ID, ACTUAL_DATE, SOURCE_URL, SOURCE_HASH, MAIN_FACE_BOX) VALUES (nextval('seq_sources'), ?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
    void bulkInsert(Long POST_ID, Integer SOURCE_STATUS_ID, LocalDateTime ACTUAL_DATE, String SOURCE_URL, String SOURCE_HASH, byte[] MAIN_FACE_BOX);

}
