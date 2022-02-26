package org.dbs24.proxy.core;

import org.dbs24.spring.boot.api.AbstractSpringBootApplication;
import static org.dbs24.spring.boot.api.AbstractSpringBootApplication.EMPTY_INITIALIZATION;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import static org.dbs24.proxy.core.consts.ProxyConsts.PkgConsts.PROXY_PACKAGE_REPO;

@SpringBootApplication
@EnableCaching
@EnableJpaRepositories(basePackages = {PROXY_PACKAGE_REPO})
public class ProxyCoreApplication extends AbstractSpringBootApplication {

    public static void main(String[] args) {
        AbstractSpringBootApplication.runSpringBootApplication(args,
                ProxyCoreApplication.class,
                EMPTY_INITIALIZATION);
    }
}
