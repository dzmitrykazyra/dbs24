package org.dbs24.card.pmt.repo;

import org.dbs24.card.pmt.entity.AbstractPaymentHist;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PaymentHistRepo extends ApplicationJpaRepository<AbstractPaymentHist, Integer>, JpaSpecificationExecutor<AbstractPaymentHist> {
}
