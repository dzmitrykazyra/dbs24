/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.jpa.spec;

import java.util.Collection;
import org.dbs24.entity.ContractStatus;
import org.dbs24.entity.User;
import org.dbs24.entity.UserContract;
import org.springframework.data.jpa.domain.Specification;

@FunctionalInterface
public interface ActualUserContracts {
    Specification<UserContract> getSpecification(User user, Collection<ContractStatus> cs);
}
