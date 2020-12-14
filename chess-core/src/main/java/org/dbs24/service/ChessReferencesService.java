/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import java.util.Collection;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
@Service
@CachedReferencesClasses(classes = {Piece.class, MoveNotice.class, CheckerBoard.class, ChessEngine.class})
public class ChessReferencesService extends AbstractReferencesService {

    public static final Piece findPiece(String pieceCode) {
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
    public static final CheckerBoard findCheckerboard(String checkerboard) {
        return AbstractRefRecord.<CheckerBoard>getRefeenceRecord(CheckerBoard.class,
                record -> record.getCheckerboardCode().equals(checkerboard));
    }

    //==========================================================================
    public static Collection<CheckerBoard> getCheckerBoardCollection() {

        return AbstractReferencesService.<CheckerBoard>getGenericCollection(CheckerBoard.class, new String[][]{
            {"A1"}, {"A2"}, {"A3"}, {"A4"}, {"A5"}, {"A6"}, {"A7"}, {"A8"},
            {"B1"}, {"B2"}, {"B3"}, {"B4"}, {"B5"}, {"B6"}, {"B7"}, {"B8"},
            {"C1"}, {"C2"}, {"C3"}, {"C4"}, {"C5"}, {"C6"}, {"C7"}, {"C8"},
            {"D1"}, {"D2"}, {"D3"}, {"D4"}, {"D5"}, {"D6"}, {"D7"}, {"D8"},
            {"E1"}, {"E2"}, {"E3"}, {"E4"}, {"E5"}, {"E6"}, {"E7"}, {"E8"},
            {"F1"}, {"F2"}, {"F3"}, {"F4"}, {"F5"}, {"F6"}, {"F7"}, {"F8"},
            {"G1"}, {"G2"}, {"G3"}, {"G4"}, {"G5"}, {"G6"}, {"G7"}, {"G8"},
            {"H1"}, {"H2"}, {"H3"}, {"H4"}, {"H5"}, {"H6"}, {"H7"}, {"H8"}},
                (record, stringRow) -> {
                    record.setCheckerboardCode(stringRow[0]);
                    record.setCheckerboard_Name("Square ".concat(stringRow[0]));
                });
    }

    //==========================================================================
    public static final ChessEngine findEngine(Integer engine) {
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
    public static final MoveNotice findMoveNotice(String moveResult) {
        return AbstractRefRecord.<MoveNotice>getRefeenceRecord(MOVE_NOTICE_CLASS,
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
            {"???", "Very Bad move", "Что за нах?"}},
                (record, stringRow) -> {
                    record.setNoticeCode(stringRow[0]);
                    record.setNoticeName(AbstractRefRecord.getTranslatedValue(new LangStrValue(stringRow[1], stringRow[2])));
                });
    }
}
