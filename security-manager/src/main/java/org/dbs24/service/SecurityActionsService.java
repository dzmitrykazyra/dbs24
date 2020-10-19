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
import static org.dbs24.application.core.sysconst.SysConst.*;

@Service
@EntityClassesPackages(pkgList = {SECURITY_PACKAGE})
public class SecurityActionsService extends AbstractActionExecutionService {

    public ApplicationUser createUser(final String login,
            final String password,
            final String name,
            final String phone,
            final String mail) {

        return this.<ApplicationUser>createActionEntity(ApplicationUser.class,
                user -> {

                    user.setName(name);
                    user.setMail(mail);
                    user.setPhone(phone);
                    user.setPassword(password);
                    user.setCreation_date(LocalDateTime.now());
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
    public ApplicationRole createRole(final String roleCode,
            final String roleName,
            final String name) {

        return this.<ApplicationRole>createActionEntity(ApplicationRole.class,
                role -> {

                    role.setRoleCode(roleCode);
                    role.setRoleName(roleName);
                    role.setCreation_date(LocalDateTime.now());

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
}
