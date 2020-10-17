/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import org.dbs24.spring.unit.SpringBoot4Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import static org.dbs24.application.core.sysconst.SysConst.*;
import org.dbs24.config.RetailLoanContractCommonConfig;
import org.dbs24.config.RetailLoanContractWebSecurityConfig;
import org.dbs24.config.SecurityConfig;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;


/**
 *
 * @author Козыро Дмитрий
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
@PropertySource(APP_PROPERTIES)
@EnableJpaRepositories(basePackages = REPOSITORY_PACKAGE)
@Import({RetailLoanContractCommonConfig.class,
    RetailLoanContractWebSecurityConfig.class,
    SecurityConfig.class})
public class TestSpringBootApp extends SpringBoot4Test {

//    public static void main(String[] args) {
//       //SpringApplication.run(Application.class, args);
//       AbstractSpringBootApplication.runSpringBootApplication(args, TestSpringBootApp.class);
//
//        GreetingWebClient gwc = new GreetingWebClient();
//        System.out.println(gwc.getResult());
//    }
}
