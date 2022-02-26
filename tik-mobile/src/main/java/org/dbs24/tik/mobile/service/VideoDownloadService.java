package org.dbs24.tik.mobile.service;

import org.dbs24.tik.mobile.entity.dto.download.DownloadVideoDtoList;
import org.dbs24.tik.mobile.entity.dto.download.DownloadVideoIdDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.SearchSinglePostDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.TiktokPostLinkDto;
import reactor.core.publisher.Mono;

public interface VideoDownloadService {

    Mono<SearchSinglePostDto> findVideoByLink(String link);

    Mono<DownloadVideoDtoList> findDownloadsHistoryByYear(Integer userId, String year);

    Mono<DownloadVideoIdDto> downloadVideo(Integer userId, Mono<TiktokPostLinkDto> postLinkDtoMono);
}
