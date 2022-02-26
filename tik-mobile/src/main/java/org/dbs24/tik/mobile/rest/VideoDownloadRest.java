package org.dbs24.tik.mobile.rest;

import org.dbs24.tik.mobile.constant.RequestQueryParam;
import org.dbs24.tik.mobile.entity.dto.download.DownloadVideoDtoList;
import org.dbs24.tik.mobile.entity.dto.download.DownloadVideoIdDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.SearchSinglePostDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.TiktokPostLinkDto;
import org.dbs24.tik.mobile.service.TokenHolder;
import org.dbs24.tik.mobile.service.VideoDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class VideoDownloadRest {

    private final VideoDownloadService videoDownloadService;
    private final TokenHolder tokenHolder;

    @Autowired
    public VideoDownloadRest(VideoDownloadService videoDownloadService,
                             TokenHolder tokenHolder) {
        this.videoDownloadService = videoDownloadService;
        this.tokenHolder = tokenHolder;
    }

    @ResponseStatus
    public Mono<ServerResponse> findVideoForDownload(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        videoDownloadService.findVideoByLink(
                                request.queryParam(RequestQueryParam.QP_TIKTOK_POST_LINK).get()
                        ),
                        SearchSinglePostDto.class
                );
    }

    public Mono<ServerResponse> findAllDownloadsVideoByYear(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        videoDownloadService.findDownloadsHistoryByYear(
                                tokenHolder.extractUserIdFromServerRequest(request),
                                request.queryParam(RequestQueryParam.QP_YEAR).get()
                        ),
                        DownloadVideoDtoList.class
                );
    }

    public Mono<ServerResponse> downloadVideo(ServerRequest request) {

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        videoDownloadService.downloadVideo(
                                tokenHolder.extractUserIdFromServerRequest(request),
                                request.bodyToMono(TiktokPostLinkDto.class)
                        ),
                        DownloadVideoIdDto.class
                );

    }
}
