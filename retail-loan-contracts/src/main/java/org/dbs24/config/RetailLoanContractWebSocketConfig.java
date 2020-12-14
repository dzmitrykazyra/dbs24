/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

import static org.dbs24.consts.SysConst.*;
import org.dbs24.websocket.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {SERVICE_PACKAGE, WEBSOCKET_PACKAGE, COMPONENT_PACKAGE})
@PropertySource(APP_PROPERTIES)
public class RetailLoanContractWebSocketConfig extends AbstractWebSocketConfig {

    @Bean
    public RetailLoanContractWebSocketHandler webSocketHandler() {
        return new RetailLoanContractWebSocketHandler();
    }
}
