/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.dao.impl;

import java.util.Collection;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.DaoAbstractApplicationService;

import org.dbs24.tik.assist.constant.CacheKey;
import org.dbs24.tik.assist.constant.reference.PhoneStatusDefine;
import org.dbs24.tik.assist.dao.PhoneDao;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.entity.domain.Phone;
import org.dbs24.tik.assist.entity.domain.PhoneUsage;
import org.dbs24.tik.assist.repo.PhoneRepo;
import org.dbs24.tik.assist.repo.PhoneUsageRepo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Data
@Log4j2
@Component
public class PhoneDaoImpl extends DaoAbstractApplicationService implements PhoneDao {

    final PhoneRepo phoneRepo;
    final PhoneUsageRepo phoneUsageRepo;
    final ReferenceDao referenceDao;

    public PhoneDaoImpl(PhoneRepo phonePhoneRepo, PhoneUsageRepo phoneUsageRepo, ReferenceDao referenceDao) {

        this.phoneRepo = phonePhoneRepo;
        this.referenceDao = referenceDao;
        this.phoneUsageRepo = phoneUsageRepo;
    }

    @Override
    @CacheEvict(value = {CacheKey.CACHE_PHONE_USAGE, CacheKey.CACHE_ACTUAL_PHONES_USAGES}, allEntries = true, beforeInvocation = true)
    public void savePhoneUsage(PhoneUsage phoneUsage) {

        phoneUsageRepo.save(phoneUsage);
    }

    @Override
    @Cacheable(CacheKey.CACHE_PHONE)
    public Phone findPhone(Integer phoneId) {

        return phoneRepo
                .findById(phoneId)
                .orElseThrow(() -> new RuntimeException(String.format("phoneId not found (%d)", phoneId)));
    }

    @Override
    @Cacheable(CacheKey.CACHE_ACTUAL_PHONES)
    public Collection<Phone> findActualPhones() {

        return phoneRepo.findByPhoneStatus(referenceDao.findPhoneStatusById(PhoneStatusDefine.PS_ACTUAL.getId()));
    }

    @Override
    @Cacheable(CacheKey.CACHE_ACTUAL_PHONES_USAGES)
    public Collection<PhoneUsage> findActualPhoneUsages() {

        return phoneUsageRepo.findActualPhoneUsages();
    }

    @Override
    @CacheEvict(value = {CacheKey.CACHE_PHONE, CacheKey.CACHE_ACTUAL_PHONES}, allEntries = true, beforeInvocation = true)
    public void savePhone(Phone phonePhone) {

        phoneRepo.save(phonePhone);
    }

    @Override
    @Cacheable(CacheKey.CACHE_PHONE_USAGE)
    public PhoneUsage findPhoneUsage(Integer phoneUsageId) {
        
        return phoneUsageRepo
                .findById(phoneUsageId)
                .orElseThrow(() -> new RuntimeException(String.format("phoneUsageId not found (%d)", phoneUsageId)));
    }    
}
