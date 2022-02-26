package org.dbs24.tik.mobile.repo;

import org.dbs24.tik.mobile.entity.domain.OrderHist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderHistRepo extends JpaRepository<OrderHist, Integer> {

}
