/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.auth.server;

import org.dbs24.spring.boot.api.AbstractSpringBootApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static org.dbs24.auth.server.consts.AuthConsts.Packages.REPOSITORY_PACKAGE;

@SpringBootApplication
@EnableCaching
@EnableJpaRepositories(basePackages = {REPOSITORY_PACKAGE})
public class AuthServer extends AbstractSpringBootApplication {

    public static void main(String[] args) {
        AbstractSpringBootApplication.runSpringBootApplication(args,
                AuthServer.class,
                EMPTY_INITIALIZATION);
    }
}
