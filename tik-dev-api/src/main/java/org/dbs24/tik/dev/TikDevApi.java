/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev;

import org.dbs24.spring.boot.api.AbstractSpringBootApplication;
import org.dbs24.tik.dev.consts.PackageName;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableCaching
@EnableJpaRepositories(basePackages = {PackageName.TIK_PACKAGE_REPO})
public class TikDevApi extends AbstractSpringBootApplication {

    public static void main(String[] args) {
        AbstractSpringBootApplication.runSpringBootApplication(args,
                TikDevApi.class,
                EMPTY_INITIALIZATION);
    }
}
