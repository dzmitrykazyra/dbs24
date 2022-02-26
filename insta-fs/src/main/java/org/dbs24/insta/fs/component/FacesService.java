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
import org.dbs24.insta.fs.entity.Face;
import org.dbs24.insta.fs.repo.FaceRepo;
import org.dbs24.insta.fs.rest.api.CreatedFace;
import org.dbs24.insta.fs.rest.api.FaceInfo;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Data
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-fs")
public class FacesService extends AbstractApplicationService {

    final FaceRepo faceRepo;
    final RefsService refsService;
    final SourcesService sourcesService;

    public FacesService(FaceRepo faceRepo, RefsService refsService, SourcesService sourcesService) {
        this.faceRepo = faceRepo;
        this.refsService = refsService;
        this.sourcesService = sourcesService;
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

            log.debug("try 2 create/update face: {}", face);
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

//    public void saveFaceHistory(FaceHist faceHist) {
//        faceHistRepo.save(faceHist);
//    }
//    public void saveFaceHistory(Face face) {
//        Optional.ofNullable(face.getFaceId())
//                .ifPresent(id -> saveFaceHistory((StmtProcessor.create(FaceHist.class, faceHist -> faceHist.assign(face)))));
//    }
    public void saveFace(Face face) {
        faceRepo.save(face);
    }
}
