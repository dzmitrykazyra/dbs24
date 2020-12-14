/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.bondschedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.dbs24.persistence.api.PersistenceEntity;
import javax.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.dbs24.consts.SysConst.DATE_FORMAT;


@Data
@Entity
@Table(name = "core_PmtScheduleLines")
@IdClass(PmtScheduleLinePK.class)
public class PmtScheduleLine implements PersistenceEntity {

    @Id
    //@Column(name = "schedule_id")
    @JoinColumn(name = "schedule_id", referencedColumnName = "schedule_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private PmtSchedule pmtSchedule;

//    @Id
//    private Long schedule_id;
    @Id
    @Column(name = "actual_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)     
    private LocalDate actualDate;
    @Id
    @Column(name = "from_date")
    private LocalDate fromDate;
    @Id
    @Column(name = "to_date")
    private LocalDate toDate;
    @Column(name = "appear_date")
    private LocalDate appearDate;
    @Column(name = "pay_sum")
    private BigDecimal paySum;
    @Column(name = "calc_date")
    private LocalDate calcDate;

}
