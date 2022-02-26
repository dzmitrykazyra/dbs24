package org.dbs24.tik.mobile.service.impl;

import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.Stmt;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.dao.UserDao;
import org.dbs24.tik.mobile.dao.UserDepositDao;
import org.dbs24.tik.mobile.entity.domain.UserDeposit;
import org.dbs24.tik.mobile.entity.dto.payment.UserDepositDto;
import org.dbs24.tik.mobile.entity.dto.payment.UserDepositIncreaseDto;
import org.dbs24.tik.mobile.service.UserDepositService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Log4j2
public class UserDepositServiceImpl implements UserDepositService {

    private final UserDepositDao userDepositDao;
    private final UserDao userDao;

    public UserDepositServiceImpl(UserDepositDao userDepositDao, UserDao userDao) {
        this.userDepositDao = userDepositDao;
        this.userDao = userDao;
    }

    @Override
    public Mono<UserDepositDto> getCurrentBalance(Integer userId) {
        return Mono.just(
                StmtProcessor.create(UserDepositDto.class,
                        userDepositDto -> userDepositDto.setAmountUserDeposit(
                                userDepositDao.findUserDepositByUserId(userId).getRestSum()
                        )
                )
        );
    }

    @Override
    public Mono<UserDepositDto> increaseUserBalance(Integer userId, Mono<UserDepositIncreaseDto> userDepositIncreaseDtoMono) {

        return userDepositIncreaseDtoMono.map(userDepositIncreaseDto -> StmtProcessor.create(UserDepositDto.class,
                        userDepositDto -> {
                            UserDeposit updatedUserDeposit = userDepositDao.increaseUserDeposit(
                                    userDao.findById(userId),
                                    userDepositIncreaseDto.getAmountDepositsToIncrease()
                            );
                            userDepositDto.setAmountUserDeposit(updatedUserDeposit.getRestSum());
                        }
                )
        );

    }
}
