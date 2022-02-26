/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import static org.dbs24.insta.api.consts.InstaConsts.Consumers.CON_GROUP_ID;
import static org.dbs24.insta.tmp.consts.IfsConst.Kafka.KAFKA_SOURCES;
import org.dbs24.insta.tmp.entity.Source;
import org.dbs24.insta.tmp.entity.dto.SourceDto;
import org.dbs24.insta.tmp.kafka.api.IgSource;
import org.dbs24.insta.tmp.repo.SourceRepo;
import org.dbs24.insta.tmp.repo.SourceDtoRepo;
import org.dbs24.insta.tmp.rest.api.CreatedSource;
import org.dbs24.insta.tmp.rest.api.SourceInfo;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.atomic.AtomicInteger;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.component.PersistenceService;
import org.springframework.scheduling.annotation.Scheduled;
import org.dbs24.insta.tmp.kafka.KafkaService;
import org.dbs24.insta.tmp.kafka.api.IgSourceTask;
import static org.dbs24.insta.tmp.consts.IfsConst.References.SourceStatuses.SS_ACTUAL;
import javax.persistence.EntityTransaction;

@Data
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-tmp")
public class SourcesService extends AbstractApplicationService {

    final SourceRepo sourceRepo;
    final SourceDtoRepo sourceDtoRepo;
    final RefsService refsService;
    final AccountsService accountsService;
    final PostsService postsService;
    final Collection<IgSource> hotSources;
    final KafkaService kafkaService;
    final PersistenceService persistenceService;

    final AtomicInteger srcCounter = new AtomicInteger();

    public SourcesService(SourceRepo sourceRepo, SourceDtoRepo sourceDtoRepo, RefsService refsService, AccountsService accountsService, PostsService postsService, KafkaService kafkaService, PersistenceService persistenceService) {
        this.sourceRepo = sourceRepo;
        this.sourceDtoRepo = sourceDtoRepo;
        this.refsService = refsService;
        this.accountsService = accountsService;
        this.postsService = postsService;
        this.hotSources = ServiceFuncs.createConcurencyCollection();
        this.kafkaService = kafkaService;
        this.persistenceService = persistenceService;

        srcCounter.set(0);
    }

    //==========================================================================
    @KafkaListener(id = KAFKA_SOURCES, groupId = CON_GROUP_ID, topics = KAFKA_SOURCES)
    public void createSourcesFromKafka(Collection<IgSource> posts) {

        srcCounter.addAndGet(posts.size());

        log.debug("{}: receive sources: {}/{}", KAFKA_SOURCES, posts.size(), srcCounter.get());

        hotSources.addAll(posts);

    }

//    @Scheduled(fixedRateString = "${config.crw.sources.processing-interval:2000}", cron = "${config.crw.sources.processing-interval.processing-cron:}")
//    @Transactional
    @Deprecated
    protected void saveAllSources() {

        final StopWatcher stopWatcher = StopWatcher.create("saveAllSources");

        final EntityTransaction entityTransaction = persistenceService.getEntityManager().getTransaction();

        final Boolean isActiveTransaction = entityTransaction.isActive();

        if (!isActiveTransaction) {
            entityTransaction.begin();
        }

        if (!persistenceService.getEntityManager().isJoinedToTransaction()) {
            persistenceService.getEntityManager().joinTransaction();
        }

        hotSources
                .stream()
                .filter(source -> !source.getIsAdded())
                .limit(1500)
                .forEach(source -> {

                    sourceRepo.bulkInsert(source.getInstaPostId(),
                            SS_ACTUAL,
                            LocalDateTime.now(),
                            source.getSourceUrl(),
                            source.getSourceHash(),
                            source.getMainFaceBox());

                    source.setIsAdded(Boolean.TRUE);

                });

        persistenceService.getEntityManager().flush();
        entityTransaction.commit();
        persistenceService.getEntityManager().clear();

        hotSources.removeIf(source -> source.getIsAdded());

        StmtProcessor.ifTrue(stopWatcher.getExecutionTime() > Long.valueOf("100"), () -> log.info(stopWatcher.getStringExecutionTime()));
    }

    @Scheduled(fixedRateString = "${config.crw.sources.processing-interval:2000}", cron = "${config.crw.sources.processing-interval.processing-cron:}")
    @Transactional
    protected void saveSources() {

        final Collection<SourceDto> newSources = hotSources
                .stream()
                .filter(source -> !source.getIsAdded())
                .map(this::createSourceEntity)
                .collect(Collectors.toList());

        final StopWatcher stopWatcher = StopWatcher.create("saveSources");

        StmtProcessor.ifTrue(!newSources.isEmpty(), () -> {
            log.debug("{}: store sources: {}/{}", KAFKA_SOURCES, newSources.size(), hotSources.size());
            saveSources(newSources);

            StmtProcessor.ifTrue(stopWatcher.getExecutionTime() > Long.valueOf("100"), () -> log.info("{} records: {}", newSources.size(), stopWatcher.getStringExecutionTime()));

            hotSources.removeIf(source -> source.getIsAdded());

            // send task to process images
            newSources
                    .stream()
                    //.limit(1) // remove in production
                    .forEach(source -> kafkaService.sendSourceTask(
                    StmtProcessor.create(IgSourceTask.class,
                            ist -> {
                                ist.setSourceId(source.getSourceId());
                                ist.setSourceUrl(source.getSourceUrl());
                                ist.setPartitionId(
                                        0);
                            })));
        });
    }

    private SourceDto createSourceEntity(IgSource igSource) {

        return StmtProcessor.create(SourceDto.class, sourceDto -> {

            //source.setPost(postsService.findHotPost(igSource.getInstaPostId()).orElseGet(() -> postsService.findPostByInstaPostId(igSource.getInstaPostId())));
            //sourceDto.setPost(postsService.findPostByInstaPostId(igSource.getInstaPostId()));
            sourceDto.setPostId(igSource.getInstaPostId());
            sourceDto.setActualDate((LocalDateTime) StmtProcessor.nvl(NLS.long2LocalDateTime(igSource.getActualDate()), LocalDateTime.now()));
            sourceDto.setSourceStatus(refsService.findSourceStatus(igSource.getSourceStatusId()));
            sourceDto.setMainFaceBox(igSource.getMainFaceBox());
            sourceDto.setSourceHash(igSource.getSourceHash());
            sourceDto.setSourceUrl(igSource.getSourceUrl());

            //StmtProcessor.ifNull(source.getPost().getPostId(), () -> log.error("post not commited yet ({})", igSource.getInstaPostId()));
            igSource.setIsAdded(Boolean.TRUE);

        });

    }

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

        return StmtProcessor.create(CreatedSource.class,
                ca -> {

                    ca.setSourceId(source.getSourceId());

                    log.debug(
                            "try 2 create/update source: {}", source.getSourceId());

                });
    }

    public Source createSource() {
        return StmtProcessor.create(Source.class,
                a -> {
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

    public synchronized void saveSources(Collection<SourceDto> sources) {

        sourceDtoRepo.saveAllAndFlush(sources);
    }
}
