/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24;

import static org.dbs24.consts.SysConst.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.dbs24.spring.boot.api.AbstractSpringBootApplication;
import org.springframework.context.annotation.Import;
import org.dbs24.config.*;
import static org.dbs24.spring.boot.api.AbstractSpringBootApplication.EMPTY_INITIALIZATION;

@SpringBootApplication
@PropertySource(APP_PROPERTIES)
@Import({
    ServiceMonitoringConfig.class,
    ServicesMonitoringRSocketConfig.class})
public class ServiceMonitoringBoot extends AbstractSpringBootApplication {

    public static void main(String[] args) {
        AbstractSpringBootApplication.runSpringBootApplication(args,
                ServiceMonitoringBoot.class,
                EMPTY_INITIALIZATION);
    }
}
