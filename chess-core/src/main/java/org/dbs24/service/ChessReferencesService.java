/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import java.util.Arrays;
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
import org.springframework.stereotype.Service;

@Data
@Slf4j
@Service
@CachedReferencesClasses(classes = {Piece.class, MoveNotice.class, CheckerBoard.class, ChessEngine.class})
public class ChessReferencesService extends AbstractReferencesService {

    public static final Piece findPiece(final String pieceCode) {
        return AbstractRefRecord.<Piece>getRefeenceRecord(Piece.class,
                record -> record.getPieceCode().equals(pieceCode));
    }

    //==========================================================================
    public static <T extends Piece> Collection<T> getPieceCollection() {

        final Collection<T> actualList = ServiceFuncs.<T>createCollection();
        final Class<T> clazz = (Class<T>) (Piece.class);

        final String[][] constList = new String[][]{
            {"K", "King", "Король"},
            {"Q", "Queen", "Ферзь"},
            {"R", "Rook", "Ладья"},
            {"B", "BeeShop", "Слон"},
            {"N", "Knight", "Конь"},
            {"P", "Pawn", "Пешка"}
        };

        Arrays.stream(constList)
                .forEach(stringRow
                        -> actualList.add((T) NullSafe.<T>createObject(clazz, object -> {
                    object.setPieceCode(stringRow[0]);
                    object.setPieceName(AbstractRefRecord.getTranslatedValue(new LangStrValue(stringRow[1], stringRow[2])));
                })));
        return actualList;
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
    public static <T extends ChessEngine> Collection<T> getChessEngineCollection() {

        final Collection<T> actualList = ServiceFuncs.<T>createCollection();
        final Class<T> clazz = (Class<T>) (ChessEngine.class);

        final String[][] engineList = new String[][]{
            {"1", "Deep rybka", "Рыбка"},
            {"2", "StockFish 5", "Стокфиш"}
        };

        Arrays.stream(engineList)
                .unordered()
                .forEach(engine
                        -> actualList.add((T) NullSafe.<T>createObject(clazz, object -> {
                    object.setEngineId(Integer.valueOf(engine[0]));
                    object.setEngineName(AbstractRefRecord.getTranslatedValue(new LangStrValue(engine[1], engine[2])));
                })));

        return actualList;
    }

    //==========================================================================
    public static final MoveNotice findMoveNotice(final String moveResult) {
        return AbstractRefRecord.<MoveNotice>getRefeenceRecord(MoveNotice.class,
                record -> record.getNoticeCode().equals(moveResult));
    }

    //==========================================================================
    public static <T extends MoveNotice> Collection<T> getMoveNoticeCollection() {

        final Collection<T> actualList = ServiceFuncs.<T>createCollection();
        final Class<T> clazz = (Class<T>) (MoveNotice.class);

        final String[][] constList = new String[][]{
            {"+", "Шах", "Шах"},
            {"=", "Deuce", "Ничья"},
            {"#", "Finish", "Мат"},
            {"!", "Good move", "Отл.ход"},
            {"?", "Bad move", "Плохой ход"},
            {"?", "Bad move", "Плохой ход"},
            {"???", "Very Bad move", "Что за нах?"}
        };

        Arrays.stream(constList)
                .unordered()
                .forEach(stringRow
                        -> actualList.add((T) NullSafe.<T>createObject(clazz, object -> {
                    object.setNoticeCode(stringRow[0]);
                    object.setNoticeName(AbstractRefRecord.getTranslatedValue(new LangStrValue(stringRow[1], stringRow[2])));
                })));

        return actualList;
    }
}
