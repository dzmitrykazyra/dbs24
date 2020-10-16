/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references;

import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.references.api.AbstractRefRecord;
import org.dbs24.references.api.LangStrValue;
import org.dbs24.references.api.ReferenceRec;
import java.util.Collection;
import java.util.Map;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import java.util.Arrays;

@Data
@Entity
@Table(name = "wc_PiecesRef")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Piece extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "piece_code")
    private String pieceCode;
    @Column(name = "piece_name")
    private String pieceName;

    @Override
    public void record2Map(final Map<String, Integer> map) {
        //map.put(String.format("%d - %s", this.getLiasTypeId(), this.getLiasTypeName()), this.getLiasTypeId());
    }

    public static final Piece findPiece(final String pieceCode) {
        return AbstractRefRecord.<Piece>getRefeenceRecord(Piece.class,
                record -> record.getPieceCode().equals(pieceCode));
    }
    //==========================================================================
    public static <T extends Piece> Collection<T> getActualReferencesList() {

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
                .unordered()
                .forEach(stringRow
                        -> actualList.add((T) NullSafe.<T>createObject(clazz, (object) -> {
                    object.setPieceCode(stringRow[0]);
                    object.setPieceName(AbstractRefRecord.getTranslatedValue(new LangStrValue(stringRow[1], stringRow[2])));
                })));        
       
        return actualList;
    }
}
