package org.dbs24.tik.mobile.entity.dto.download;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.entity.domain.VideoDownload;

@Data
public class DownloadVideoIdDto {
    private Integer downloadVideoId;

    public static DownloadVideoIdDto of(VideoDownload videoDownload) {
        return StmtProcessor.create(DownloadVideoIdDto.class,
                downloadVideoIdDto -> downloadVideoIdDto.setDownloadVideoId(videoDownload.getVideoDownloadId())
        );
    }
}
