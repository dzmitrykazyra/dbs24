/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.service.user;

import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.constant.reference.UserStatusDefine;
import org.dbs24.tik.assist.entity.domain.UserStatus;
import org.dbs24.tik.assist.service.exception.*;
import org.dbs24.tik.assist.service.email.EmailSender;
import org.dbs24.tik.assist.service.security.AuthenticationService;
import org.dbs24.tik.assist.service.user.resolver.UserActivationResolver;
import org.dbs24.tik.assist.service.user.resolver.UserPasswordResolver;
import org.dbs24.tik.assist.dao.ReferenceDao;
import org.dbs24.tik.assist.dao.UserDao;
import org.dbs24.tik.assist.entity.domain.User;
import org.dbs24.tik.assist.entity.dto.oauth2.GoogleVerificationResponse;
import org.dbs24.tik.assist.entity.dto.user.*;
import org.dbs24.tik.assist.entity.dto.user.AuthDto;
import org.dbs24.tik.assist.entity.dto.user.UserIdDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@Service
public class UserService extends AbstractApplicationService {

    final UserDao userDao;
    final ReferenceDao referenceDao;

    final EmailSender emailSender;
    final UserPasswordResolver userPasswordResolver;
    final AuthenticationService authenticationService;
    final UserActivationResolver userActivationResolver;

    public UserService(UserDao userDao,
                       EmailSender emailSender,
                       ReferenceDao referenceDao,
                       UserPasswordResolver userPasswordResolver,
                       AuthenticationService authenticationService,
                       UserActivationResolver userActivationResolver) {
        this.userDao = userDao;
        this.referenceDao = referenceDao;
        this.userActivationResolver = userActivationResolver;
        this.emailSender = emailSender;
        this.authenticationService = authenticationService;
        this.userPasswordResolver = userPasswordResolver;
    }

    @Transactional(rollbackFor = Throwable.class)
    public Mono<UserActivationDto> registerDefault(Mono<UserDto> userDtoMono) {

        return userDtoMono.map(
                userDto -> {
                    List<User> users = userDao.findAllUsersByEmail(userDto.getEmail());

                   /* if (userDao.findUserOptionalByEmail(userDto.getEmail()).isPresent()) {
                        throw new EmailIsAlreadyRegisteredException(HttpStatus.CONFLICT);
                    }

                    User userToSave = UserDto.defaultUserFromDto(userDto);
                    userToSave.setUserStatus(referenceDao.findNotActiveUserStatus());
                    userToSave.setHashPass(authenticationService.encodePassword(userDto.getPass()));

                    String activationKey = userActivationResolver.generateActivationKeyAndSendEmail(userToSave);

                    userDao.saveUser(userToSave);

                    return UserActivationDto.toDto(activationKey);*/
                    Optional<User> first = users
                            .stream()
                            .filter(user -> user.getHashPass() != null).
                            findFirst();
                    if(first.isPresent()){
                        throw new EmailIsAlreadyRegisteredException(HttpStatus.CONFLICT);
                    }else {
                        User userToSave = UserDto.defaultUserFromDto(userDto);
                        userToSave.setUserStatus(referenceDao.findNotActiveUserStatus());
                        userToSave.setHashPass(authenticationService.encodePassword(userDto.getPass()));

                        String activationKey = userActivationResolver.generateActivationKeyAndSendEmail(userToSave);

                        userDao.saveUser(userToSave);
                        return UserActivationDto.toDto(activationKey);
                    }

                }
        );
    }

    public Mono<UserActivationDto> sendActivationEmail(Mono<UserEmailDto> resendActivationEmailDtoMono) {

        return resendActivationEmailDtoMono.map(
                userEmailDto -> {

                    String emailAddress = userEmailDto.getEmailAddress();
                    Optional<User> registeredUserOptional = userDao.findUserOptionalByEmail(emailAddress);

                    if (registeredUserOptional.isEmpty()) {
                        throw new UserIsNotRegisteredException(HttpStatus.UNAUTHORIZED);
                    }

                    if (referenceDao.findActiveUserStatus().getUserStatusId()
                            .equals(registeredUserOptional.get().getUserStatus().getUserStatusId())) {
                        throw new UserIsAlreadyActivatedException(HttpStatus.BAD_REQUEST);
                    }

                    String activationKey = userActivationResolver.generateActivationKeyAndSendEmail(registeredUserOptional.get());

                    return UserActivationDto.toDto(activationKey);
                }
        );
    }

    public Mono<AuthDto> activateUserByKey(Mono<UserActivationDto> userActivationDtoMono) {

        return userActivationDtoMono.map(
                userActivationDto -> {

                    String userToActivateEmail = userActivationResolver.activateCodeAndGetUserEmail(userActivationDto.getActivationKey());
                    User userToActivate = userDao.findUserByEmail(userToActivateEmail);
                    userToActivate.setUserStatus(referenceDao.findActiveUserStatus());

                    User activatedUser = userDao.saveUser(userToActivate);

                    String jwt = authenticationService.generateJwtDefault(activatedUser);

                    return AuthDto.toDto(jwt);
                }
        );
    }

    public Mono<AuthDto> loginDefault(Mono<UserDto> loginUserDtoMono) {

        return loginUserDtoMono.map(
                loginUserDto -> {
                    List<User> usersList = userDao.findAllUsersByEmail(loginUserDto.getEmail());
                //    Optional<User> userOptional = userDao.findUserOptionalByEmail(loginUserDto.getEmail());
              /*      User userFound = userOptional.orElseThrow(() -> new EmailIsNotRegisteredException(HttpStatus.UNPROCESSABLE_ENTITY));


                    if (!authenticationService.isPasswordMatch(loginUserDto.getPass(),
                            userFound.getHashPass())) {
                        throw new IncorrectPasswordException(HttpStatus.FORBIDDEN);
                    }

                    if (!referenceDao.findActiveUserStatus().getUserStatusId()
                            .equals(userFound.getUserStatus().getUserStatusId())) {
                        throw new UserIsNotActivatedException(HttpStatus.UNAUTHORIZED);
                    }

                    return AuthDto.toDto(authenticationService.generateJwtDefault(userFound));*/
                    Optional<AuthDto> first = usersList
                            .stream()
                            .filter(user -> user.getHashPass() != null)
                            .map(user -> {
                                if (!authenticationService.isPasswordMatch(loginUserDto.getPass(),
                                        user.getHashPass())) {
                                    throw new IncorrectPasswordException(HttpStatus.FORBIDDEN);
                                }
                                if (!referenceDao.findActiveUserStatus().getUserStatusId()
                                        .equals(user.getUserStatus().getUserStatusId())) {
                                    throw new UserIsNotActivatedException(HttpStatus.UNAUTHORIZED);
                                }
                                return AuthDto.toDto(authenticationService.generateJwtDefault(user));
                            }).findFirst();
                    if(first.isPresent()){
                        return first.get();
                    }
                    throw new EmailIsNotRegisteredException(HttpStatus.UNPROCESSABLE_ENTITY);
                }
        );
    }

    public Mono<AuthDto> loginWithFacebook(Mono<FacebookLoginUserDto> facebookLoginUserDtoMono) {

        return facebookLoginUserDtoMono.map(
                facebookLoginUserDto -> AuthDto.toDto(
                        userDao.findUserOptionalByFacebookUserId(facebookLoginUserDto.getFacebookUserId())
                                .map(authenticationService::generateJwtDefault)
                                .orElseGet(
                                        () -> authenticationService.generateJwtDefault(
                                                registerFacebookUser(facebookLoginUserDto)
                                        )
                                )
                )
        );
    }

    private User registerFacebookUser(FacebookLoginUserDto facebookLoginUserDto) {

        return userDao.saveUser(
                User.builder()
                        .facebookUserId(facebookLoginUserDto.getFacebookUserId())
                        .email(facebookLoginUserDto.getEmail())
                        .userPhoneNum(facebookLoginUserDto.getPhoneNumber())
                        .actualDate(LocalDateTime.now())
                        .userStatus(referenceDao.findActiveFacebookUserStatus())
                        .build()
        );
    }

    public Mono<AuthDto> loginWithGoogle(Mono<GoogleLoginUserDto> googleLoginUserDtoMono) {

        return authenticationService
                .generateJwtViaGoogle(googleLoginUserDtoMono.toProcessor().block())
                .map(
                        googleVerificationResponse -> {
                            AuthDto authDto = new AuthDto();

                            if (googleVerificationResponse.getVerifiedEmail()) {

                                userDao
                                        .findOptionalByEmailAndStatusId(googleVerificationResponse.getEmail(), StmtProcessor.create(UserStatus.class, userStatus ->{
                                            userStatus.setUserStatusId(UserStatusDefine.US_ACTIVE_GOOGLE.getId());
                                            userStatus.setUserStatusName(UserStatusDefine.US_ACTIVE_GOOGLE.getStatusValue());
                                        })).ifPresentOrElse(
                                                user -> {
                                                    if (userDao.isDefaultUser(user)) {
                                                    throw new EmailIsAlreadyRegisteredException(HttpStatus.CONFLICT);
                                                }

                                                authDto.setToken(authenticationService.generateJwtDefault(user));
                                                },
                                                () -> authDto.setToken(authenticationService.generateJwtDefault(registerGoogleUser(googleVerificationResponse)))
                            );

                            return authDto;
                    } else {
                        throw new ForbiddenException(HttpStatus.FORBIDDEN);
                    }
                }
        );
    }

    private User registerGoogleUser(GoogleVerificationResponse googleVerificationResponse) {

        User userToSave = User.builder()
                .googleUserId(googleVerificationResponse.getGoogleUserId())
                .email(googleVerificationResponse.getEmail())
                .actualDate(LocalDateTime.now())
                .userStatus(referenceDao.findActiveGoogleUserStatus())
                .build();

        return userDao.saveUser(userToSave);
    }

    @Transactional
    public Mono<AuthDto> changePassword(Mono<ChangePasswordDto> changePasswordDtoMono, Integer userId) {

        return changePasswordDtoMono.map(
                changePasswordDto -> {

                    User userById = userDao.findUserById(userId);

                    if (!authenticationService.isPasswordMatch(changePasswordDto.getOldPassword(), userById.getHashPass())) {
                        throw new PasswordMismatchException(HttpStatus.BAD_REQUEST);
                    }

                    userDao.saveUserHistByUser(userById);
                    userById.setHashPass(authenticationService.encodePassword(changePasswordDto.getNewPassword()));

                    userDao.saveUser(userById);

                    return AuthDto.toDto(authenticationService.generateJwtDefault(userById));
                }
        );
    }

    public Mono<UserIdDto> sendEmailToChangePassword(Mono<ForgotPasswordDto> forgotPasswordDtoMono) {

        return forgotPasswordDtoMono.map(
                forgotPasswordDto -> userDao
                        .findUserOptionalByEmail(forgotPasswordDto.getEmail())
                        .filter(
                                user -> user.getUserStatus().getUserStatusId()
                                        .equals(referenceDao.findActiveUserStatus().getUserStatusId())
                        )
                        .map(
                                user -> UserIdDto.toDtoById(userPasswordResolver.sendChangePasswordEmail(user))
                        )
                        .orElseThrow(
                                () -> new UserIsNotRegisteredException(HttpStatus.UNAUTHORIZED)
                        )
        );
    }

    public Mono<UserIdDto> sendEmailToChangePasswordAuthenticated(Integer userId) {

        return Mono.just(
                UserIdDto.toDtoById(
                        userPasswordResolver.sendChangePasswordEmail(userDao.findUserById(userId))
                )
        );
    }

    public Mono<AuthDto> changeForgottenPassword(Mono<ChangeForgottenPasswordDto> changeForgottenPasswordDtoMono) {

        return changeForgottenPasswordDtoMono.map(
                changeForgottenPasswordDto -> {

                    String userEmail = userPasswordResolver.getUserEmailByForgotPasswordDto(changeForgottenPasswordDto);

                    if (!userPasswordResolver.useKeys(changeForgottenPasswordDto)) {
                        throw new KeySetIsInvalidException(HttpStatus.NOT_FOUND);
                    }

                    User userById = userDao.findUserByEmail(userEmail);

                    userDao.saveUserHistByUser(userById);
                    userById.setHashPass(authenticationService.encodePassword(changeForgottenPasswordDto.getNewPassword()));
                    User savedUser = userDao.saveUser(userById);

                    return AuthDto.toDto(authenticationService.generateJwtDefault(savedUser));
                }
        );
    }

    public Mono<AuthDto> refreshToken(String jwt) {

        if (!authenticationService.isTokenValid(jwt)) {
            throw new JwtIsExpiredException(HttpStatus.UNAUTHORIZED);
        }

        return Mono.just(AuthDto.toDto(
                authenticationService.generateJwtDefault(
                        userDao.findUserById(authenticationService.extractUserIdFromJwt(jwt))
                )
        ));
    }

    public Mono<Boolean> isKeySetValid(Mono<PasswordKeySetDto> passwordKeySetDtoMono) {

        return passwordKeySetDtoMono.map(
                passwordKeySetDto -> {
                    if (Objects.isNull(passwordKeySetDto.getUserKey())
                            || Objects.isNull(passwordKeySetDto.getExpirationKey())
                            || !userPasswordResolver.isKeySetValid(passwordKeySetDto.getUserKey(), passwordKeySetDto.getExpirationKey())) {
                        throw new KeySetIsInvalidException(HttpStatus.NOT_FOUND);
                    }

                    return Boolean.TRUE;
                }
        );
    }
}
