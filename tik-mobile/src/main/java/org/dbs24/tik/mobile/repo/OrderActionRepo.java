package org.dbs24.tik.mobile.repo;

import org.dbs24.tik.mobile.entity.domain.OrderAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderActionRepo extends JpaRepository<OrderAction, Integer> {

}
