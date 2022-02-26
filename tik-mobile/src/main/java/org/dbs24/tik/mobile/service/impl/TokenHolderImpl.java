package org.dbs24.tik.mobile.service.impl;

import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.log4j.Log4j2;
import org.dbs24.component.JwtSecurityService;
import org.dbs24.tik.mobile.config.TikMobileRouter;
import org.dbs24.tik.mobile.entity.domain.User;
import org.dbs24.tik.mobile.entity.dto.user.TokenDto;
import org.dbs24.tik.mobile.service.TokenHolder;
import org.dbs24.tik.mobile.service.exception.http.ForbiddenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Component
public class TokenHolderImpl implements TokenHolder {

    private final Map<Integer, String> userIdToJwt;

    private final JwtSecurityService jwtUtils;

    @Value("${config.security.jwt.lifetime}")
    private long jwtLifetime;

    public TokenHolderImpl(JwtSecurityService jwtUtils) {

        this.jwtUtils = jwtUtils;
        this.userIdToJwt = new ConcurrentHashMap<>();
    }

    @Override
    public TokenDto generateToken(User user) {

        String token = jwtUtils.generateToken(
                String.valueOf(user.getId()),
                Map.of(
                        /*"email", user.getEmail(),*/
                        "username", user.getUsername()
                ),
                jwtLifetime
        );

        userIdToJwt.put(user.getId(), token);

        return TokenDto.of(token);
    }

    @Override
    public Boolean removeToken(String token) {

        removeExpiredTokens();

        return userIdToJwt.remove(getUserIdByToken(token)) != null;
    }

    @Override
    public Integer getUserIdByToken(String token) {
        try {
            return Integer.valueOf(jwtUtils.getUsernameFromToken(token));
        } catch (MalformedJwtException e) {
            throw new ForbiddenException();
        }
    }

    @Override
    public Boolean isTokenValid(String token) {

        return userIdToJwt.containsValue(token) && jwtUtils.validateToken(token);
    }

    @Override
    public String extractJwtFromServerRequest(ServerRequest request) {

        return request.headers().firstHeader(TikMobileRouter.AUTHORIZATION_HEADER_NAME);
    }

    @Override
    public Integer extractUserIdFromServerRequest(ServerRequest request) {

        return getUserIdByToken(extractJwtFromServerRequest(request));
    }

    @Override
    public void removeExpiredTokens() {

        userIdToJwt.values().removeIf(jwt -> !jwtUtils.validateToken(jwt));
    }
}
