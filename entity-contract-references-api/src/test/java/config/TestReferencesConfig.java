/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import org.dbs24.config.MainApplicationConfig;
import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.dbs24.service.EntityContractReferencesService;
import static org.dbs24.consts.SysConst.*;

/**
 *
 * @author Козыро Дмитрий
 */
@Configuration
//@ComponentScan(basePackages = SERVICE_PACKAGE)
//@EntityScan(basePackages = {SECURITY_PACKAGE, ENTITY_PACKAGE})
@PropertySource(APP_PROPERTIES)
//@EnableJpaRepositories(basePackages = REPOSITORY_PACKAGE)
@Data
@Import({EntityContractReferencesService.class})
public class TestReferencesConfig extends MainApplicationConfig {

}
