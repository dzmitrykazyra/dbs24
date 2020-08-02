/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.contract.subjects;

/**
 *
 * @author kazyra_d
 */
import com.kdg.fs24.application.core.service.funcs.ServiceFuncs;
import com.kdg.fs24.application.core.exception.api.InternalAppException;
import java.util.Map;
import java.util.Optional;
import com.kdg.fs24.references.api.ReferenceRec;
import com.kdg.fs24.references.api.AbstractRefRecord;
import java.util.stream.Collectors;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "core_ContractSubjectsRef")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class ContractSubject extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "contract_subject_id")
    private Integer contractSubjectId;
    @Column(name = "contract_subject_name")
    private String contractSubjectName;

    @Override
    public void record2Map(final Map<String, Integer> map) {
        map.put(this.toString(), this.getContractSubjectId());
    }

    //==========================================================================
    public final static ContractSubject findContractSubject(final Integer liasContractSubjectId) {
        return AbstractRefRecord.<ContractSubject>getRefeenceRecord(ContractSubject.class,
                record -> record.getContractSubjectId().equals(liasContractSubjectId));
    }
}
