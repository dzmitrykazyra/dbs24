package org.dbs24.tik.mobile.entity.dto.download;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.entity.domain.VideoDownload;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class DownloadVideoDtoList {
    private List<DownloadVideoDto> downloadVideoDtoList;

    public static DownloadVideoDtoList toDto(List<VideoDownload> videoDownloads) {
        return StmtProcessor.create(DownloadVideoDtoList.class,
                downloadVideoList -> downloadVideoList.setDownloadVideoDtoList(
                        videoDownloads.stream().map(DownloadVideoDto::of).collect(Collectors.toList())
                )
        );
    }

}
