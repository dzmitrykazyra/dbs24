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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import org.dbs24.entity.Payment;
import lombok.NoArgsConstructor;
import static org.dbs24.consts.SysConst.DATETIME_MS_FORMAT;

@Data
@NoArgsConstructor
public class PaymentInfo {

    private Integer id;
    private Integer user_id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_MS_FORMAT)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime fulfilTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_MS_FORMAT)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime validUntil;
    private String PayType;
    private Integer SubsAmount;
    private BigDecimal price;
    private String CurCode;
    private String GpStrPrice;
    private String GpOrderId;
    private String GpPurchaseToken;

    public void assign(Payment payment) {
        this.id = payment.getId();
        this.CurCode = payment.getCurCode();
        this.GpOrderId = payment.getGpOrderId();
        this.GpPurchaseToken = payment.getGpPurchaseToken();
        this.GpStrPrice = payment.getGpStrPrice();
        this.PayType = payment.getPayType();
        this.SubsAmount = payment.getSubsAmount();
        this.fulfilTime = payment.getFulfilTime();
        this.price = payment.getPrice();
        this.user_id = payment.getAppUser().getId();
        this.validUntil = payment.getValidUntil();
    }
}
