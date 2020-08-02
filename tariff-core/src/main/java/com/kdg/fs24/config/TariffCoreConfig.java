/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.config;

import com.kdg.fs24.spring.config.AbstractApplicationConfiguration;
import lombok.Data;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import com.kdg.fs24.application.core.sysconst.SysConst;
import com.kdg.fs24.service.*;
import org.springframework.context.annotation.Bean;
import com.kdg.fs24.application.core.nullsafe.NullSafe;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author Козыро Дмитрий
 */
@Deprecated
@Configuration
@ComponentScan(basePackages = SysConst.SERVICE_PACKAGE)
//@EntityScan(basePackages = {SysConst.SECURITY_PACKAGE, SysConst.ENTITY_PACKAGE})
@PropertySource(SysConst.APP_PROPERTIES)
//@EnableJpaRepositories(basePackages = SysConst.REPOSITORY_PACKAGE)
@Data
public class TariffCoreConfig extends AbstractApplicationConfiguration {

    @Bean
    TariffStdRates tariffStrRates() {
        return NullSafe.createObject(TariffStdRates.class);
    }
}
