/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.boot;

import org.dbs24.application.core.log.LogService;
import org.dbs24.repository.ApplicationUserRepository;
import org.dbs24.repository.ApplicationRoleRepository;
import org.dbs24.spring.unit.SpringBoot4Test;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.dbs24.application.core.sysconst.SysConst;

/**
 *
 * @author Козыро Дмитрий
 */
@SpringBootApplication
@ComponentScan(basePackages = SysConst.SERVICE_PACKAGE)
@PropertySource(SysConst.APP_PROPERTIES)
@EntityScan(basePackages = {SysConst.SECURITY_PACKAGE, SysConst.ENTITY_PACKAGE})
@EnableJpaRepositories(basePackages = SysConst.REPOSITORY_PACKAGE)
//public class SpringSecurityBoot extends AbstractSpringBootApplication {
public class SpringSecurityBoot extends SpringBoot4Test {

    //==========================================================================
//    @Bean
//    public CommandLineRunner clrRoleRepository(final ApplicationRoleRepository applicationRoleRepository) {
//        return (args) -> {
//
//            // final Collection<KeyWordRecords> op2 = keyWordsRepository.findKeyWords();
//            LogService.LogInfo(this.getClass(), () -> String.format("ApplicationRoleRepository: %d entries, %s ",
//                    applicationRoleRepository.count(),
//                    applicationRoleRepository.getClass().getCanonicalName()));
//
//        };
//    }
    //==========================================================================
    @Bean
    public CommandLineRunner clrUserRepository(final ApplicationUserRepository applicationUserRepository) {
        return (args) -> {

            //applicationUserRepository.findByPassword("dd");
            // final Collection<KeyWordRecords> op2 = keyWordsRepository.findKeyWords();
            LogService.LogInfo(this.getClass(), () -> String.format("applicationUserRepository: %d entries, %s ",
                    applicationUserRepository.count(),
                    applicationUserRepository.getClass().getCanonicalName()));

//            applicationUserRepository
//                    .findAll()
//                    .forEach(user -> {
//                        LogService.LogInfo(this.getClass(), () -> String.format("applicationUserRepository: %s, %s ",
//                                user.getName(),
//                                user.getClass().getCanonicalName()));
        };

    }
    //==========================================================================
    @Bean
    public CommandLineRunner clrRoleRepository(final ApplicationRoleRepository applicationRoleRepository) {
        return (args) -> {

            //applicationUserRepository.findByPassword("dd");
            // final Collection<KeyWordRecords> op2 = keyWordsRepository.findKeyWords();
            LogService.LogInfo(this.getClass(), () -> String.format("applicationRoleRepository: %d entries, %s ",
                    applicationRoleRepository.count(),
                    applicationRoleRepository.getClass().getCanonicalName()));

//            applicationUserRepository
//                    .findAll()
//                    .forEach(user -> {
//                        LogService.LogInfo(this.getClass(), () -> String.format("applicationUserRepository: %s, %s ",
//                                user.getName(),
//                                user.getClass().getCanonicalName()));
        };

    }    

}
