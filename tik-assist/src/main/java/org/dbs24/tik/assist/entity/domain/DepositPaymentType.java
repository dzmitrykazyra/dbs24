package org.dbs24.tik.assist.entity.domain;

import lombok.Data;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "tik_payment_types_ref")
public class DepositPaymentType extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "payment_type_id", updatable = false)
    private Integer paymentTypeId;

    @NotNull
    @Column(name = "payment_type_name")
    private String paymentTypeName;
}