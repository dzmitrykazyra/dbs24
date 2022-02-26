package org.dbs24.card.pmt.repo;

import org.dbs24.card.pmt.entity.GooglePaymentHist;
import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GooglePaymentHistRepo extends ApplicationJpaRepository<GooglePaymentHist, Integer>, JpaSpecificationExecutor<GooglePaymentHist> {
}
