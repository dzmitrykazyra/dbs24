package org.dbs24.tik.mobile.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "tm_video_downloads")
@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
public class VideoDownload {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tm_video_downloads")
    @SequenceGenerator(name = "seq_tm_video_downloads", sequenceName = "seq_tm_video_downloads", allocationSize = 1)
    @Column(name = "video_download_id")
    private Integer videoDownloadId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "download_date")
    private LocalDateTime downloadDate;

    @Column(name = "username")
    private String username;

    @Column(name = "cover")
    private String cover;
}
