/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg;

import static org.dbs24.consts.SysConst.ALL_PACKAGES;
import static org.dbs24.consts.SysConst.REPOSITORY_PACKAGE;
import org.dbs24.spring.boot.api.AbstractSpringBootApplication;
import static org.dbs24.spring.boot.api.AbstractSpringBootApplication.EMPTY_INITIALIZATION;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableCaching
@EnableJpaRepositories(basePackages = {REPOSITORY_PACKAGE, "org.dbs24.insta.reg.repo"})
public class InstaRegistry extends AbstractSpringBootApplication {

    public static void main(String[] args) {
        AbstractSpringBootApplication.runSpringBootApplication(args,
                InstaRegistry.class,
                EMPTY_INITIALIZATION);
    }
}
