/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import org.dbs24.spring.unit.SpringBoot4Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
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
@EntityScan(basePackages = {"org.dbs24.entity.counterparties.api", SysConst.ENTITY_PACKAGE})
@EnableJpaRepositories(basePackages = SysConst.REPOSITORY_PACKAGE)
public class CounterpartiesBootTest extends SpringBoot4Test {

}
