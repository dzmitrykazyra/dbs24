package org.dbs24.tik.assist.service.payment;

import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.tik.assist.dao.DepositPaymentDao;
import org.dbs24.tik.assist.dao.UserDao;
import org.dbs24.tik.assist.dao.UserDepositDao;
import org.dbs24.tik.assist.entity.dto.payment.IncreaseUserDepositDto;
import org.dbs24.tik.assist.entity.dto.payment.UserDepositBalanceDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class PaymentsService extends AbstractApplicationService {

    private final UserDao userDao;
    private final UserDepositDao userDepositDao;
    private final DepositPaymentDao depositPaymentDao;

    public PaymentsService(DepositPaymentDao depositPaymentDao, UserDao userDao, UserDepositDao userDepositDao) {

        this.userDao = userDao;
        this.depositPaymentDao = depositPaymentDao;
        this.userDepositDao = userDepositDao;
    }

    @Transactional
    public Mono<UserDepositBalanceDto> increaseUserDeposit(Mono<IncreaseUserDepositDto> increaseDepositDtoMono, Integer userId) {

        return increaseDepositDtoMono.map(
                increaseUserDepositDto -> UserDepositBalanceDto.of(
                        userDepositDao.increaseUserDeposit(
                                userDao.findUserById(userId),
                                increaseUserDepositDto.getIncreaseAmount()
                        )
                )
        );
    }

    public Mono<UserDepositBalanceDto> getUserDepositBalance(Integer userId) {

        return Mono.just(
                UserDepositBalanceDto.of(
                        userDepositDao.findOrCreateUserDepositByUser(userDao.findUserById(userId))
                )
        );
    }
}
