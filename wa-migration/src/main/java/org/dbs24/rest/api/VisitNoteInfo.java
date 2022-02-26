/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import static org.dbs24.consts.SysConst.DATETIME_MS_FORMAT;
import org.dbs24.entity.VisitNote;

@Data
@NoArgsConstructor
public class VisitNoteInfo {

    private Long id;
    private Integer phoneId;
    private Integer isOnline;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_MS_FORMAT)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime addTime;
    
    @JsonIgnore
    private Boolean added = Boolean.FALSE;

    public void assign(VisitNote visitNote) {
        id = visitNote.getId();
        phoneId = visitNote.getSubscriptionPhone().getId();
        isOnline = visitNote.getIsOnline();
        addTime = visitNote.getAddTime();
    }
}
