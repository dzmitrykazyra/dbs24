/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.boot;

import com.kdg.fs24.application.core.log.LogService;
import com.kdg.fs24.config.SecurityConst;
import com.kdg.fs24.entity.security.ApplicationUser;
import com.kdg.fs24.entity.security.ApplicationRole;
import test.config.SecurityTestConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.UUID;
import org.springframework.context.annotation.Import;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;
import com.kdg.fs24.repository.*;
import lombok.Data;
import com.kdg.fs24.service.*;

/**
 *
 * @author Козыро Дмитрий
 */
//public class SpringSecurityTests extends SpringBoot4Test<SpringSecurityTestConfig> {
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(SecurityTestConfig.class)
//@DataJpaTest
@Data
//public final class SpringSecurityTests extends Unit4Test<SpringSecurityBoot, SecurityTestConfig> {
public class SecurityTests {

    @Autowired
    private SecurityActionsService securityActionsService;

    @Autowired
    private ApplicationRoleRepository applicationRoleRepository;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;
//
//    @Autowired(required = false)
//    private EntityStatusesRepository entityStatusesRepository;

    @Test
    public void test1() {
        //this.initializeTest();
        LogService.LogInfo(this.getClass(), () -> String.format("Unit test '%s' is running ",
                this.getClass().getCanonicalName()));

        final String testValue = UUID.randomUUID().toString().substring(1, 20);

        //==========================================================
        final ApplicationUser user = securityActionsService.createUser(testValue,
                testValue,
                testValue,
                testValue,
                testValue);

        //entityManager.persist(user);
        // создание пользователя через действие
        securityActionsService.executeAction(user, SecurityConst.ACT_CREATE_OR_MODIFY_USER, null);

        //==========================================================
        //  добавляем роли
        // создание тестовой роли
        final ApplicationRole role = securityActionsService.createRole(testValue,
                testValue,
                testValue);

        //entityManager.persist(user);
        // создание пользователя через действие
        securityActionsService.executeAction(role, SecurityConst.ACT_CREATE_OR_MODIFY_ROLE, null);

        //==========================================================
        LogService.LogInfo(this.getClass(), () -> String.format("\\ %s: %d ================================================",
                "applicationUserRepository", applicationUserRepository.count()));
        //==========================================================
        LogService.LogInfo(this.getClass(), () -> String.format("\\ %s: %d ================================================",
                "applicationRoleRepository", applicationRoleRepository.count()));

    }

//    @Test
//    public void testXX() {
//        //this.initializeTest();
//        LogService.LogInfo(this.getClass(), () -> String.format("Unit test '%s' is running ",
//                this.getClass().getCanonicalName()));
//    }
}
