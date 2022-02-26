/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import javax.validation.constraints.NotNull;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;


@Data
@Entity
@Table(name = "payment")
public class Payment extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    //@NotNull
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUser appUser;

    @NotNull
    @Column(name = "FULFIL_TIME")
    private LocalDateTime fulfilTime;

    @NotNull
    @Column(name = "VALID_UNTIL")
    private LocalDateTime ValidUntil;    

    @NotNull
    @Column(name = "PAY_TYPE")
    private String payType;

    @NotNull
    @Column(name = "SUBS_AMOUNT")
    private Integer subsAmount;

    @NotNull
    @Column(name = "PRICE")
    private BigDecimal price;

    @NotNull
    @Column(name = "CUR_CODE")
    private String curCode;

    @NotNull
    @Column(name = "GP_STR_PRICE")
    private String gpStrPrice;
    
    @NotNull
    @Column(name = "GP_ORDER_ID")
    private String gpOrderId;    

    @NotNull
    @Column(name = "GP_PURCHASE_TOKEN")
    private String gpPurchaseToken;    
    
}
