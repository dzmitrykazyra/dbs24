package org.dbs24.tik.mobile.service;

import org.dbs24.tik.mobile.entity.domain.User;
import org.dbs24.tik.mobile.entity.dto.user.TokenDto;
import org.springframework.web.reactive.function.server.ServerRequest;

public interface TokenHolder {

    TokenDto generateToken(User user);

    Boolean removeToken(String token);

    Integer getUserIdByToken(String token);

    Boolean isTokenValid(String token);

    String extractJwtFromServerRequest(ServerRequest request);

    Integer extractUserIdFromServerRequest(ServerRequest request);

    void removeExpiredTokens();
}
