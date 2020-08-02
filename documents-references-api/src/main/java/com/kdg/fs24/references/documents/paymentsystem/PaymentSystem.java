/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.references.documents.paymentsystem;

/**
 *
 * @author kazyra_d
 */
import java.time.LocalDate;
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

    @Override
    public void record2Map(final Map<String, Integer> map) {
//        map.put(String.format("%d - %s", this.getPmt_sys_id(), this.getPmt_sys_name()), this.getPmt_sys_id());
    }

}
