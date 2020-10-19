package org.dbs24;

import static org.dbs24.application.core.sysconst.SysConst.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.dbs24.spring.boot.api.AbstractSpringBootApplication;
import org.springframework.context.annotation.Import;
import org.dbs24.config.*;

/**
 *
 * @author Козыро Дмитрий
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
@PropertySource(APP_PROPERTIES)
@EnableJpaRepositories(REPOSITORY_PACKAGE)
@Import({RetailLoanContractCommonConfig.class,
    RetailLoanContractWebSecurityConfig.class})
    //SecurityConfig.class})
public class RetailLoanContractBoot extends AbstractSpringBootApplication {

    public static void main(String[] args) {
        AbstractSpringBootApplication.runSpringBootApplication(args,
                RetailLoanContractBoot.class,
                AbstractSpringBootApplication.EMPTY_INITIALIZATION);
    }
}
