/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24;

import lombok.extern.log4j.Log4j2;
import static org.dbs24.consts.SysConst.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.dbs24.spring.boot.api.AbstractSpringBootApplication;
import static org.dbs24.spring.boot.api.AbstractSpringBootApplication.EMPTY_INITIALIZATION;

@Log4j2
@SpringBootApplication
//@PropertySource(APP_PROPERTIES)
//@Import({
//    ServiceMonitoringConfig.class,
//    StandardMonitoringRSocketConfig.class})
public class ServiceMonitoringBoot extends AbstractSpringBootApplication {
    
    public static void main(String[] args) {
        AbstractSpringBootApplication.runSpringBootApplication(args,
                ServiceMonitoringBoot.class,
                EMPTY_INITIALIZATION);
    }

//    @Override
//    protected SpringApplicationBuilder configure(
//            SpringApplicationBuilder builder) {
//        return builder.sources(ServiceMonitoringBoot.class);
//    } 
//    @Bean
//    CommandLineRunner initUsers(UserRepository repo) {
//
//        return args -> {
//            for (int i = 0; i < users.length; i++) {
//                String email = users[i] + "@" + users[i] + ".com";
//                User.Role role = i > 1 ? User.Role.USER : i == 0 ? User.Role.ADMIN : User.Role.USER_MANAGER;
//                double minGleePerDay = rnd(1000 * feelings.length);
//                String pwd = passwordEncoder.encode("pwd");
//                log.info("save {}", repo.save(new User(null, email, pwd, role, minGleePerDay, null)));
//            }
//        };
//    }    
}
