/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.card.pmt.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "pmt_google_payments")
@EqualsAndHashCode
@PrimaryKeyJoinColumn(name = "payment_id", referencedColumnName = "payment_id")
public class GooglePayment extends AbstractPayment {

    @Column(name = "google_purchase_token")
    private String googlePurchaseToken;

    @Column(name = "google_order_id")
    private String googleOrderId;

    @Column(name = "google_sku")
    private String googleSku;
}
