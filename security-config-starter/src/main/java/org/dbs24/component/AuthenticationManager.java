/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Log4j2
@Component
public class AuthenticationManager extends AbstractApplicationService implements ReactiveAuthenticationManager {

    final Map<String, LocalDateTime> actualTokens = ServiceFuncs.createConcurencyMap();
    final JwtSecurityService jwtUtils;

    public AuthenticationManager(JwtSecurityService jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    //@SuppressWarnings("unchecked")
    public Mono<Authentication> authenticate(Authentication authentication) {

        final String authToken = authentication.getCredentials().toString();

        log.debug("authenticate {}, authToken {}", authentication, authToken);

        return jwtUtils.validateToken(authToken) ? getAuthentication(authToken) : Mono.empty();

    }

    //==========================================================================
    private Mono<Authentication> getAuthentication(String authToken) {

        final String userName = jwtUtils.getUsernameFromToken(authToken);

        final Claims claims = jwtUtils.getAllClaimsFromToken(authToken);
        final Collection<String> rolesMap = claims.get("role", List.class);
        final Collection<GrantedAuthority> authorities = ServiceFuncs.createCollection();

        Optional.ofNullable(rolesMap)
                .ifPresent(roles -> roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role))));

        return Mono.just(new UsernamePasswordAuthenticationToken(userName, null, authorities));
    }

    //==========================================================================
    public Mono<SecurityContext> createSecurityContext(String authToken) {

        return authenticate(new UsernamePasswordAuthenticationToken(authToken, authToken))
                .map(authentication -> new SecurityContextImpl(authentication));
    }

    //==========================================================================
    public void addToken(String token, LocalDateTime validUntil) {

        actualTokens.put(token, validUntil);

        log.info("register new token {} ({})", token, actualTokens.size());

    }
}
