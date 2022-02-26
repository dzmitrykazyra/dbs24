package org.dbs24.tik.mobile.service.impl;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.mobile.constant.reference.UserStatusDefine;
import org.dbs24.tik.mobile.dao.UserDao;
import org.dbs24.tik.mobile.dao.UserDepositDao;
import org.dbs24.tik.mobile.dao.UserStatusDao;
import org.dbs24.tik.mobile.entity.domain.User;
import org.dbs24.tik.mobile.entity.domain.UserProfileDetail;
import org.dbs24.tik.mobile.entity.dto.user.*;
import org.dbs24.tik.mobile.service.*;
import org.dbs24.tik.mobile.service.exception.http.BadRequestException;
import org.dbs24.tik.mobile.service.exception.http.ConflictException;
import org.dbs24.tik.mobile.service.exception.http.ForbiddenException;
import org.dbs24.tik.mobile.service.exception.http.NoContentException;
import org.dbs24.tik.mobile.service.mail.template.BoundEmailAddressMail;
import org.dbs24.tik.mobile.service.mail.template.ForgotPasswordMail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Log4j2
@Service
public class UserServiceImpl implements UserService {

    private final TokenHolder tokenHolder;
    private final PasswordService passwordService;
    private final EmailSendingService emailSendingService;
    private final TiktokAccountService tiktokAccountService;
    private final UserEmailKeyService userEmailKeyService;
    private final UserPasswordKeyService userPasswordKeyService;
    private final UserTiktokAccountService userTiktokAccountService;

    private final UserDao userDao;
    private final UserStatusDao userStatusDao;
    private final UserDepositDao userDepositDao;

    public UserServiceImpl(UserDao userDao,
                           PasswordService passwordService,
                           TiktokAccountService tiktokAccountService,
                           EmailSendingService emailSendingService,
                           UserEmailKeyService userEmailKeyService,
                           UserPasswordKeyService userPasswordKeyService,
                           TokenHolder tokenHolder,
                           UserTiktokAccountService userTiktokAccountService,
                           UserStatusDao userStatusDao,
                           UserDepositDao userDepositDao) {
        this.userDao = userDao;
        this.passwordService = passwordService;
        this.tiktokAccountService = tiktokAccountService;
        this.emailSendingService = emailSendingService;
        this.userEmailKeyService = userEmailKeyService;
        this.userPasswordKeyService = userPasswordKeyService;
        this.tokenHolder = tokenHolder;
        this.userTiktokAccountService = userTiktokAccountService;
        this.userStatusDao = userStatusDao;
        this.userDepositDao = userDepositDao;
    }

    @Override
    @Transactional
    public Mono<TokenDto> register(Mono<UserDto> userDtoMono) {

        UserDto userDto = userDtoMono.toProcessor().block();

        return tiktokAccountService.searchTiktokUserByUsername(userDto.getTiktokAccountUsername())
                .map(
                        tiktokUserDto -> {
                            if (!userDto.getTiktokAccountUsername().equals(tiktokUserDto.getLoginName())) {
                                throw new BadRequestException();
                            }

                            userDao.findOptionalByUsername(tiktokUserDto.getLoginName())
                                    .ifPresent(user -> {throw new ConflictException();});

                            User userToSave = createDefaultUser(userDto);
                            userToSave.setSecUserId(tiktokUserDto.getSid());

                            User registeredUser = userDao.save(userToSave);
                            userDepositDao.saveZeroUserDepositByUser(registeredUser);

                            userTiktokAccountService.saveUserDetailsByUserAndTiktokUserDtoMono(registeredUser, tiktokUserDto);

                            return tokenHolder.generateToken(registeredUser);
                        }
                );
    }

    private User createDefaultUser(UserDto userDto) {

        return User.builder()
                .hashPass(passwordService.encodePassword(userDto.getRawPassword()))
                .username(userDto.getTiktokAccountUsername())
                .actualDate(LocalDateTime.now())
                .userStatus(userStatusDao.findByDefine(UserStatusDefine.US_ACTIVE))
                .build();
    }

    @Override
    public Mono<TokenDto> login(Mono<UserDto> userDtoMono) {

        return userDtoMono.map(
                userDto -> tokenHolder.generateToken(
                        userDao.findOptionalByUsername(userDto.getTiktokAccountUsername())
                                .filter(user -> passwordService.isMatch(userDto.getRawPassword(), user.getHashPass()))
                                .orElseThrow(ForbiddenException::new)
                )
        );
    }

    @Override
    public Mono<UserVerificationDto> logout(String token) {

        return Mono.just(
                UserVerificationDto.of(
                        tokenHolder.removeToken(
                                token
                        )
                )
        );
    }

    @Override
    public Mono<UserEmailDto> checkUserEmailExistence(String token) {

        User user = userDao.findById(tokenHolder.getUserIdByToken(token));
        if (user.getEmail() == null) {
            throw new NoContentException();
        }

        return Mono.just(UserEmailDto.of(user.getEmail()));
    }

    @Override
    public Mono<UserEmailBoundingKeysetDto> boundEmailRequest(Mono<UserEmailBoundingDto> userEmailBoundDtoMono) {

        return userEmailBoundDtoMono.map(
                userEmailBoundingDto -> {
                    String requiredEmail = userEmailBoundingDto.getEmail();

                    userDao.findOptionalByEmail(requiredEmail).ifPresent(user -> {
                        throw new ConflictException();
                    });
                    User user = userDao.findById(tokenHolder.getUserIdByToken(userEmailBoundingDto.getJwt()));
                    UserEmailBoundingKeysetDto keyset = userEmailKeyService.generateEmailBoundingKeyset(requiredEmail, user);

                    emailSendingService.send(
                            new BoundEmailAddressMail(
                                    userEmailBoundingDto.getEmail(),
                                    keyset
                            )
                    );

                    return keyset;
                }
        );
    }

    @Override
    public Mono<TokenDto> activateBoundedEmail(Mono<UserEmailBoundingKeysetDto> emailActivationDtoMono) {

        return emailActivationDtoMono.map(
                emailActivationDto -> {
                    String emailToBound = userEmailKeyService.getEmailByKeyBoundingSet(emailActivationDto);

                    User user = userDao.findById(userEmailKeyService.getUserIdByKeyBoundingSet(emailActivationDto));
                    user.setEmail(emailToBound);
                    User updatedUser = userDao.update(user);

                    return tokenHolder.generateToken(updatedUser);
                }
        );
    }

    @Override
    public Mono<UserForgottenPasswordKeysetDto> forgotPassword(Mono<UserEmailDto> userEmailDtoMono) {

        return userEmailDtoMono.map(
                userEmailDto -> {
                    UserForgottenPasswordKeysetDto keyset = userPasswordKeyService
                            .generateChangePasswordKeyset(
                                    userDao.findOptionalByEmail(userEmailDto.getUserEmail())
                                            .orElseThrow(NoContentException::new)
                            );

                    emailSendingService.send(
                            new ForgotPasswordMail(
                                    userEmailDto.getUserEmail(),
                                    keyset)
                    );

                    return keyset;
                }
        );
    }

    @Override
    public Mono<TokenDto> changeForgottenPassword(Mono<UserForgottenPasswordKeysetDto> userForgottenPasswordKeysetDtoMono) {

        return userForgottenPasswordKeysetDtoMono.map(
                userForgottenPasswordKeysetDto -> {
                    User user = userDao.findById(userPasswordKeyService.getUserIdByChangePasswordKeyset(userForgottenPasswordKeysetDto));
                    user.setHashPass(passwordService.encodePassword(userForgottenPasswordKeysetDto.getRawNewPassword()));

                    return tokenHolder.generateToken(userDao.update(user));
                }
        );
    }

    @Override
    public Mono<TokenDto> refreshToken(Mono<TokenDto> tokenToRefreshMono) {

        return tokenToRefreshMono.map(
                tokenDto -> tokenHolder.generateToken(
                        userDao.findById(
                                tokenHolder.getUserIdByToken(tokenDto.getJwt())
                        )
                )
        );
    }
}
