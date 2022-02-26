package org.dbs24.tik.mobile.repo;

import org.dbs24.tik.mobile.entity.domain.User;
import org.dbs24.tik.mobile.entity.domain.VideoDownload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VideoDownloadRepo extends JpaRepository<VideoDownload, Integer> {

    List<VideoDownload> findAllByDownloadDateBetweenAndUser(LocalDateTime firstDay, LocalDateTime lastDay, User user);

    Optional<VideoDownload> findByUserAndVideoUrl(User user, String videoUrl);
}
