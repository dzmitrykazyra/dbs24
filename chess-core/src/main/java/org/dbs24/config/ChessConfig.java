/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

import static org.dbs24.consts.SysConst.*;
import org.dbs24.config.MainApplicationConfig;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(basePackages = {SERVICE_PACKAGE, RESTFUL_PACKAGE, COMPONENT_PACKAGE})
@EntityScan(basePackages = {ENTITY_PACKAGE, REFERENCE_PACKAGE})
@PropertySource(APP_PROPERTIES)
public class ChessConfig extends MainApplicationConfig {
    
}
    
