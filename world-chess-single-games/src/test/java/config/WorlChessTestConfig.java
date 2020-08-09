/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import org.dbs24.config.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import org.dbs24.service.WorldChessActionExecutionService;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.dbs24.application.core.sysconst.SysConst;
import org.springframework.context.annotation.Bean;
/**
 *
 * @author Козыро Дмитрий
 */
@Configuration
@ComponentScan(basePackages = {SysConst.SERVICE_PACKAGE, SysConst.RESTFUL_PACKAGE})
@EntityScan(basePackages = {SysConst.ENTITY_PACKAGE, SysConst.REFERENCE_PACKAGE})
@PropertySource(SysConst.APP_PROPERTIES)
//@EnableJpaRepositories(basePackages = SysConst.REPOSITORY_PACKAGE)
@Import({WorldChessActionExecutionService.class})
public class WorlChessTestConfig extends WorldChessConfig{
    
}
