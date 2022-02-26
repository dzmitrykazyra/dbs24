package org.dbs24.google.config;

import lombok.extern.log4j.Log4j2;
import org.dbs24.config.MainApplicationConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Log4j2
@Configuration
public class GoogleConnectionConfig extends MainApplicationConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }
}