/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import com.kdg.fs24.config.CounterpartyConfig;
import com.kdg.fs24.config.EntityReferencesConfig;
import org.springframework.context.annotation.ComponentScan;
import com.kdg.fs24.spring.config.MainApplicationConfig;
import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import com.kdg.fs24.application.core.sysconst.SysConst;


/**
 *
 * @author Козыро Дмитрий
 */
@Configuration
@ComponentScan(basePackages = SysConst.SERVICE_PACKAGE)
//@EntityScan(basePackages = {SysConst.SECURITY_PACKAGE, SysConst.ENTITY_PACKAGE})
@PropertySource(SysConst.APP_PROPERTIES)
//@EnableJpaRepositories(basePackages = SysConst.REPOSITORY_PACKAGE)
@Data
@Import({CounterpartyConfig.class, EntityReferencesConfig.class})
public class CounterpartiesTestConfig extends MainApplicationConfig {

}
