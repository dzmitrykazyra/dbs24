/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

import org.dbs24.spring.config.AbstractApplicationConfiguration;
import lombok.Data;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import static org.dbs24.application.core.sysconst.SysConst.*;
import org.dbs24.service.*;
import org.springframework.context.annotation.Bean;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author Козыро Дмитрий
 */
@Deprecated
@Configuration
@ComponentScan(basePackages = SERVICE_PACKAGE)
//@EntityScan(basePackages = {SECURITY_PACKAGE, ENTITY_PACKAGE})
@PropertySource(APP_PROPERTIES)
//@EnableJpaRepositories(basePackages = REPOSITORY_PACKAGE)
@Data
public class TariffCoreConfig extends AbstractApplicationConfiguration {

    @Bean
    TariffStdRates tariffStrRates() {
        return NullSafe.createObject(TariffStdRates.class);
    }
}
