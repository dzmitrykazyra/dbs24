package org.dbs24.tik.mobile.dao.impl;

import org.dbs24.tik.mobile.dao.VideoDownloadDao;
import org.dbs24.tik.mobile.entity.domain.User;
import org.dbs24.tik.mobile.entity.domain.VideoDownload;
import org.dbs24.tik.mobile.repo.VideoDownloadRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

@Component
public class VideoDownloadDaoImpl implements VideoDownloadDao {

    private final VideoDownloadRepo videoDownloadRepo;

    @Autowired
    public VideoDownloadDaoImpl(VideoDownloadRepo videoDownloadRepo) {
        this.videoDownloadRepo = videoDownloadRepo;
    }

    @Override
    public VideoDownload save(VideoDownload videoDownload) {
        return videoDownloadRepo.save(videoDownload);
    }

    @Override
    public VideoDownload findOrSaveVideoDownload(VideoDownload videoDownload) {
        return videoDownloadRepo.findByUserAndVideoUrl(videoDownload.getUser(), videoDownload.getVideoUrl()).orElseGet(() -> save(videoDownload));
    }

    @Override
    public List<VideoDownload> findAllDownloadsByYear(String year, User user) {
        LocalDateTime firstDayOfYear = LocalDateTime.now()
                .withYear(Integer.parseInt(year))
                .with(TemporalAdjusters.firstDayOfYear());

        LocalDateTime lastDayOfYear = LocalDateTime.now()
                .withYear(Integer.parseInt(year))
                .with(TemporalAdjusters.lastDayOfYear());

        return videoDownloadRepo.findAllByDownloadDateBetweenAndUser(firstDayOfYear, lastDayOfYear, user);
    }
}