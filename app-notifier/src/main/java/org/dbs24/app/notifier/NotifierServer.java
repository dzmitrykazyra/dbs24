/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.notifier;

import org.dbs24.spring.boot.api.AbstractSpringBootApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static org.dbs24.app.notifier.consts.AppNotifierConsts.Packages.REPOSITORY_PACKAGE;

@SpringBootApplication
@EnableCaching
@EnableJpaRepositories(basePackages = {REPOSITORY_PACKAGE})
public class NotifierServer extends AbstractSpringBootApplication {

    public static void main(String[] args) {
        AbstractSpringBootApplication.runSpringBootApplication(args,
                NotifierServer.class,
                EMPTY_INITIALIZATION);
    }
}
