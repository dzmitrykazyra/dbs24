/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import org.dbs24.entity.security.ApplicationRole;
import org.dbs24.entity.security.ApplicationUser;
import org.dbs24.application.core.nullsafe.NullSafe;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.dbs24.entity.core.api.EntityClassesPackages;
import org.dbs24.entity.status.EntityStatus;
import org.dbs24.entity.status.EntityStatusPK;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.annotation.PostConstruct;
import static org.dbs24.consts.SysConst.SERVICE_USER_ID;
import static org.dbs24.consts.SecurityConst.ACT_CREATE_OR_MODIFY_USER;
import static org.dbs24.consts.SysConst.*;
import org.dbs24.mime.MimeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

@Data
@Service
@EntityClassesPackages(pkgList = {SECURITY_PACKAGE})
public class SecurityActionsService extends AbstractActionExecutionService {

    final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityActionsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public ApplicationUser createUser(String login,
            final String password,
            final String name,
            final String phone,
            final String mail) {

        return this.<ApplicationUser>createActionEntity(ApplicationUser.class,
                user -> {

                    user.setName(name);
                    user.setMail(mail);
                    user.setPhone(phone);
                    user.setPassword(passwordEncoder.encode(password));
                    user.setCreationDate(LocalDateTime.now());
                    user.setLogin(login);

                    final EntityStatusPK entityStatusPK = NullSafe.createObject(EntityStatusPK.class);

                    entityStatusPK.setEntityStatusId(1);
                    entityStatusPK.setEntityTypeId(100);

                    final EntityStatus userStatus = this.getPersistenceEntityManager()
                            .getEntityManager()
                            .find(EntityStatus.class, entityStatusPK);

                    user.setEntityStatus(userStatus);
                });
    }

    //==========================================================================
    public ApplicationRole createRole(String roleCode,
            final String roleName,
            final String name) {

        return this.<ApplicationRole>createActionEntity(ApplicationRole.class,
                role -> {

                    role.setRoleCode(roleCode);
                    role.setRoleName(roleName);
                    role.setCreationDate(LocalDateTime.now());

                    final EntityStatusPK roleStatusPK = NullSafe.createObject(EntityStatusPK.class);

                    roleStatusPK.setEntityStatusId(1);
                    roleStatusPK.setEntityTypeId(101);

                    //this.getPersistenceEntityManager().getEntityManager()
                    final EntityStatus roleStatus = this.getPersistenceEntityManager()
                            .getEntityManager()
                            .find(EntityStatus.class, roleStatusPK);
                    role.setEntityStatus(roleStatus);
                });
    }

    //==========================================================================
    @PostConstruct
    public void createOrUpdateRootUser() {

        // create root user
        final ApplicationUser applicationUser = Optional.ofNullable(this.getPersistenceEntityManager()
                .getEntityManager()
                .find(ApplicationUser.class, SERVICE_USER_ID))
                .orElseGet(() -> {

                    final ApplicationUser rootUser = this.createUser("root", "no password", "root", "101", "dbs24.dev@tut.by");

                    getPersistenceEntityManager()
                            .executeTransaction(em -> em.persist(rootUser));

                    return rootUser;
                });

        Assert.isTrue(applicationUser.getEntity_id().equals(SERVICE_USER_ID),
                String.format("Service/Root user_id/entity_id should equals Long(%s)", SERVICE_USER_ID));
    }
    //==========================================================================
        public ApplicationUser createAndSaveUser(String login,
            final String password,
            final String name,
            final String phone,
            final String mail) {

        final ApplicationUser applicationUser = this.createUser(login, password, name, phone, mail);

        return this.<ApplicationUser>createEntity(applicationUser, map -> {
            map.put(MimeTypes.ENTITY_ACTION_ID.toString(), String.valueOf(ACT_CREATE_OR_MODIFY_USER));
            map.put(MimeTypes.ENTITY_USER_ID.toString(), String.valueOf(SERVICE_USER_ID));
        });
    }
}
