package org.dbs24.tik.mobile;

import org.dbs24.spring.boot.api.AbstractSpringBootApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableCaching
@EnableJpaRepositories(basePackages = {"org.dbs24.tik.mobile.repo"})
public class TikMobile extends AbstractSpringBootApplication {

    public static void main(String[] args) {
        AbstractSpringBootApplication.runSpringBootApplication(args,
                TikMobile.class,
                EMPTY_INITIALIZATION);

    }
}
