package org.dbs24.tik.mobile.test.service;

import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.test.core.AbstractWebTest;
import org.dbs24.tik.mobile.TikMobile;
import org.dbs24.tik.mobile.config.TikMobileConfig;
import org.dbs24.tik.mobile.config.TikMobileRouter;
import org.dbs24.tik.mobile.constant.ApiPath;
import org.dbs24.tik.mobile.constant.RequestQueryParam;
import org.dbs24.tik.mobile.dao.VideoDownloadDao;
import org.dbs24.tik.mobile.entity.domain.User;
import org.dbs24.tik.mobile.entity.dto.download.DownloadVideoDtoList;
import org.dbs24.tik.mobile.entity.dto.download.DownloadVideoIdDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.SearchSinglePostDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.TiktokPostLinkDto;
import org.dbs24.tik.mobile.repo.UserRepo;
import org.dbs24.tik.mobile.service.TokenHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.dbs24.consts.SysConst.ALL_PACKAGES;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TikMobile.class})
@Import({TikMobileConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EntityScan(basePackages = {ALL_PACKAGES})
public class DownloadVideoTest extends AbstractWebTest {

    private final String TIKTOK_POST_LINK = "";
    private final String FILTER_VIDEO_YEAR = "2021";

    private final UserRepo userRepo;
    private final TokenHolder tokenHolder;
    private final VideoDownloadDao videoDownloadDao;

    @Autowired
    public DownloadVideoTest(UserRepo userRepo, TokenHolder tokenHolder, VideoDownloadDao videoDownloadDao) {
        this.userRepo = userRepo;
        this.tokenHolder = tokenHolder;
        this.videoDownloadDao = videoDownloadDao;
    }


    @RepeatedTest(1)
    @Order(100)
    public void findVideoForDownloadWithErrorLink_expected400Error() {
        runTest(() -> {

            log.info("TEST {} WITH ERROR LINK", ApiPath.DOWNLOADS_FIND_VIDEO_BY_LINK);

            webTestClient
                    .get()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(ApiPath.DOWNLOADS_FIND_VIDEO_BY_LINK)
                            .queryParam(RequestQueryParam.QP_TIKTOK_POST_LINK, "ERROR LINK")
                            .build())
                    .exchange()
                    .expectStatus()
                    .isBadRequest();
        });
    }

    @RepeatedTest(1)
    @Order(200)
    public void findVideoForDownloadsWithCorrectLink_expectedInfoAboutPost() {
        runTest(() -> {

            log.info("TEST {} WITH CORRECT LINK = {}", ApiPath.DOWNLOADS_FIND_VIDEO_BY_LINK, TIKTOK_POST_LINK);

            final SearchSinglePostDto responseBody = webTestClient
                    .get()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(ApiPath.DOWNLOADS_FIND_VIDEO_BY_LINK)
                            .queryParam(RequestQueryParam.QP_TIKTOK_POST_LINK, TIKTOK_POST_LINK)
                            .build())
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(SearchSinglePostDto.class)
                    .returnResult()
                    .getResponseBody();

            log.info("RESULT {}", responseBody);
        });
    }

    @RepeatedTest(1)
    @Order(300)
    public void findDownloadsVideoByYear() {
        runTest(() -> {

            log.info("TEST {} WITH YEAR = {}", ApiPath.DOWNLOADS_GET_ALL_VIDEO_HISTORY, FILTER_VIDEO_YEAR);

            User user = findRandomUser();

            final DownloadVideoDtoList responseBody = webTestClient
                    .get()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(ApiPath.DOWNLOADS_GET_ALL_VIDEO_HISTORY)
                            .queryParam(RequestQueryParam.QP_YEAR, FILTER_VIDEO_YEAR)
                            .build())
                    .header(TikMobileRouter.AUTHORIZATION_HEADER_NAME, tokenHolder.generateToken(user).getJwt())
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(DownloadVideoDtoList.class)
                    .returnResult()
                    .getResponseBody();

            log.info("RESULT {} TEST. USER ID = {}. ALL DOWNLOADED VIDEOS = {}",
                    ApiPath.DOWNLOADS_GET_ALL_VIDEO_HISTORY,
                    user.getId(),
                    responseBody
            );

        });
    }

    @RepeatedTest(1)
    @Order(300)
    public void downloadVideo() {
        runTest(() -> {

            log.info("TEST {} WITH VIDEO LINK = {}", ApiPath.DOWNLOADS_VIDEO_DOWNLOAD, TIKTOK_POST_LINK);

            Mono<TiktokPostLinkDto> requestBody = Mono.just(
                    StmtProcessor.create(TiktokPostLinkDto.class,
                            tiktokPostLink -> tiktokPostLink.setPostLink(TIKTOK_POST_LINK)
                    )
            );

            User user = findRandomUser();


            final DownloadVideoIdDto responseBody = webTestClient
                    .post()
                    .uri(uriBuilder
                            -> uriBuilder
                            .path(ApiPath.DOWNLOADS_VIDEO_DOWNLOAD)
                            .build())
                    .header(TikMobileRouter.AUTHORIZATION_HEADER_NAME, tokenHolder.generateToken(user).getJwt())
                    .body(requestBody, TiktokPostLinkDto.class)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(DownloadVideoIdDto.class)
                    .returnResult()
                    .getResponseBody();

            log.info("RESULT {} TEST IS : {}", ApiPath.DOWNLOADS_VIDEO_DOWNLOAD, responseBody);
        });
    }

    private User findRandomUser() {
        return userRepo.findAll().stream().findFirst().get();
    }
}
