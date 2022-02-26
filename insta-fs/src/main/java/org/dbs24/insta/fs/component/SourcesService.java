/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.fs.component;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.insta.fs.entity.Source;
import org.dbs24.insta.fs.repo.SourceRepo;
import org.dbs24.insta.fs.rest.api.CreatedSource;
import org.dbs24.insta.fs.rest.api.SourceInfo;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Data
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-fs")
public class SourcesService extends AbstractApplicationService {

    final SourceRepo sourceRepo;
    //final SourceHistRepo sourceHistRepo;
    final RefsService refsService;
    final AccountsService accountsService;
    final PostsService postsService;

    public SourcesService(SourceRepo sourceRepo, RefsService refsService, AccountsService accountsService, PostsService postsService) {
        this.sourceRepo = sourceRepo;
        this.refsService = refsService;
        this.accountsService = accountsService;
        this.postsService = postsService;
    }
    //==========================================================================

    @Transactional
    public CreatedSource createOrUpdateSource(SourceInfo sourceInfo) {

        final Source source = findOrCreateSource(sourceInfo.getSourceId());

        // copy 2 history
        source.setPost(postsService.findPost(sourceInfo.getPostId()));
        source.setActualDate((LocalDateTime) StmtProcessor.nvl(NLS.long2LocalDateTime(sourceInfo.getActualDate()), LocalDateTime.now()));
        source.setSourceStatus(refsService.findSourceStatus(sourceInfo.getSourceStatusId()));
        source.setMainFaceBox(sourceInfo.getMainFaceBox());
        source.setSourceHash(sourceInfo.getSourceHash());
        source.setSourceUrl(sourceInfo.getSourceUrl());

        saveSource(source);

        return StmtProcessor.create(CreatedSource.class, ca -> {

            ca.setSourceId(source.getSourceId());

            log.debug("try 2 create/update source: {}", source);

        });
    }

    public Source createSource() {
        return StmtProcessor.create(Source.class, a -> {
            a.setActualDate(LocalDateTime.now());
        });
    }

    public Source findSource(Long sourceId) {

        return sourceRepo
                .findById(sourceId)
                .orElseThrow(() -> new RuntimeException(String.format("sourceId not found (%d)", sourceId)));
    }

    public Source findOrCreateSource(Long sourceId) {
        return (Optional.ofNullable(sourceId)
                .orElseGet(() -> Long.valueOf("0")) > 0)
                ? findSource(sourceId)
                : createSource();
    }

//    public void saveSourceHistory(SourceHist sourceHist) {
//        sourceHistRepo.save(sourceHist);
//    }
//    public void saveSourceHistory(Source source) {
//        Optional.ofNullable(source.getSourceId())
//                .ifPresent(id -> saveSourceHistory((StmtProcessor.create(SourceHist.class, sourceHist -> sourceHist.assign(source)))));
//    }
    public void saveSource(Source source) {
        sourceRepo.save(source);
    }

}
