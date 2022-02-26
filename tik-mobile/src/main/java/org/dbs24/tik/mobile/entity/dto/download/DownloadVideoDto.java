package org.dbs24.tik.mobile.entity.dto.download;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dbs24.tik.mobile.entity.domain.VideoDownload;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class DownloadVideoDto {
    private String username;
    private String cover;
    private String videoUrl;
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate downloadDate;


    public static DownloadVideoDto of(VideoDownload videoDownload) {
        return DownloadVideoDto.builder()
                .withDownloadDate(videoDownload.getDownloadDate().toLocalDate())
                .withVideoUrl(videoDownload.getVideoUrl())
                .withCover(videoDownload.getCover())
                .withUsername(videoDownload.getUsername())
                .build();
    }
}
