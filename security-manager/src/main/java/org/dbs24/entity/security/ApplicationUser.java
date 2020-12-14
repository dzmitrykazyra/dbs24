/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.security;

import org.dbs24.consts.SecurityConst;
import org.dbs24.entity.core.AbstractActionEntity;
import org.dbs24.entity.core.api.EntityKindId;
import org.dbs24.entity.core.api.EntityTypeId;
import java.util.Collection;
import javax.persistence.*;
import lombok.Data;
import org.dbs24.entity.core.api.ActionClassesPackages;
import org.dbs24.consts.EntityConst;
import org.dbs24.entity.core.api.EntityStatusesRef;
import org.dbs24.entity.status.EntityStatusId;

@Data
@Entity
@Table(name = "core_Users")
@PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "entity_id")
@ActionClassesPackages(pkgList = {"org.dbs24.actions"})
@EntityTypeId(entity_type_id = SecurityConst.FS24_USER,
        entity_type_name = "Пользователь комплекса")
@EntityKindId(entity_kind_id = SecurityConst.FS24_USER_BASE,
        entity_type_id = SecurityConst.FS24_USER,
        entity_kind_name = "Стандартный пользователь комплекса")
@EntityStatusesRef(
        entiy_status = {
            @EntityStatusId(
                    entity_type_id = SecurityConst.FS24_USER,
                    entity_status_id = EntityConst.ES_ACTUAL,
                    entity_status_name = "Действующий пользователь")
            ,
            @EntityStatusId(
                    entity_type_id = SecurityConst.FS24_USER,
                    entity_status_id = EntityConst.ES_CLOSED,
                    entity_status_name = "Закрытый пользователь")
            ,
            @EntityStatusId(
                    entity_type_id = SecurityConst.FS24_USER,
                    entity_status_id = EntityConst.ES_CANCELLED,
                    entity_status_name = "Аннулированный пользователь")
        })
public class ApplicationUser extends AbstractActionEntity {

    @Column(name = "login")
    private String login;
    @Column(name = "password")
    private String password; // BASE-256
    @Column(name = "name")
    private String name;
    @Column(name = "phone")
    private String phone;
    @Column(name = "mail")
    private String mail;

    @Embedded
    private ApplicationUserDetails applicationUserDetails;

    @ManyToMany
    @JoinTable(name = "core_User2Role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<ApplicationRole> userRoles;

    public Long getUserId() {
        return super.getEntity_id();
    }
}
