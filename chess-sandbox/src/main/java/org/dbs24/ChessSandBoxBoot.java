/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24;

import static org.dbs24.consts.SysConst.*;
import org.dbs24.config.SecurityConfig;
import org.dbs24.config.ChessConfig;
import org.dbs24.config.ChessCoreRestConfig;
import org.dbs24.spring.boot.api.AbstractSpringBootApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
@PropertySource(APP_PROPERTIES)
@EnableJpaRepositories(basePackages = REPOSITORY_PACKAGE)
@Import({
    ChessCoreRestConfig.class,
    ChessConfig.class,
    SecurityConfig.class})
public class ChessSandBoxBoot extends AbstractSpringBootApplication {

    public static void main(String[] args) {
        AbstractSpringBootApplication.runSpringBootApplication(args, ChessSandBoxBoot.class, EMPTY_INITIALIZATION);
    }
}
