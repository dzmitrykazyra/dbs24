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
import javax.persistence.Column;
import javax.persistence.Id;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import java.util.Arrays;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@Entity
@Table(name = "wc_MovesNotationsRef")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MoveNotice extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "notice_code")
    private String noticeCode;
    @Column(name = "notice_name")
    private String noticeName;

    @Override
    public void record2Map(final Map<String, Integer> map) {
        //map.put(String.format("%d - %s", this.getLiasTypeId(), this.getLiasTypeName()), this.getLiasTypeId());
    }

    public static final MoveNotice findMoveNotice(final String moveResult) {
        return AbstractRefRecord.<MoveNotice>getRefeenceRecord(MoveNotice.class,
                record -> record.getNoticeCode().equals(moveResult));
    }

    //==========================================================================
    public static <T extends MoveNotice> Collection<T> getActualReferencesList() {

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
                        -> actualList.add((T) NullSafe.<T>createObject(clazz, (object) -> {
                    object.setNoticeCode(stringRow[0]);
                    object.setNoticeName(AbstractRefRecord.getTranslatedValue(new LangStrValue(stringRow[1], stringRow[2])));
                })));

        return actualList;
    }
}
