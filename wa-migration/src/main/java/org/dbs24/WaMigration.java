/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24;

import org.dbs24.spring.boot.api.AbstractSpringBootApplication;
import static org.dbs24.consts.SysConst.REPOSITORY_PACKAGE;
import static org.dbs24.spring.boot.api.AbstractSpringBootApplication.EMPTY_INITIALIZATION;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(REPOSITORY_PACKAGE)
@EnableCaching
public class WaMigration extends AbstractSpringBootApplication {

    public static void main(String[] args) {
        AbstractSpringBootApplication.runSpringBootApplication(args,
                WaMigration.class,
                EMPTY_INITIALIZATION);
    }
}
