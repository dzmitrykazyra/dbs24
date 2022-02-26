package org.dbs24.email.spammer;

import org.dbs24.spring.boot.api.AbstractSpringBootApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("org.dbs24.email.spammer.repo")
@EnableCaching
public class EmailSpammer extends AbstractSpringBootApplication {

    public static void main(String[] args) {
        AbstractSpringBootApplication.runSpringBootApplication(
                args,
                EmailSpammer.class,
                EMPTY_INITIALIZATION);
    }
}
