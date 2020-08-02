/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.references.liases.finopercode;

import java.util.Map;
import com.kdg.fs24.references.api.ReferenceRec;
import com.kdg.fs24.references.api.AbstractRefRecord;
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
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "liasFinOperCodesRef")
public class LiasFinOperCode extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "fin_oper_code")
    private Integer finOperCode;
    @Column(name = "fin_oper_name")
    private String finOperName;

    @Override
    public void record2Map(final Map<String, Integer> map) {
        map.put(String.format("%d - %s", this.getFinOperCode(), this.getFinOperName()), this.getFinOperCode());
    }
        public final static LiasFinOperCode findLiasFinOperCode(final Integer liasFinOperCodeId) {
        return AbstractRefRecord.<LiasFinOperCode>getRefeenceRecord(LiasFinOperCode.class,
                record -> record.getFinOperCode().equals(liasFinOperCodeId));
    }
}
