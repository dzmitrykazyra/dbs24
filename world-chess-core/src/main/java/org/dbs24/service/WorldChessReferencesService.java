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
@CachedReferencesClasses(classes = {Piece.class})
//@CachedReferencesClasses(classes = {Piece.class, MoveNotice.class, CheckerBoard.class, ChessEngine.class})
public class WorldChessReferencesService extends AbstractReferencesService {

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
}
