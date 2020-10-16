package org.dbs24;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.dbs24.application.core.sysconst.SysConst;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.dbs24.spring.boot.api.AbstractSpringBootApplication;

//import org.springframework.boot.actuate.autoconfigure.security.ManagementWebSecurityAutoConfiguration;

/**
 *
 * @author Козыро Дмитрий
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
@PropertySource(SysConst.APP_PROPERTIES)
@EnableJpaRepositories(basePackages = SysConst.REPOSITORY_PACKAGE)
public final class RetailLoanContractBoot extends AbstractSpringBootApplication {

    public static void main(String[] args) {
        AbstractSpringBootApplication.runSpringBootApplication(args,
                RetailLoanContractBoot.class,
                AbstractSpringBootApplication.EMPTY_INITIALIZATION);
    }
}
