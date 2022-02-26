package org.dbs24.tik.assist.dao;

import org.dbs24.tik.assist.entity.domain.Phone;
import org.dbs24.tik.assist.entity.domain.PhoneUsage;

import java.util.Collection;

public interface PhoneDao {

    void savePhone(Phone phonePhone);
    void savePhoneUsage(PhoneUsage phoneUsage);

    Phone findPhone(Integer phoneId);
    PhoneUsage findPhoneUsage(Integer phoneUsageId);

    Collection<Phone> findActualPhones();
    Collection<PhoneUsage> findActualPhoneUsages();
}
