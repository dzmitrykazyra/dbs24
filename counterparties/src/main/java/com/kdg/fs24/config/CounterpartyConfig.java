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

/**
 *
 * @author Козыро Дмитрий
 */
@Configuration
@ComponentScan(basePackages = SysConst.SERVICE_PACKAGE)
@PropertySource(SysConst.APP_PROPERTIES)
@Data
public class CounterpartyConfig extends AbstractApplicationConfiguration {
}
