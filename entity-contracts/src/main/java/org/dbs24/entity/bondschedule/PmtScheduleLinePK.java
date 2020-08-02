/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.bondschedule;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
public class PmtScheduleLinePK implements Serializable {

    private PmtSchedule pmtSchedule;
    //private Long schedule_id;
    private LocalDate actualDate;
    private LocalDate fromDate;
    private LocalDate toDate;

}
