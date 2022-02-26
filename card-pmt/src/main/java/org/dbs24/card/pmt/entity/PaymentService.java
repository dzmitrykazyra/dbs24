/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.card.pmt.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "pmt_payment_services_ref")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class PaymentService extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "payment_service_id", updatable = false)
    private Integer paymentServiceId;
    
    @NotNull
    @Column(name = "payment_service_name", updatable = false)
    private String paymentServiceName;
}
