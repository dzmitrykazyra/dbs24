package org.dbs24.tik.assist.dao.impl;

import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;
import org.dbs24.tik.assist.dao.DepositPaymentDao;
import org.dbs24.tik.assist.entity.domain.DepositPayment;
import org.dbs24.tik.assist.repo.DepositPaymentRepo;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class DepositPaymentDaoImpl extends DaoAbstractApplicationService implements DepositPaymentDao {

    final DepositPaymentRepo depositPaymentRepo;

    public DepositPaymentDaoImpl(DepositPaymentRepo depositPaymentRepo) {
        this.depositPaymentRepo = depositPaymentRepo;
    }

    @Override
    public DepositPayment findDepositPaymentById(Integer depositPaymentId) {

        return depositPaymentRepo
                .findById(depositPaymentId)
                .orElseThrow(() -> new RuntimeException(
                        "Cannot find deposit by id: " + depositPaymentId
                ));
    }

    @Override
    public DepositPayment save(DepositPayment depositPaymentToSave) {

        return depositPaymentRepo.save(depositPaymentToSave);
    }
}
