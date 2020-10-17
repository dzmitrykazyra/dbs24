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
import static org.dbs24.application.core.sysconst.SysConst.*;

/**
 *
 * @author Козыро Дмитрий
 */
@SpringBootApplication
@ComponentScan(basePackages = SERVICE_PACKAGE)
@PropertySource(APP_PROPERTIES)
@EntityScan(basePackages = {ENTITY_PACKAGE})
@EnableJpaRepositories(basePackages = REPOSITORY_PACKAGE)
public class TestSpringBoot extends SpringBoot4Test {
    
}
