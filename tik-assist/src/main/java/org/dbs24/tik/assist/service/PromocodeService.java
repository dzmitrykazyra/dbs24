package org.dbs24.tik.assist.service;

import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.StringFuncs;
import org.dbs24.tik.assist.dao.PromocodeDao;
import org.dbs24.tik.assist.dao.UserDao;
import org.dbs24.tik.assist.entity.domain.Promocode;
import org.dbs24.tik.assist.entity.dto.user.UserEmailDto;
import org.dbs24.tik.assist.entity.dto.user.UserMailingDto;
import org.dbs24.tik.assist.service.email.EmailSender;
import org.dbs24.tik.assist.service.email.template.UserPromocodeEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Log4j2
@Component
public class PromocodeService {

    @Value("${constraint.user.promocode.length}")
    private int promocodeValueLength;

    @Value("${constraint.user.promocode.discount}")
    private int promocodeDiscount;

    @Value("${constraint.user.promocode.validity-period-days}")
    private int promocodeValidityDays;

    private final EmailSender emailSender;

    private final UserDao userDao;
    private final PromocodeDao promocodeDao;

    public PromocodeService(PromocodeDao promocodeDao, EmailSender emailSender, UserDao userDao) {

        this.promocodeDao = promocodeDao;
        this.emailSender = emailSender;
        this.userDao = userDao;
    }

    /**
     * Method allows creating single promocode for user mailing accept.
     * Method steps to verify user email:
     *      - check email existence in mailing service (must not be existing cause of promocode uniqueness)
     *      - if email was not used for sending before, generate promocode value
     *      - store promocode unique for user
     *      - send email with promocode to user
     */
    public Mono<UserMailingDto> sendMailingPromocode(Integer userId, Mono<UserEmailDto> userEmailDtoMono) {

        UserEmailDto userEmailDto = userEmailDtoMono.toProcessor().block();

        Promocode savedPromocode = promocodeDao.save(
                Promocode.builder()
                        .promocodeValue(generatePromocodeValue())
                        .promocodeDiscount(promocodeDiscount)
                        .isActive(Boolean.TRUE)
                        .user(userDao.findUserById(userId))
                        .beginDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now().plusDays(promocodeValidityDays))
                        .actualDate(LocalDateTime.now())
                        .build()
        );

        emailSender.sendEmail(new UserPromocodeEmail(userEmailDto.getEmailAddress(), savedPromocode.getPromocodeValue()));

        return Mono.just(UserMailingDto.success());
    }

    private String generatePromocodeValue() {

        return StringFuncs.createRandomString(promocodeValueLength);
    }
}
