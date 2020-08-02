/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

/**
 *
 * @author Козыро Дмитрий
 */
import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.dbs24.config.*;
import org.dbs24.repository.EntityTypesRepository;
import org.dbs24.spring.config.MainApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.dbs24.application.core.sysconst.SysConst;

@Configuration
//@ComponentScan(basePackages = SysConst.SERVICE_PACKAGE)
//@EntityScan(basePackages = {SysConst.SECURITY_PACKAGE, SysConst.ENTITY_PACKAGE})
@PropertySource(SysConst.APP_PROPERTIES)
//@EnableJpaRepositories(basePackages = SysConst.REPOSITORY_PACKAGE)
@Data
@Import({EntityReferencesConfig.class})
public class TestRepoConfig extends MainApplicationConfig {
    
}
