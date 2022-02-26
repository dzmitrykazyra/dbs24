package org.dbs24.tik.mobile.repo;

import org.dbs24.tik.mobile.entity.domain.OrderActionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderActionTypeRepo extends JpaRepository<OrderActionType, Integer> {
    Optional<OrderActionType> findByOrderActionTypeId(Integer orderActionTypeId);

    Optional<OrderActionType> findByOrderActionTypeName(String actionTypeName);
}
