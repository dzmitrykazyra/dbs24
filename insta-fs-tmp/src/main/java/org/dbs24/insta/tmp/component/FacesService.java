/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.dbs24.application.core.service.funcs.ServiceFuncs;

import static org.dbs24.insta.api.consts.InstaConsts.Consumers.CON_GROUP_ID;
import static org.dbs24.insta.tmp.consts.IfsConst.Kafka.KAFKA_VECTORS;
import static org.dbs24.insta.tmp.consts.IfsConst.Kafka.KAFKA_PICTURES;

import org.dbs24.insta.tmp.entity.Face;
import org.dbs24.insta.tmp.entity.dto.AccountDto;
import org.dbs24.insta.tmp.entity.dto.FaceDto;
import org.dbs24.insta.tmp.entity.dto.PictureDto;
import org.dbs24.insta.tmp.kafka.api.IgVector;
import org.dbs24.insta.tmp.kafka.api.IgPicture;
import org.dbs24.insta.tmp.repo.AccountDtoRepo;
import org.dbs24.insta.tmp.repo.FaceRepo;
import org.dbs24.insta.tmp.repo.FaceDtoRepo;
import org.dbs24.insta.tmp.repo.PictureDtoRepo;
import org.dbs24.insta.tmp.rest.api.CreatedFace;
import org.dbs24.insta.tmp.rest.api.FaceInfo;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Data
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-tmp")
public class FacesService extends AbstractApplicationService {

    final AccountDtoRepo accountDtoRepo;
    final FaceRepo faceRepo;
    final RefsService refsService;
    final SourcesService sourcesService;
    final Collection<IgVector> hotVectors;
    final Collection<IgPicture> hotPictures;
    final AtomicInteger vectorCounter = new AtomicInteger();
    final AtomicInteger pictureCounter = new AtomicInteger();
    final FaceDtoRepo faceDtoRepo;
    final PictureDtoRepo pictureDtoRepo;

    public FacesService(FaceRepo faceRepo, RefsService refsService, SourcesService sourcesService, FaceDtoRepo faceDtoRepo, PictureDtoRepo pictureDtoRepo, AccountDtoRepo accountDtoRepo) {
        this.faceRepo = faceRepo;
        this.refsService = refsService;
        this.sourcesService = sourcesService;
        this.faceDtoRepo = faceDtoRepo;
        this.pictureDtoRepo = pictureDtoRepo;
        this.accountDtoRepo = accountDtoRepo;

        hotVectors = ServiceFuncs.createConcurencyCollection();
        hotPictures = ServiceFuncs.createConcurencyCollection();
        vectorCounter.set(0);
        pictureCounter.set(0);
    }
    //==========================================================================

    @KafkaListener(id = KAFKA_VECTORS, groupId = CON_GROUP_ID, topics = KAFKA_VECTORS)
    public void receiveVectorsFromKafka(Collection<IgVector> vectors) {

        vectorCounter.addAndGet(vectors.size());
        log.debug("{}: receive vectors/faces: {}/{}", KAFKA_VECTORS, vectors.size(), vectorCounter.get());
        hotVectors.addAll(vectors);
    }

    @KafkaListener(id = KAFKA_PICTURES, groupId = CON_GROUP_ID, topics = KAFKA_PICTURES)
    public void receivePicturesFromKafka(Collection<IgPicture> pictures) {

        pictureCounter.addAndGet(pictures.size());
        log.debug("{}: receive pictures: {}/{}", KAFKA_PICTURES, pictures.size(), pictureCounter.get());
        hotPictures.addAll(pictures);
    }


    //==========================================================================
    @Scheduled(fixedRateString = "${config.faces.posts.processing-interval:5000}", cron = "${config.faces.posts.processing-interval.processing-cron:}")
    @Transactional
    protected void saveFaces() {

        final Collection<FaceDto> newFaces = hotVectors
                .stream()
                .filter(face -> !face.getIsAdded())
                .map(this::createFaceEntity)
                .collect(Collectors.toList());

        StmtProcessor.processCollection(newFaces, faces -> {

            final StopWatcher stopWatcher = StopWatcher.create("saveFaces");

            log.debug("{}: store vectors/faces: {}/{}", KAFKA_VECTORS, faces.size(), hotVectors.size());
            saveFaces(faces);

            StmtProcessor.ifTrue(stopWatcher.getExecutionTime() > Long.valueOf("100"), () -> log.info("{} vectors/faces records: {}", faces.size(), stopWatcher.getStringExecutionTime()));

            hotVectors.removeIf(face -> face.getIsAdded());

        });
    }

    //==========================================================================
    @Scheduled(fixedRateString = "${config.pictures.posts.processing-interval:5000}", cron = "${config.pictures.posts.processing-interval.processing-cron:}")
    @Transactional
    protected void savePictures() {

        final Collection<PictureDto> newPictures = hotPictures
                .stream()
                .filter(picture -> !picture.getIsAdded())
                .map(this::createPictureEntity)
                .collect(Collectors.toList());

        StmtProcessor.processCollection(newPictures, pictures -> {

            final StopWatcher stopWatcher = StopWatcher.create("savePictures");

            log.debug("{}: store pictures: {}/{}", KAFKA_PICTURES, pictures.size(), hotPictures.size());
            savePictures(pictures);

            StmtProcessor.ifTrue(stopWatcher.getExecutionTime() > Long.valueOf("100"), () -> log.info("{} pictures records: {}", pictures.size(), stopWatcher.getStringExecutionTime()));

            hotPictures.removeIf(picture -> picture.getIsAdded());

        });
    }

    //==========================================================================
    private FaceDto createFaceEntity(IgVector igVector) {

        return StmtProcessor.create(FaceDto.class, faceDto -> {

            faceDto.setFaceBoxStr(igVector.getFaceBox());
            faceDto.setActualDate(LocalDateTime.now());
            faceDto.setFaceVector(igVector.getFaceVector());
            faceDto.setSourceId(igVector.getSourceId());
            faceDto.setFaceBox(null);

            final Optional<AccountDto> optAccount = accountDtoRepo.findAccountBySourceId(igVector.getSourceId()).stream().findAny();

            optAccount.ifPresentOrElse(account -> {
                faceDto.setUserName(account.getUserName());
                faceDto.setInstaId(account.getInstaId());
            }, () -> log.error("Can't find account by sourceId: {}", igVector.getSourceId()));

            igVector.setIsAdded(Boolean.TRUE);

        });
    }

    //==========================================================================
    private PictureDto createPictureEntity(IgPicture igPicture) {

        return StmtProcessor.create(PictureDto.class, pictureDto -> {

            pictureDto.setActualDate(LocalDateTime.now());
            pictureDto.setSourceId(igPicture.getSourceId());
            pictureDto.setPicture(igPicture.getPicture());
            igPicture.setIsAdded(Boolean.TRUE);

        });
    }

    //==========================================================================
    @Transactional
    public CreatedFace createOrUpdateFace(FaceInfo faceInfo) {

        final Face face = findOrCreateFace(faceInfo.getSourceFaceId());

        // copy 2 history
        //saveFaceHistory(face);
        face.setFaceBox(faceInfo.getFaceBox());
        face.setActualDate((LocalDateTime) StmtProcessor.nvl(NLS.long2LocalDateTime(faceInfo.getActualDate()), LocalDateTime.now()));
        face.setFaceVector(faceInfo.getFaceVector());
        face.setSource(sourcesService.findOrCreateSource(faceInfo.getSourceId()));

        //face.setFaceId(face.getFaceId());
        saveFace(face);

        return StmtProcessor.create(CreatedFace.class, ca -> {

            ca.setFaceId(face.getSourceFaceId());

            log.debug("try 2 create/update face: {}", face.getSourceFaceId());
        });
    }

    public Face createFace() {
        return StmtProcessor.create(Face.class, a -> {
            a.setActualDate(LocalDateTime.now());
        });
    }

    public Face findFace(Long faceId) {

        return faceRepo
                .findById(faceId)
                .orElseThrow(() -> new RuntimeException(String.format("faceId not found (%d)", faceId)));
    }

    public Face findOrCreateFace(Long faceId) {
        return (Optional.ofNullable(faceId)
                .orElseGet(() -> Long.valueOf("0")) > 0)
                ? findFace(faceId)
                : createFace();
    }

    public void saveFace(Face face) {
        faceRepo.save(face);
    }

    @Transactional
    public synchronized void saveFaces(Collection<FaceDto> faces) {
        faceDtoRepo.saveAllAndFlush(faces);
    }

    @Transactional
    public synchronized void savePictures(Collection<PictureDto> pictures) {
        pictureDtoRepo.saveAllAndFlush(pictures);
    }

}
