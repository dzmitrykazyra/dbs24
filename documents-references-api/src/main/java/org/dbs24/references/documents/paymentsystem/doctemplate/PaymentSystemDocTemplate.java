/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.documents.paymentsystem.doctemplate;

import java.util.Map;
import java.time.LocalDate;
import org.dbs24.references.api.ReferenceRec;
import org.dbs24.references.api.AbstractRefRecord;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

@Data
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "liasDebtStatesRef")
public class PaymentSystemDocTemplate extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "pmt_sys_id")
    private Integer pmtSysId;
    @Column(name = "fin_oper_code")
    private Integer finOperCode;
    @Column(name = "doc_template_id")
    private Integer docTemplateId;
    @Column(name = "actual_date")
    private LocalDate actualDate;
    @Column(name = "close_date")
    private LocalDate closeDate;
}