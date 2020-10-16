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
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import java.util.Arrays;
import java.util.Map;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@Entity
@Table(name = "wc_EnginesRef")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ChessEngine extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "engine_id")
    private Integer engineId;
    @Column(name = "engine_name")
    private String engineName;

    @Override
    public void record2Map(final Map<String, Integer> map) {
        //map.put(String.format("%d - %s", this.getLiasTypeId(), this.getLiasTypeName()), this.getLiasTypeId());
    }

    public static final ChessEngine findEngine(final Integer engine) {
        return AbstractRefRecord.<ChessEngine>getRefeenceRecord(ChessEngine.class,
                record -> record.getEngineId().equals(engine));
    }

    //==========================================================================
    public static <T extends ChessEngine> Collection<T> getActualReferencesList() {

        final Collection<T> actualList = ServiceFuncs.<T>createCollection();
        final Class<T> clazz = (Class<T>) (ChessEngine.class);
        
        final String[][] engineList = new String[][]{
            {"1", "Deep rybka", "Рыбка"},
            {"2", "StockFish 5", "Стокфиш"}
        };
        
        Arrays.stream(engineList)
                .unordered()
                .forEach(engine
                        -> actualList.add((T) NullSafe.<T>createObject(clazz, (object) -> {
                    object.setEngineId(Integer.valueOf(engine[0]));
                    object.setEngineName(AbstractRefRecord.getTranslatedValue(new LangStrValue(engine[1], engine[2])));
                })));

        return actualList;
    }
}
