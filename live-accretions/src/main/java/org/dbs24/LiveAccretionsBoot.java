/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24;

import lombok.extern.log4j.Log4j2;
import static org.dbs24.consts.SysConst.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.dbs24.spring.boot.api.AbstractSpringBootApplication;
import org.springframework.context.annotation.Import;
import org.dbs24.config.*;

@SpringBootApplication
//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
@PropertySource(APP_PROPERTIES)
@EnableJpaRepositories(REPOSITORY_PACKAGE)
@Import({
    LiveAccretionRestConfig.class,
    LiveAccretionConfig.class,
    SecurityConfig.class,
    LiveAccretionWebSecurityConfig.class})
public class LiveAccretionsBoot extends AbstractSpringBootApplication {

    public static void main(String[] args) {
        AbstractSpringBootApplication.runSpringBootApplication(args,
                LiveAccretionsBoot.class,
                EMPTY_INITIALIZATION);
    }
}
