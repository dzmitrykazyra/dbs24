package org.dbs24.tik.dev.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.tik.dev.entity.ContractHist;
import org.dbs24.tik.dev.entity.ContractHistPK;

public interface ContractHistRepo extends ApplicationJpaRepository<ContractHist, ContractHistPK> {

}
