/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.dbs24.config.AbstractApplicationConfiguration;
import static org.dbs24.consts.SysConst.*;

@Configuration
@ComponentScan(basePackages = {COMPONENT_PACKAGE, SERVICE_PACKAGE})
//@PropertySource(APP_PROPERTIES)
@Data
public class EntityReferencesConfig extends AbstractApplicationConfiguration {


}
