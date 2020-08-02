/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import org.dbs24.spring.unit.SpringBoot4Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.dbs24.application.core.sysconst.SysConst;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.dbs24.spring.boot.api.AbstractSpringBootApplication;
//import org.springframework.boot.actuate.autoconfigure.security.ManagementWebSecurityAutoConfiguration;

/**
 *
 * @author Козыро Дмитрий
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
@PropertySource(SysConst.APP_PROPERTIES)
@EnableJpaRepositories(basePackages = SysConst.REPOSITORY_PACKAGE)
public class TestSpringBootApp extends SpringBoot4Test {

//    public static void main(String[] args) {
//       //SpringApplication.run(Application.class, args);
//       AbstractSpringBootApplication.runSpringBootApplication(args, TestSpringBootApp.class);
//
//        GreetingWebClient gwc = new GreetingWebClient();
//        System.out.println(gwc.getResult());
//    }
}
