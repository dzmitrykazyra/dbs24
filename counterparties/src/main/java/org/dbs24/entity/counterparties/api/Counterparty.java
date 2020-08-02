/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.counterparties.api;

import javax.persistence.*;
import lombok.Data;
import org.dbs24.entity.core.AbstractActionEntity;
import org.dbs24.entity.core.api.EntityKindId;
import org.dbs24.entity.core.api.EntityTypeId;
import org.dbs24.entity.core.api.ActionClassesPackages;
import org.dbs24.entity.core.api.EntityStatusesRef;
import org.dbs24.entity.status.EntityStatusId;
import org.dbs24.entity.core.api.EntityConst;

/**
 *
 * @author Козыро Дмитрий
 */
@Entity
@Table(name = "core_Counterparties")
@Data
@PrimaryKeyJoinColumn(name = "counterparty_id", referencedColumnName = "entity_id")
@ActionClassesPackages(pkgList = {"org.dbs24.counterparties.actions"})
@EntityTypeId(entity_type_id = CounterpartyConst.FS24_CLIENT,
        entity_type_name = "Клиент")
@EntityKindId(entity_kind_id = CounterpartyConst.FS24_ODINARY_CLIENT,
        entity_type_id = CounterpartyConst.FS24_CLIENT,
        entity_kind_name = "Стандартный клиент")
@EntityStatusesRef(
        entiy_status = {
            @EntityStatusId(
                    entity_type_id = CounterpartyConst.FS24_CLIENT,
                    entity_status_id = EntityConst.ES_ACTUAL,
                    entity_status_name = "Действующая клиент")
            ,
            @EntityStatusId(
                    entity_type_id = CounterpartyConst.FS24_CLIENT,
                    entity_status_id = EntityConst.ES_CLOSED,
                    entity_status_name = "Закрытый клиент")
            ,
            @EntityStatusId(
                    entity_type_id = CounterpartyConst.FS24_CLIENT,
                    entity_status_id = EntityConst.ES_CANCELLED,
                    entity_status_name = "Арестованный клиент")
        })
public class Counterparty extends AbstractActionEntity {

//    @Column(name = "counterparty_id")
//    private Long counterpartyId;
    @Column(name = "counterparty_code")
    private String counterpartyCode;
    @Column(name = "short_name")
    private String shortName;
    @Column(name = "full_name")
    private String fullName;

    public Long getCounterpartyId() {
        return super.getEntity_id();
    }
}
