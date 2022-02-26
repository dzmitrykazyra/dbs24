package org.dbs24.tik.assist.dao;

import org.dbs24.tik.assist.entity.domain.DepositPayment;

public interface DepositPaymentDao {

    DepositPayment findDepositPaymentById(Integer depositPaymentId);
    DepositPayment save(DepositPayment depositPaymentToSave);
}
