package org.dbs24.tik.mobile.test.service;

import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.test.core.AbstractWebTest;
import org.dbs24.tik.mobile.TikMobile;
import org.dbs24.tik.mobile.config.TikMobileConfig;
import org.dbs24.tik.mobile.config.TikMobileRouter;
import org.dbs24.tik.mobile.constant.ApiPath;
import org.dbs24.tik.mobile.dao.UserDepositDao;
import org.dbs24.tik.mobile.entity.domain.User;
import org.dbs24.tik.mobile.entity.dto.payment.UserDepositDto;
import org.dbs24.tik.mobile.entity.dto.payment.UserDepositIncreaseDto;
import org.dbs24.tik.mobile.repo.UserRepo;
import org.dbs24.tik.mobile.service.TokenHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import static org.dbs24.consts.SysConst.ALL_PACKAGES;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TikMobile.class})
@Import({TikMobileConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
@TestPropertySource(properties = "config.security.profile.webfilter.chain=development")
public class UserDepositTest extends AbstractWebTest {

    private final UserDepositDao userDepositDao;
    private final UserRepo userRepo;
    private final TokenHolder tokenHolder;

    @Autowired
    public UserDepositTest(UserDepositDao userDepositDao, UserRepo userRepo, TokenHolder tokenHolder) {
        this.userDepositDao = userDepositDao;
        this.userRepo = userRepo;
        this.tokenHolder = tokenHolder;
    }


    @RepeatedTest(1)
    @Order(100)
    public void getActualUserBalance() {
        runTest(() -> {

            log.info("TEST {}", ApiPath.DEPOSITS_GET_CURRENT_BALANCE);

            UserDepositDto userBalance = webTestClient
                    .get()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(ApiPath.DEPOSITS_GET_CURRENT_BALANCE)
                            .build())
                    .header(TikMobileRouter.AUTHORIZATION_HEADER_NAME, tokenHolder.generateToken(findRandomUser()).getJwt())
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(UserDepositDto.class)
                    .returnResult()
                    .getResponseBody();

            log.info("TEST {} result is: {}", ApiPath.DEPOSITS_GET_CURRENT_BALANCE, userBalance);
        });
    }

    @RepeatedTest(1)
    @Order(200)
    public void increaseUserBalance() {
        runTest(() -> {
            log.info("TEST {}", ApiPath.DEPOSITS_INCREASE);

            Integer oldUserBalance = userDepositDao.findUserDepositByUserId(findRandomUser().getId()).getRestSum();

            Mono<UserDepositIncreaseDto> userDepositIncreaseDtoMono = Mono.just(
                    StmtProcessor.create(UserDepositIncreaseDto.class,
                            userDepositIncrease -> {
                                userDepositIncrease.setAmountDepositsToIncrease(450);
                            }
                    )
            );

            UserDepositDto responseUserBalanceDto = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(ApiPath.DEPOSITS_INCREASE)
                            .build())
                    .header(TikMobileRouter.AUTHORIZATION_HEADER_NAME, tokenHolder.generateToken(findRandomUser()).getJwt())
                    .body(userDepositIncreaseDtoMono, UserDepositIncreaseDto.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(UserDepositDto.class)
                    .returnResult()
                    .getResponseBody();

            log.info("TEST {}. OLD USER BALANCE = {}. NEW ORDER BALANCE = {}",
                    ApiPath.DEPOSITS_INCREASE,
                    oldUserBalance,
                    responseUserBalanceDto.getAmountUserDeposit()
            );

            Assertions.assertEquals(oldUserBalance + 450, responseUserBalanceDto.getAmountUserDeposit());
        });
    }

    private User findRandomUser() {
        return userRepo.findAll().stream().findFirst().get();
    }
}
