/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.references.documents.paymentsystem;

/**
 *
 * @author kazyra_d
 */
import java.time.LocalDate;
import java.util.Map;
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
@Table(name = "paymentSystemsRef")
public class PaymentSystem extends AbstractRefRecord implements ReferenceRec {

    @Id
    @Column(name = "pmt_sys_id")
    private Integer pmt_sys_id;
    @Column(name = "pmt_sys_code")
    private String pmt_sys_code;
    @Column(name = "pmt_sys_name")
    private String pmt_sys_name;
    @Column(name = "pmt_open_date")
    private LocalDate pmt_open_date;
    @Column(name = "pmt_close_date")
    private LocalDate pmt_close_date;
}
