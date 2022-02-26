/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.jpa.spec;

import org.dbs24.entity.VisitNote;
import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;

@FunctionalInterface
public interface VisitNoteAddTime {

    Specification<VisitNote> setFilter(LocalDateTime from, LocalDateTime to);
}
