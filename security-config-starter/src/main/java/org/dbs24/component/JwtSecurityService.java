package org.dbs24.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.consts.SysConst;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Log4j2
@Component
@EqualsAndHashCode(callSuper = true)
public class JwtSecurityService extends AbstractApplicationService {

    @Value("${config.security.jwt.secret:ThisIsSecretForJWTHS512SignatureAlgorithmThatMUSTHave64ByteLength")
    private String secretKey;

    private Key key;

    @Override
    public void initialize() {

        super.initialize();
        key = Keys.hmacShaKeyFor(secretKey.getBytes());

    }

    public String generateToken(String subject, Map<String, String> claims, Long expirationTime) {
        return doGenerateToken(subject, claims, expirationTime);
    }

    private String doGenerateToken(String subject, Map<String, String> claims, Long expirationTime) {

        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expirationTime * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Boolean validateToken(String token) {
        return (Boolean) StmtProcessor.createSilent(() -> !isTokenExpired(token))
                .whenIsNull(() -> SysConst.BOOLEAN_FALSE)
                .get();
    }

    public Mono<String> checkToken(String token) {

        try {
            isTokenExpired(token);
        } catch (Throwable t) {
            log.warn("checkToken: {}", t.getMessage());
        }

        return Mono.just(token);
    }
}
