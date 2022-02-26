/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.cloud.server;

import lombok.EqualsAndHashCode;
import org.dbs24.spring.boot.api.AbstractSpringBootApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
@EqualsAndHashCode(callSuper = false)
public class RegistryServer extends AbstractSpringBootApplication {

    public static void main(String[] args) {
        AbstractSpringBootApplication.runSpringBootApplication(args,
                RegistryServer.class,
                EMPTY_INITIALIZATION);
    }
}