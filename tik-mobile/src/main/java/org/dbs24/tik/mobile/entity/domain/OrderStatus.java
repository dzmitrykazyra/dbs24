package org.dbs24.tik.mobile.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tm_order_statuses_ref")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatus {

    @Id
    @Column(name = "order_status_id")
    @NotNull
    private Integer orderStatusId;

    @Column(name = "order_status_name")
    private String orderStatusName;
}