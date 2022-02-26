/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.service.phone;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Map;
import java.util.Collection;
import javax.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.constant.reference.PhoneStatusDefine;
import org.dbs24.tik.assist.dao.PhoneDao;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.entity.domain.Phone;
import org.dbs24.tik.assist.entity.domain.PhoneUsage;
import org.dbs24.tik.assist.entity.dto.phone.CreatedPhoneDto;
import org.dbs24.tik.assist.entity.dto.phone.CreatedPhoneUsageDto;
import org.dbs24.tik.assist.entity.dto.phone.PhoneUsageDto;
import org.dbs24.tik.assist.entity.dto.phone.PhoneDto;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class PhonesService extends AbstractApplicationService {

    final Map<Integer, LocalDateTime> phoneIdToLastUsageDateTime = ServiceFuncs.createConcurencyMap();

    final ReferenceDao referenceDao;
    final PhoneDao phoneDao;

    public PhonesService(ReferenceDao referenceDao, PhoneDao phoneDao) {

        this.referenceDao = referenceDao;
        this.phoneDao = phoneDao;
    }

    @PostConstruct
    public void initializeUsingMap() {

        final Collection<PhoneUsage> storedLastPhoneUsages = findActualPhoneUsages();

        findActualPhones().stream().forEach(phone -> {

            final LocalDateTime lastUsed = storedLastPhoneUsages
                    .stream()
                    .filter(pu -> pu.getPhone().getPhoneId().equals(phone.getPhoneId()))
                    .findFirst()
                    .map(m -> m.getActualDate())
                    .orElse(phone.getActualDate());

            phoneIdToLastUsageDateTime.put(phone.getPhoneId(), lastUsed);

        });

        log.info("Actual phones size : {}", phoneIdToLastUsageDateTime.size());
    }

    public CreatedPhoneDto createOrUpdatePhone(PhoneDto phoneDto) {

        final Phone phone = findOrCreatePhone(phoneDto.getPhoneId());

        phone.setActualDate(NLS.long2LocalDateTime(phoneDto.getActualDate()));
        phone.setPhoneStatus(referenceDao.findPhoneStatusById(phoneDto.getPhoneStatusId()));
        phone.setApkAttrs(phoneDto.getApkAttrs());
        phone.setApkHashId(phoneDto.getApkHashId());
        phone.setDeviceId(phoneDto.getDeviceId());
        phone.setInstallId(phoneDto.getInstallId());

        // save phone
        phoneDao.savePhone(phone);

        if (phoneDto.getPhoneStatusId().equals(PhoneStatusDefine.PS_ACTUAL.getId())) {
            // add using mark
            addUsingMark(phone.getPhoneId());
        }

        if (phoneDto.getPhoneStatusId().equals(PhoneStatusDefine.PS_BANNED.getId())) {
            // remove using mark
            removeUsingMark(phone.getPhoneId());
        }

        return StmtProcessor.create(CreatedPhoneDto.class, cp -> {

            cp.setCreatedPhoneId(phone.getPhoneId());

            log.debug("created/update phonePhone: {}", cp);

        });
    }

    public CreatedPhoneUsageDto createOrUpdatePhoneUsage(PhoneUsageDto phoneUsageDto) {

        final PhoneUsage phoneUsage = findOrCreatePhoneUsage(phoneUsageDto.getPhoneUsageId());

        phoneUsage.setPhoneUsageId(phoneUsageDto.getPhoneUsageId());
        phoneUsage.setActualDate(NLS.long2LocalDateTime(phoneUsageDto.getActualDate()));
        phoneUsage.setIsSuccess(phoneUsageDto.getIsSuccess());
        phoneUsage.setPhone(findPhone(phoneUsageDto.getPhoneId()));

        phoneDao.savePhoneUsage(phoneUsage);
        return StmtProcessor.create(CreatedPhoneUsageDto.class, ca -> {

            ca.setCreatedPhoneUsageId(phoneUsage.getPhoneUsageId());

            log.debug("created/update phoneUsage: {}", phoneUsage);

        });
    }

    public PhoneDto getPhone(Integer phoneId) {

        return PhoneDto.toPhoneDto(findPhone(phoneId));
    }

    public PhoneDto getLongestNotUsedPhone() {

        //log.info("try 2 findLeastUsedPhone");
        final Integer availablePhone = phoneIdToLastUsageDateTime
                .entrySet()
                .stream()
                .sorted((a, b) -> a.getValue().compareTo(b.getValue()))
                .limit(3)
                .findFirst()
                .map(m -> m.getKey())
                .orElseThrow(() -> new RuntimeException("No available phones found"));

        final PhoneDto phoneDto = PhoneDto.toPhoneDto(findPhone(availablePhone));

        addUsingMark(availablePhone);

        return phoneDto;

    }

    public void addUsingMark(Integer phoneId) {

        phoneIdToLastUsageDateTime.putIfAbsent(phoneId, LocalDateTime.now());

        phoneIdToLastUsageDateTime
                .entrySet()
                .stream()
                .filter(phone -> phone.getKey().equals(phoneId))
                .findFirst()
                .orElseThrow()
                .setValue(LocalDateTime.now());

        log.info("Add using mark 4 phone {}; {} phone(s)", phoneId, phoneIdToLastUsageDateTime.size());

    }

    public void removeUsingMark(Integer phoneId) {

        phoneIdToLastUsageDateTime
                .entrySet()
                .removeIf(es -> es.getKey().equals(phoneId));

        log.warn("Remove using mark 4 phone {}; {} phone(s)", phoneId, phoneIdToLastUsageDateTime.size());
    }

    public Phone createPhone() {
        return StmtProcessor.create(Phone.class, a -> a.setActualDate(LocalDateTime.now()));
    }

    public Phone findPhone(Integer phoneId) {

        return phoneDao.findPhone(phoneId);
    }

    public Phone findOrCreatePhone(Integer phoneId) {
        return (Optional.ofNullable(phoneId)
                .orElseGet(() -> 0) > 0)
                ? findPhone(phoneId)
                : createPhone();
    }

    public Collection<Phone> findActualPhones() {

        return phoneDao.findActualPhones();
    }

    public Collection<PhoneUsage> findActualPhoneUsages() {

        return phoneDao.findActualPhoneUsages();
    }

    public PhoneUsage createPhoneUsage() {
        return StmtProcessor.create(PhoneUsage.class, a -> a.setActualDate(LocalDateTime.now()));
    }

    public PhoneUsage findPhoneUsage(Integer phoneUsageId) {

        return phoneDao.findPhoneUsage(phoneUsageId);
    }

    public PhoneUsage findOrCreatePhoneUsage(Integer phoneUsageId) {
        return (Optional.ofNullable(phoneUsageId)
                .orElseGet(() -> Integer.valueOf("0")) > 0)
                ? findPhoneUsage(phoneUsageId)
                : createPhoneUsage();
    }
}
