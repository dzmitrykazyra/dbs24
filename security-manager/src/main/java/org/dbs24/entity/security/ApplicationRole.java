/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.security;

import java.util.Collection;
import lombok.Data;
import javax.persistence.*;
import org.dbs24.entity.core.AbstractActionEntity;
import org.dbs24.entity.core.api.EntityKindId;
import org.dbs24.config.SecurityConst;
import org.dbs24.entity.core.api.EntityTypeId;
import org.dbs24.entity.core.api.ActionClassesPackages;
import org.dbs24.entity.core.api.EntityConst;
import org.dbs24.entity.core.api.EntityStatusesRef;
import org.dbs24.entity.status.EntityStatusId;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@Entity
@Table(name = "core_Roles")
@PrimaryKeyJoinColumn(name = "role_id", referencedColumnName = "entity_id")
@ActionClassesPackages(pkgList = {"org.dbs24.actions"})
@EntityTypeId(entity_type_id = SecurityConst.FS24_ROLE,
        entity_type_name = "Роль в учетной системе")
@EntityKindId(entity_kind_id = SecurityConst.FS24_ROLE_BASE,
        entity_type_id = SecurityConst.FS24_ROLE,
        entity_kind_name = "Стандартная роль комплекса")
@EntityStatusesRef(
        entiy_status = {
            @EntityStatusId(
                    entity_type_id = SecurityConst.FS24_ROLE,
                    entity_status_id = EntityConst.ES_ACTUAL,
                    entity_status_name = "Действующая роль")
            ,
            @EntityStatusId(
                    entity_type_id = SecurityConst.FS24_ROLE,
                    entity_status_id = EntityConst.ES_CLOSED,
                    entity_status_name = "Закрытая роль")
            ,
            @EntityStatusId(
                    entity_type_id = SecurityConst.FS24_ROLE,
                    entity_status_id = EntityConst.ES_CANCELLED,
                    entity_status_name = "Аннулированная роль")
        })
public class ApplicationRole extends AbstractActionEntity {

    @Column(name = "role_code")
    private String roleCode;
    @Column(name = "role_name")
    private String roleName;
    @ManyToMany(mappedBy = "userRoles")
    private Collection<ApplicationUser> roleUsers;

    public Long getRole_id() {
        return super.getEntity_id();
    }

}
