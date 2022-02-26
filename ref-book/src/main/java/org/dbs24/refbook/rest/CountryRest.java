package org.dbs24.refbook.rest;

import lombok.extern.log4j.Log4j2;
import org.dbs24.refbook.component.CountryService;
import org.dbs24.refbook.config.RefBookRestConfig;
import org.dbs24.refbook.entity.dto.*;
import org.dbs24.rest.api.ReactiveRestProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class CountryRest extends ReactiveRestProcessor {

    private final CountryService countryService;

    public CountryRest(CountryService countryService) {
        this.countryService = countryService;
    }

    public Mono<ServerResponse> createCountry(ServerRequest request) {

        return this.<CountryDto, CreatedCountryDto>processServerRequest(
                request,
                CountryDto.class,
                countryService::createCountry
        );
    }

    public Mono<ServerResponse> createCountries(ServerRequest request) {

        return this.<CountryListDto, CreatedCountryListDto>processServerRequest(
                request,
                CountryListDto.class,
                countryService::createCountries
        );
    }

    public Mono<ServerResponse> getAllCountries(ServerRequest request) {

        return this.<CountryListDto>processServerRequest(
                request,
                countryService::getAllCountries);

    }

    public Mono<ServerResponse> getCountry(ServerRequest request) {

        return this.<CountryDto>processServerRequest(
                request,
                () -> countryService.getCountry(getOptionalStringFromParam(request, RefBookRestConfig.QP_COUNTRY_ISO)));
    }

    public Mono<ServerResponse> getHostToCountryMap(ServerRequest request) {

        return this.<HostListDto, HostToIsoMap>processServerRequest(
                request,
                HostListDto.class,
                countryService::getHostToCountryByHostList
        );
    }
}
