package org.dbs24.tik.assist.dao.impl;

import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.dao.PromocodeDao;
import org.dbs24.tik.assist.entity.domain.Promocode;
import org.dbs24.tik.assist.repo.PromocodeRepo;
import org.dbs24.tik.assist.service.exception.NoSuchDataInDaoException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
@Component
public class PromocodeDaoImpl implements PromocodeDao {

    final PromocodeRepo promocodeRepo;

    public PromocodeDaoImpl(PromocodeRepo promocodeRepo) {

        this.promocodeRepo = promocodeRepo;
    }

    @Override
    public Promocode activatePromocode(Integer userId, String promocodeValue) {
        Optional<Promocode> promocodeOptional = promocodeRepo.findByPromocodeValue(promocodeValue);
        AtomicReference<Promocode> atomicValidPromocode = new AtomicReference<>(null);

        promocodeOptional.ifPresent(
                promocode -> StmtProcessor.ifTrue(
                        verifyPromocodeForUser(promocode, userId),
                        () -> {
                            invalidatePromocode(promocode);
                            atomicValidPromocode.set(promocode);
                        }
                )
        );

        return atomicValidPromocode.get();
    }

    /**
     * Method allows validating promocode by few parameters:
     *      1) promocode isActive boolean variable
     *      2) if promocode one-time to target user
     *      3) if promocode time duration is valid
     * Also method allows invalidating one-time for user or expired promocodes
     * @return boolean variable contaiting is promocode valid value
     */
    @Override
    public boolean verifyPromocodeForUser(Promocode promocode, Integer userId) {

        return promocode.getIsActive()
                && promocode.getUser() != null
                && verifyPromocodeByUser(promocode, userId)
                && verifyPromocodeByItsTimeDuration(promocode);
    }

    @Override
    public boolean verifyPromocodeValueForUser(String promocodeValue, Integer userId) {

        return verifyPromocodeForUser(
                promocodeRepo.findByPromocodeValue(promocodeValue).orElseThrow(() -> new NoSuchDataInDaoException(HttpStatus.BAD_REQUEST)),
                userId
        );
    }

    @Override
    public void invalidatePromocode(Promocode promocode) {

        promocode.setIsActive(false);
        promocodeRepo.save(promocode);
    }

    @Override
    public Promocode save(Promocode promocode) {

        return promocodeRepo.save(promocode);
    }

    private boolean verifyPromocodeByUser(Promocode promocode, Integer userId) {

        return promocode.getUser().getUserId().equals(userId);
    }

    private boolean verifyPromocodeByItsTimeDuration(Promocode promocode) {

        if (promocode.getEndDate().isBefore(LocalDateTime.now())) {
            invalidatePromocode(promocode);
        }

        return promocode.getBeginDate().isBefore(LocalDateTime.now()) && promocode.getEndDate().isAfter(LocalDateTime.now());
    }
}
