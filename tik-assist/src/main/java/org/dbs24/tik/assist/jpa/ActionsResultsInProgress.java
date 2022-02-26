/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.jpa;

import java.util.Collection;
import org.springframework.data.jpa.domain.Specification;
import org.dbs24.tik.assist.entity.domain.OrderAction;
import org.dbs24.tik.assist.entity.domain.OrderActionResult;


@FunctionalInterface
public interface ActionsResultsInProgress {
    Specification<OrderAction> getSpecification(Collection<OrderActionResult> cs);
}

