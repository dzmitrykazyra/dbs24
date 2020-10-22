/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import java.util.Collection;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.references.CheckerBoard;
import org.dbs24.references.ChessEngine;
import org.dbs24.references.MoveNotice;
import org.dbs24.references.Piece;
import org.dbs24.references.api.AbstractRefRecord;
import org.dbs24.references.api.LangStrValue;
import org.dbs24.references.core.CachedReferencesClasses;
import static org.dbs24.consts.WorldChessConst.*;
import org.springframework.stereotype.Service;

@Data
@Slf4j
@Service
@CachedReferencesClasses(classes = {Piece.class, MoveNotice.class, CheckerBoard.class, ChessEngine.class})
public class ChessReferencesService extends AbstractReferencesService {

    public static final Piece findPiece(final String pieceCode) {
        return AbstractRefRecord.<Piece>getRefeenceRecord(PIECE_CLASS,
                record -> record.getPieceCode().equals(pieceCode));
    }

    //==========================================================================
    public static Collection<Piece> getPieceCollection() {

        return AbstractReferencesService.<Piece>getGenericCollection(PIECE_CLASS, new String[][]{
            {"K", "King", "Король"},
            {"Q", "Queen", "Ферзь"},
            {"R", "Rook", "Ладья"},
            {"B", "BeeShop", "Слон"},
            {"N", "Knight", "Конь"},
            {"P", "Pawn", "Пешка"}},
                (record, stringRow) -> {
                    record.setPieceCode(stringRow[0]);
                    record.setPieceName(AbstractRefRecord.getTranslatedValue(new LangStrValue(stringRow[1], stringRow[2])));
                });
    }

    //==========================================================================
    public static final CheckerBoard findCheckerboard(final String checkerboard) {
        return AbstractRefRecord.<CheckerBoard>getRefeenceRecord(CheckerBoard.class,
                record -> record.getCheckerboardCode().equals(checkerboard));
    }

    //==========================================================================
    public static <T extends CheckerBoard> Collection<T> getCheckerBoardCollection() {

        final Collection<T> actualList = ServiceFuncs.<T>createCollection();
        final Class<T> clazz = (Class<T>) (CheckerBoard.class);

        final Integer n = 8;
        Integer i, j;

        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {

                final String checkerboardCode = String.format("%s%s", (char) (i + 65), j + 1);

                actualList.add((T) NullSafe.<T>createObject(clazz, object -> {
                    object.setCheckerboardCode(checkerboardCode);
                    object.setCheckerboard_Name(checkerboardCode);
                }));
            }
        }
        return actualList;
    }

    //==========================================================================
    public static final ChessEngine findEngine(final Integer engine) {
        return AbstractRefRecord.<ChessEngine>getRefeenceRecord(ChessEngine.class,
                record -> record.getEngineId().equals(engine));
    }

    //==========================================================================
    public static Collection<ChessEngine> getChessEngineCollection() {

        return AbstractReferencesService.<ChessEngine>getGenericCollection(CHESS_ENGINE_CLASS, new String[][]{
            {"1", "DR2", "Deep rybka", "Рыбка"},
            {"2", "SF5", "StockFish 5", "Стокфиш"}},
                (record, stringRow) -> {
                    record.setEngineId(Integer.valueOf(stringRow[0]));
                    record.setEngineCode(stringRow[1]);
                    record.setEngineName(AbstractRefRecord.getTranslatedValue(new LangStrValue(stringRow[2], stringRow[3])));
                });
    }

    //==========================================================================
    public static final MoveNotice findMoveNotice(final String moveResult) {
        return AbstractRefRecord.<MoveNotice>getRefeenceRecord(MoveNotice.class,
                record -> record.getNoticeCode().equals(moveResult));
    }

    //==========================================================================
    public static Collection<MoveNotice> getMoveNoticeCollection() {

        return AbstractReferencesService.<MoveNotice>getGenericCollection(MOVE_NOTICE_CLASS, new String[][]{
            {"+", "Шах", "Шах"},
            {"=", "Deuce", "Ничья"},
            {"#", "Finish", "Мат"},
            {"!", "Good move", "Отл.ход"},
            {"?", "Bad move", "Плохой ход"},
            {"?", "Bad move", "Плохой ход"},
            {"???", "Very Bad move", "Что за нах?"}
        }, (record, stringRow) -> {
            record.setNoticeCode(stringRow[0]);
            record.setNoticeName(AbstractRefRecord.getTranslatedValue(new LangStrValue(stringRow[1], stringRow[2])));
        });
    }
}
