package org.dbs24.tik.mobile.dao;

import org.dbs24.tik.mobile.entity.domain.User;
import org.dbs24.tik.mobile.entity.domain.VideoDownload;

import java.util.List;

public interface VideoDownloadDao {

    List<VideoDownload> findAllDownloadsByYear(String year, User user);

    VideoDownload save(VideoDownload videoDownload);

    VideoDownload findOrSaveVideoDownload(VideoDownload videoDownload);

}
