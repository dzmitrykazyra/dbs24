package org.dbs24.tik.mobile.service.impl;

import org.dbs24.tik.mobile.dao.UserDao;
import org.dbs24.tik.mobile.dao.VideoDownloadDao;
import org.dbs24.tik.mobile.entity.domain.VideoDownload;
import org.dbs24.tik.mobile.entity.dto.download.DownloadVideoDtoList;
import org.dbs24.tik.mobile.entity.dto.download.DownloadVideoIdDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.SearchSinglePostDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.TiktokPostLinkDto;
import org.dbs24.tik.mobile.service.LinkExpirationService;
import org.dbs24.tik.mobile.service.TiktokAccountService;
import org.dbs24.tik.mobile.service.UserTiktokAccountService;
import org.dbs24.tik.mobile.service.VideoDownloadService;
import org.dbs24.tik.mobile.service.exception.http.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class VideoDownloadServiceImpl implements VideoDownloadService {

    private final VideoDownloadDao videoDownloadDao;
    private final TiktokAccountService tiktokAccountService;
    private final UserDao userDao;
    private final LinkExpirationService linkExpirationService;
    private final UserTiktokAccountService userTiktokAccountService;

    @Autowired
    public VideoDownloadServiceImpl(VideoDownloadDao videoDownloadDao,
                                    TiktokAccountService tiktokAccountService,
                                    UserDao userDao, LinkExpirationService linkExpirationService,
                                    UserTiktokAccountService userTiktokAccountService) {
        this.videoDownloadDao = videoDownloadDao;
        this.tiktokAccountService = tiktokAccountService;
        this.userDao = userDao;
        this.linkExpirationService = linkExpirationService;
        this.userTiktokAccountService = userTiktokAccountService;
    }

    @Override
    public Mono<SearchSinglePostDto> findVideoByLink(String link) {

        return tiktokAccountService.searchSinglePostByLink(link).map(postDto -> {
                    if (postDto.getTiktokUserPostDto() == null || postDto.getTiktokUserPostDto().getAwemeId() == null) {
                        throw new BadRequestException();
                    }
                    postDto.setAuthName(authNameFromWebLink(postDto.getTiktokUserPostDto().getWebLink()));
                    return postDto;
                }
        );
    }

    @Override
    public Mono<DownloadVideoDtoList> findDownloadsHistoryByYear(Integer userId, String year) {

        List<VideoDownload> allDownloadsByYear = videoDownloadDao.findAllDownloadsByYear(year, userDao.findById(userId));

        for (VideoDownload videoDownload : allDownloadsByYear) {
            verifyCoverLink(videoDownload);
        }
        return Mono.just(DownloadVideoDtoList.toDto(allDownloadsByYear));
    }

    @Override
    public Mono<DownloadVideoIdDto> downloadVideo(Integer userId, Mono<TiktokPostLinkDto> postLinkDtoMono) {

        String postLink = postLinkDtoMono.toProcessor().block().getPostLink();
        return tiktokAccountService.searchSinglePostByLink(postLink).map(post ->
                DownloadVideoIdDto.of(
                        videoDownloadDao.findOrSaveVideoDownload(
                                VideoDownload.builder()
                                        .withUser(userDao.findById(userId))
                                        .withVideoUrl(postLink)
                                        .withDownloadDate(LocalDateTime.now())
                                        .withCover(post.getTiktokUserPostDto().getCover())
                                        .withUsername(authNameFromWebLink(post.getTiktokUserPostDto().getWebLink()))
                                        .build()
                        )
                )
        );
    }

    private void verifyCoverLink(VideoDownload videoDownload) {
        String cover = videoDownload.getCover();
        boolean coverNeedUpdate = linkExpirationService.checkExpiration(cover);
        if (coverNeedUpdate) {
            updateCoverLink(videoDownload);
        }
    }

    private void updateCoverLink(VideoDownload videoDownload) {
        Mono<SearchSinglePostDto> videoByLink = userTiktokAccountService.findVideoByLink(videoDownload.getVideoUrl());
        SearchSinglePostDto searchSinglePostDto = videoByLink.toProcessor().block();
        if (searchSinglePostDto == null) {
            throw new BadRequestException();
        }
        String updatedCover = searchSinglePostDto.getTiktokUserPostDto().getCover();
        videoDownload.setCover(updatedCover);
        videoDownloadDao.save(videoDownload);
    }

    private String authNameFromWebLink(String webLink){
        Pattern authName = Pattern.compile("@([^/]+)");
        Matcher matcher = authName.matcher(webLink);
        if(matcher.find()){
            return matcher.group(1);
        }
        return null;
    }
}