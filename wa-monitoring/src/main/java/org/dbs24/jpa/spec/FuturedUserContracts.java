package org.dbs24.jpa.spec;

import org.dbs24.entity.ContractStatus;
import org.dbs24.entity.UserContract;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

@FunctionalInterface
public interface FuturedUserContracts {
    Specification<UserContract> getSpecification(Collection<ContractStatus> cs);
}
