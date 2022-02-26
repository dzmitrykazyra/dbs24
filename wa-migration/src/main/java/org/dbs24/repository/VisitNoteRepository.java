/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.repository;

import org.dbs24.entity.VisitNote;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VisitNoteRepository extends ApplicationJpaRepository<VisitNote, Long>, JpaSpecificationExecutor<VisitNote>, PagingAndSortingRepository<VisitNote, Long> {

}
