/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references;

import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.references.api.AbstractRefRecord;
import org.dbs24.references.api.ReferenceRec;
import java.util.Collection;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.dbs24.application.core.service.funcs.ServiceFuncs;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@Entity
@Table(name = "wc_CheckerBoardsRef")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CheckerBoard extends AbstractRefRecord implements ReferenceRec {
    
    @Id
    @Column(name = "checkerboard_code")
    private String checkerboardCode;
    @Column(name = "checkerboard_name")
    private String checkerboard_Name;
      
    public static final CheckerBoard findCheckerboard(final String checkerboard) {
        return AbstractRefRecord.<CheckerBoard>getRefeenceRecord(CheckerBoard.class,
                record -> record.getCheckerboardCode().equals(checkerboard));
    }

    //==========================================================================
    public static <T extends CheckerBoard> Collection<T> getActualReferencesList() {
        
        final Collection<T> actualList = ServiceFuncs.<T>createCollection();
        final Class<T> clazz = (Class<T>) (CheckerBoard.class);
        
        final Integer n = 8;
        Integer i, j;
        
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                
                final String checkerboardCode = String.format("%s%s", (char) (i + 65), j + 1);
                
                actualList.add((T) NullSafe.<T>createObject(clazz, (object) -> {
                    object.setCheckerboardCode(checkerboardCode);
                    object.setCheckerboard_Name(checkerboardCode);
                }));
            }
        }

//        actualList.add((T) NullSafe.<T>createObject(clazz, (object) -> {
//            object.setPieceCode("Q");
//            object.setPieceName(AbstractRefRecord.getTranslatedValue(new LangStrValue("Queen", "Ферзь")));
//        }));
        return actualList;
    }
}
