package org.dbs24.tik.mobile.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.mobile.constant.RequestQueryParam;
import org.dbs24.tik.mobile.entity.dto.tiktok.TiktokPostIdentifierListDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.TiktokUserProfileDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.SearchSinglePostDto;
import org.dbs24.tik.mobile.service.TokenHolder;
import org.dbs24.tik.mobile.service.impl.UserTiktokAccountServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class UserTiktokAccountRest {

    private final UserTiktokAccountServiceImpl userTiktokAccountService;
    private final TokenHolder tokenHolder;

    public UserTiktokAccountRest(UserTiktokAccountServiceImpl userTiktokAccountService, TokenHolder tokenHolder) {

        this.userTiktokAccountService = userTiktokAccountService;
        this.tokenHolder = tokenHolder;
    }

    @ResponseStatus
    public Mono<ServerResponse> getLatestTiktokUserVideos(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userTiktokAccountService.getLatestUserVideos(tokenHolder.extractUserIdFromServerRequest(request)),
                        TiktokPostIdentifierListDto.class
                );
    }


    @ResponseStatus
    public Mono<ServerResponse> getUserTiktokProfileInfo(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userTiktokAccountService.getUserTiktokProfileInfo(tokenHolder.extractUserIdFromServerRequest(request)),
                        TiktokUserProfileDto.class
                );
    }
}
