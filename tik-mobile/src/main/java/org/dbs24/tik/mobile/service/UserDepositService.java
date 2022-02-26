package org.dbs24.tik.mobile.service;

import org.dbs24.tik.mobile.entity.dto.payment.UserDepositDto;
import org.dbs24.tik.mobile.entity.dto.payment.UserDepositIncreaseDto;
import reactor.core.publisher.Mono;

public interface UserDepositService {

    Mono<UserDepositDto> getCurrentBalance(Integer userId);

    Mono<UserDepositDto> increaseUserBalance(Integer userId, Mono<UserDepositIncreaseDto> userDepositIncreaseDtoMono);

}
