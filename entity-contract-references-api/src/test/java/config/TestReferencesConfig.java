/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import org.dbs24.spring.config.MainApplicationConfig;
import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.dbs24.service.EntityContractReferencesService;
import org.dbs24.application.core.sysconst.SysConst;

/**
 *
 * @author Козыро Дмитрий
 */
@Configuration
//@ComponentScan(basePackages = SysConst.SERVICE_PACKAGE)
//@EntityScan(basePackages = {SysConst.SECURITY_PACKAGE, SysConst.ENTITY_PACKAGE})
@PropertySource(SysConst.APP_PROPERTIES)
//@EnableJpaRepositories(basePackages = SysConst.REPOSITORY_PACKAGE)
@Data
@Import({EntityContractReferencesService.class})
public class TestReferencesConfig extends MainApplicationConfig {

}
