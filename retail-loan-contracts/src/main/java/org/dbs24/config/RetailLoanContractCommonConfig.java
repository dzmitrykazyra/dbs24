/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

import org.dbs24.application.core.log.LogService;
import org.dbs24.spring.config.AbstractApplicationConfiguration;
import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.dbs24.application.core.sysconst.SysConst;
import org.dbs24.application.core.nullsafe.NullSafe;
import org.dbs24.spring.config.MainApplicationConfig;
import org.dbs24.service.RetailLoanContractActionsService;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.dbs24.application.core.sysconst.SysConst;
import org.dbs24.entity.core.api.EntityContractConst;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.context.annotation.Profile;
import org.dbs24.rest.*;
import org.dbs24.repository.LoanContractRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.annotation.Order;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Козыро Дмитрий
 */
@Configuration
@ComponentScan(basePackages = {SysConst.SERVICE_PACKAGE, SysConst.RESTFUL_PACKAGE})
@EntityScan(basePackages = {SysConst.ENTITY_PACKAGE, SysConst.REFERENCE_PACKAGE})
@PropertySource(SysConst.APP_PROPERTIES)
//@Import({RetailLoanContractActionsService.class})
//@EnableJpaRepositories(basePackages = SysConst.REPOSITORY_PACKAGE)
@Data
public class RetailLoanContractCommonConfig extends MainApplicationConfig {

}
