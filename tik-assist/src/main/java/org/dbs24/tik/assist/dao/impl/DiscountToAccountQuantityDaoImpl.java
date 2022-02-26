package org.dbs24.tik.assist.dao.impl;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.dao.DiscountToAccountQuantityDao;
import org.dbs24.tik.assist.entity.domain.DiscountToAccountsQuantity;
import org.dbs24.tik.assist.repo.DiscountToAccountsQuantityRepo;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Data
@Log4j2
@Component
public class DiscountToAccountQuantityDaoImpl implements DiscountToAccountQuantityDao {

    final DiscountToAccountsQuantityRepo discountToAccountsQuantityRepo;

    public DiscountToAccountQuantityDaoImpl(DiscountToAccountsQuantityRepo discountToAccountsQuantityRepo) {

        this.discountToAccountsQuantityRepo = discountToAccountsQuantityRepo;
    }

    @Override
    public DiscountToAccountsQuantity saveDiscountToAccountsQuantity(DiscountToAccountsQuantity discountToAccountsQuantity) {

        return discountToAccountsQuantityRepo.save(discountToAccountsQuantity);
    }

    @Override
    public List<DiscountToAccountsQuantity> findAll() {

        return discountToAccountsQuantityRepo.findAll();
    }

    @Override
    public DiscountToAccountsQuantity findByAccountsQuantity(Integer accountsQuantity) {

        return discountToAccountsQuantityRepo
                .findByUpToAccountsQuantityGreaterThan(accountsQuantity)
                .stream()
                .min(Comparator.comparing(DiscountToAccountsQuantity::getUpToAccountsQuantity))
                .orElseGet(this::createZeroDiscountObject);
    }

    private DiscountToAccountsQuantity createZeroDiscountObject() {

        log.warn("Cannot find available DiscountToAccountsQuantity record in db");

        return DiscountToAccountsQuantity.builder()
                .discount(0)
                .build();
    }
}
