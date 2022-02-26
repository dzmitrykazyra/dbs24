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
@Table(name = "pmt_apple_payments")
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "payment_id", referencedColumnName = "payment_id")
public class ApplePayment extends AbstractPayment {

    @Column(name = "apple_transaction_id")
    private String appleTransactionId;       

    @Column(name = "apple_original_transaction_id")
    private String appleOriginalTransactionId;

    @Column(name = "apple_product_id")
    private String appleProductId;
    
}
