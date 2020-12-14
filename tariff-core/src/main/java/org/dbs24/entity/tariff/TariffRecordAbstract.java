/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.tariff;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.dbs24.references.tariffs.kind.TariffRateRecord;
import org.dbs24.entity.Currency;
import java.time.LocalDate;
import javax.persistence.*;
import lombok.Data;
import static org.dbs24.consts.SysConst.DATE_FORMAT;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@MappedSuperclass
public class TariffRecordAbstract extends TariffRateRecord {

    @Id
    @ManyToOne
    @JoinColumn(name = "rate_id")
    @JsonIgnore
    private TariffRate tariffRate;
    //--------------------------------------------------------------------------
    @Id
    @Column(name = "rate_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)     
    private LocalDate rateDate;
    @Id
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;
}
