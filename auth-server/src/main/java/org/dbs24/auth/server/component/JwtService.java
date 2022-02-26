/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.auth.server.component;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.auth.server.api.TokenBuilder;
import org.dbs24.auth.server.entity.Token;
import org.dbs24.auth.server.repo.TokenCardRepo;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.Map;

@Log4j2
@Component
@Getter
@EqualsAndHashCode(callSuper = true)
//@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-reg")
public class JwtService extends AbstractApplicationService {

    final TokenCardRepo tokenCardRepo;
    final RefsService refsService;
    final Map<String, Token> issuedTokens = ServiceFuncs.createConcurencyMap();

    public JwtService(TokenCardRepo tokenCardRepo, RefsService refsService) {
        this.tokenCardRepo = tokenCardRepo;
        this.refsService = refsService;
    }

    public Token findToken(String tokenKey, LocalDateTime validUntil, Integer applicationId, String requestId, TokenBuilder tokenBuilder) {

        final LocalDateTime current = LocalDateTime.now().plusHours(1);

        return issuedTokens
                .entrySet()
                .stream()
                .filter(es -> es.getKey().equals(tokenKey))
                .filter(es -> current.compareTo(es.getValue().getValidUntil()) < 0)
                .findFirst()
                .orElseGet(() -> {

                    final String jwt = tokenBuilder.build();
                    final Token newToken = createJwt(validUntil, applicationId, jwt, requestId, tokenKey);

                    issuedTokens.put(tokenKey, newToken);

                    log.info("Issue new token for '{}': {}", tokenKey, newToken);

                    return new AbstractMap.SimpleEntry<String, Token>(jwt, newToken);

                }).getValue();
    }

    @Transactional
    public Token createJwt(LocalDateTime untilDate, Integer applicationId, String tokenCard, String requestId, String tag) {

        final Token token = StmtProcessor.create(Token.class, tk -> {
            tk.setApplication(refsService.findApplication(applicationId));
            tk.setIssueDate(LocalDateTime.now());
            tk.setValidUntil(untilDate);
            tk.setTokenCard(tokenCard);
            tk.setRequestId(requestId);
            tk.setTag(tag);
        });

        tokenCardRepo.save(token);

        return token;

    }
}
