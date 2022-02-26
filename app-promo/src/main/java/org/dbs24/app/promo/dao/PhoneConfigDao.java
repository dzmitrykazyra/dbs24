package org.dbs24.app.promo.dao;

import lombok.extern.log4j.Log4j2;
import org.dbs24.app.promo.repo.PhoneConfigRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class PhoneConfigDao {
    private final PhoneConfigRepo phoneConfigRepo;

    @Autowired
    public PhoneConfigDao(PhoneConfigRepo phoneConfigRepo) {
        this.phoneConfigRepo = phoneConfigRepo;
    }
}
