package org.dbs24.card.pmt.repo;

import org.dbs24.card.pmt.entity.ApplePaymentHist;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ApplyPaymentHistRepo extends ApplicationJpaRepository<ApplePaymentHist, Integer>, JpaSpecificationExecutor<ApplePaymentHist> {
}
