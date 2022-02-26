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
@Table(name = "tm_order_action_types_ref")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderActionType {

    @Id
    @Column(name = "order_action_type_id")
    @NotNull
    private Integer orderActionTypeId;

    @Column(name = "order_action_type_name")
    private String orderActionTypeName;
}
