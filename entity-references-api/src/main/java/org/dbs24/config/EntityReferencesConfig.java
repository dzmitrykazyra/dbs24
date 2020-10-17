/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

import org.dbs24.application.core.nullsafe.NullSafe;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.dbs24.repository.*;
import org.springframework.context.annotation.Bean;
import org.dbs24.spring.config.AbstractApplicationConfiguration;
import static org.dbs24.application.core.sysconst.SysConst.*;

/**
 *
 * @author Козыро Дмитрий
 */
@Configuration
@ComponentScan(basePackages = SERVICE_PACKAGE)
//@EntityScan(basePackages = {SECURITY_PACKAGE, ENTITY_PACKAGE})
@PropertySource(APP_PROPERTIES)
//@EnableJpaRepositories(basePackages = REPOSITORY_PACKAGE)
@Data
public class EntityReferencesConfig extends AbstractApplicationConfiguration {


}
