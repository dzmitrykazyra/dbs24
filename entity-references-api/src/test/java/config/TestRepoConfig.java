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
import org.dbs24.config.MainApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import static org.dbs24.consts.SysConst.*;

@Configuration
//@ComponentScan(basePackages = SERVICE_PACKAGE)
//@EntityScan(basePackages = {SECURITY_PACKAGE, ENTITY_PACKAGE})
@PropertySource(APP_PROPERTIES)
//@EnableJpaRepositories(basePackages = REPOSITORY_PACKAGE)
@Data
@Import({EntityReferencesConfig.class})
public class TestRepoConfig extends MainApplicationConfig {
    
}
