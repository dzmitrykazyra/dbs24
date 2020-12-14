/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import org.dbs24.config.CounterpartyConfig;
import org.dbs24.config.EntityReferencesConfig;
import org.springframework.context.annotation.ComponentScan;
import org.dbs24.config.MainApplicationConfig;
import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import static org.dbs24.consts.SysConst.*;


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
@Import({CounterpartyConfig.class, EntityReferencesConfig.class})
public class CounterpartiesTestConfig extends MainApplicationConfig {

}
