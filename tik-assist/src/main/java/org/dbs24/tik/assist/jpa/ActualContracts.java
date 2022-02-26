/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.jpa;

import java.util.Collection;
import org.dbs24.tik.assist.entity.domain.OrderStatus;
import org.dbs24.tik.assist.entity.domain.OrderAction;
import org.springframework.data.jpa.domain.Specification;

@FunctionalInterface
public interface ActualContracts {

    Specification<OrderAction> getSpecification(Collection<OrderStatus> cs);
}
