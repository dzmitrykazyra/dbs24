package org.dbs24.refbook.config;

import lombok.extern.log4j.Log4j2;
import org.dbs24.config.AbstractWebSecurityConfig;
import org.dbs24.consts.RestHttpConsts;
import org.dbs24.refbook.rest.CountryRest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Log4j2
@Configuration
public class RefBookRestConfig extends AbstractWebSecurityConfig {

    public final static String QP_COUNTRY_ISO = "countryIso";

    private static final String URI_API = RestHttpConsts.URI_API;

    public static final String URI_CREATE_COUNTRY = URI_API + "/createCountry";
    public static final String URI_CREATE_COUNTRIES = URI_API + "/createCountries";
    public static final String URI_GET_COUNTRY_BY_ISO = URI_API + "/getCountry";
    public static final String URI_GET_COUNTRIES = URI_API + "/getCountries";
    public static final String URI_GET_HOST_TO_COUNTRY_MAP = URI_API + "/getHostToCountryMap";

    private final CountryRest countryRest;

    public RefBookRestConfig(CountryRest countryRest) {
        this.countryRest = countryRest;
    }

    @Bean
    public RouterFunction<ServerResponse> routerInstaRegRest() {

        final RouterFunction<ServerResponse> mainRoutes = addCommonRoutes()
                .andRoute(postRoute(URI_CREATE_COUNTRY), countryRest::createCountry)
                .andRoute(postRoute(URI_CREATE_COUNTRIES), countryRest::createCountries)
                .andRoute(getRoute(URI_GET_COUNTRY_BY_ISO), countryRest::getCountry)
                .andRoute(getRoute(URI_GET_COUNTRIES), countryRest::getAllCountries)
                .andRoute(postRoute(URI_GET_HOST_TO_COUNTRY_MAP), countryRest::getHostToCountryMap);

        return mainRoutes;
    }
}
