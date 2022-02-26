/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24;

import org.dbs24.spring.boot.api.AbstractSpringBootApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import reactor.tools.agent.ReactorDebugAgent;

import static org.dbs24.consts.SysConst.REPOSITORY_PACKAGE;
import static org.dbs24.consts.SysConst.REPOSITORY_PACKAGE_SHORT;

@SpringBootApplication
@EnableJpaRepositories({REPOSITORY_PACKAGE, REPOSITORY_PACKAGE_SHORT})
@EnableEurekaClient
@EnableCaching
public class WaMonitoring extends AbstractSpringBootApplication {

    public static void main(String[] args) {
        AbstractSpringBootApplication.runSpringBootApplication(args,
                WaMonitoring.class,
                ReactorDebugAgent::init);
    }
}
