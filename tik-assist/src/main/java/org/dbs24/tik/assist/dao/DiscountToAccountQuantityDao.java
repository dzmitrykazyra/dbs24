package org.dbs24.tik.assist.dao;

import org.dbs24.tik.assist.entity.domain.DiscountToAccountsQuantity;

import java.util.List;

public interface DiscountToAccountQuantityDao {

    DiscountToAccountsQuantity saveDiscountToAccountsQuantity(DiscountToAccountsQuantity discountToAccountsQuantity);

    List<DiscountToAccountsQuantity> findAll();

    DiscountToAccountsQuantity findByAccountsQuantity(Integer accountsQuantity);
}
