/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

import org.dbs24.application.core.sysconst.SysConst;
import static org.dbs24.consts.WorldChessConst.URI_CREATE_CHESS_PLAYER;
import static org.dbs24.consts.WorldChessConst.URI_CREATE_CHESS_GAME;
import static org.dbs24.consts.WorldChessConst.URI_FIND_CHESS_PLAYER;
import static org.dbs24.consts.WorldChessConst.URI_EXECUTE_ACTION;
//import static org.dbs24.entity.core.api.EntityContractConst.URI_EXECUTE_ACTION;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.dbs24.config.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import org.springframework.web.reactive.function.server.RouterFunction;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.dbs24.config.*;
/**
 *
 * @author N76VB
 */
@Configuration
@ComponentScan(basePackages = {SysConst.SERVICE_PACKAGE, SysConst.RESTFUL_PACKAGE})
//@EntityScan(basePackages = {SysConst.ENTITY_PACKAGE, SysConst.REFERENCE_PACKAGE})
@PropertySource(SysConst.APP_PROPERTIES)
//@EnableWebFlux
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WorldChessSecurityConfig extends AbstractWebSecurityConfig {
    
}
