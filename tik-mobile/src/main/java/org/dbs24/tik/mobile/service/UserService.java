package org.dbs24.tik.mobile.service;

import org.dbs24.tik.mobile.entity.dto.user.*;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<TokenDto> register(Mono<UserDto> userDto);

    Mono<TokenDto> login(Mono<UserDto> userDto);

    Mono<UserVerificationDto> logout(String token);

    Mono<UserEmailDto> checkUserEmailExistence(String token);

    Mono<UserEmailBoundingKeysetDto> boundEmailRequest(Mono<UserEmailBoundingDto> userEmailDto);

    Mono<TokenDto> activateBoundedEmail(Mono<UserEmailBoundingKeysetDto> emailActivationDto);

    Mono<UserForgottenPasswordKeysetDto> forgotPassword(Mono<UserEmailDto> userEmailDto);

    Mono<TokenDto> changeForgottenPassword(Mono<UserForgottenPasswordKeysetDto> changePasswordDto);

    Mono<TokenDto> refreshToken(Mono<TokenDto> tokenToRefresh);
}
