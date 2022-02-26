/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import static org.dbs24.consts.SysConst.DATETIME_MS_FORMAT;

@Data
@NoArgsConstructor
public class UserContractInfo {

    private Integer contractId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_MS_FORMAT)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime actualDate;
    private Integer userId;
    private Integer contractTypeId;
    private Byte contractStatusId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_MS_FORMAT)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime beginDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_MS_FORMAT)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime endDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_MS_FORMAT)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime cancelDate;
    private Integer subscriptionsAmount;
}
