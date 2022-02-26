package org.dbs24.refbook;

import org.dbs24.spring.boot.api.AbstractSpringBootApplication;
import static org.dbs24.spring.boot.api.AbstractSpringBootApplication.EMPTY_INITIALIZATION;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("org.dbs24.refbook.repo")
@EnableCaching
public class RefBookApplication extends AbstractSpringBootApplication {

    public static void main(String[] args) {
        AbstractSpringBootApplication.runSpringBootApplication(args,
                RefBookApplication.class,
                EMPTY_INITIALIZATION);
    }
}
