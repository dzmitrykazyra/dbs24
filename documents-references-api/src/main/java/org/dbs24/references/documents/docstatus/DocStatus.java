/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.documents.docstatus;

import org.dbs24.references.api.ReferenceRec;
import org.dbs24.references.api.AbstractRefRecord;
import java.util.Map;
import java.util.Collection;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.references.api.LangStrValue;
import org.dbs24.references.api.DocumentsConst;
import java.util.Arrays;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

/**
 *
 * @author kazyra_d
 */
@Data
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "docStatusesRef")
public class DocStatus extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "doc_status_id")
    private Integer docStatusId;

    @Column(name = "doc_status_name")
    private String docStatusName;

    @Override
    public void record2Map(final Map<String, Integer> map) {
        //map.put(String.format("%d - %s", this.getDoc_status_id(), this.getDoc_status_name()), this.getDoc_status_id());
    }

    //==========================================================================
    public final static DocStatus findDocStatus(final Integer statusId) {
        return AbstractRefRecord.<DocStatus>getRefeenceRecord(DocStatus.class,
                record -> record.getDocStatusId().equals(statusId));
    }

    //==========================================================================
    public static <T extends DocStatus> Collection<T> getActualReferencesList() {

        final Collection<T> actualList = ServiceFuncs.<T>createCollection();
        final Class<T> clazz = (Class<T>) (DocStatus.class);

        final String[][] constList = new String[][]{
            {String.valueOf(DocumentsConst.DS_CREATED), "Created", "Создан"},
            {String.valueOf(DocumentsConst.DS_CANCELLED), "Cancelled", "Аннулирован"},
            {String.valueOf(DocumentsConst.DS_POSTPONED), "Postponed", "Отложен"},
            {String.valueOf(DocumentsConst.DS_EXECUTED), "Executed", "Исполнен"}
        };

        Arrays.stream(constList)
                .unordered()
                .forEach(stringRow
                        -> actualList.add((T) NullSafe.<T>createObject(clazz, (object) -> {
                    object.setDocStatusId(Integer.valueOf(stringRow[0]));
                    object.setDocStatusName(AbstractRefRecord.getTranslatedValue(new LangStrValue(stringRow[1], stringRow[2])));
                })));

        return actualList;
    }
}
