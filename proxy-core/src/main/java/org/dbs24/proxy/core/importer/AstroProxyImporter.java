/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.core.importer;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.component.ProxyService;
import org.dbs24.proxy.core.component.ReferenceService;
import org.dbs24.proxy.core.entity.domain.Proxy;
import org.dbs24.proxy.core.rest.importer.AstroPorts;
import org.dbs24.proxy.core.rest.importer.Countries;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.dbs24.proxy.core.consts.ProxyConsts.ProxyProviderEnum.PP_ASTRO;
import static org.dbs24.proxy.core.consts.ProxyConsts.ProxyStatusEnum.*;
import static org.dbs24.proxy.core.consts.ProxyConsts.ProxyTypeEnum.PT_MOBILE;
import static org.dbs24.proxy.core.rest.api.consts.ProxyHttpConsts.RestQueryParams.QP_TOKEN;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Data
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "proxysss-core")
public class AstroProxyImporter extends AbstractApplicationService {

    @Value("${config.providers.astro.import.token:1fb142e5c6b18088}")
    private String token;

    @Value("${config.providers.astro.import.uri.base:https://astroproxy.com}")
    private String uriBase;

    @Value("${config.providers.astro.import.uri.ports:/api/v1/ports}")
    private String uriGetPorts;

    @Value("${config.providers.astro.import.uri.countries:/api/v1/countries}")
    private String uriGetCountries;

    @Value("${config.providers.astro.trafic.out:100000}")
    private Integer outOfTrafic;

    private final ReferenceService referenceService;
    private final ProxyService proxyService;

//    @Scheduled(fixedRateString = "${config.providers.astro.import.refresh:60000}")
    private void doImport() {

        validateCountriesRef();
        synchronizePorts();
    }

    //==========================================================================
    private void validateCountriesRef() {
        getWebClient()
                .get()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(uriGetCountries)
                        .queryParam(QP_TOKEN, token)
                        .build())
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(httpStatus -> HttpStatus.NOT_FOUND.equals(httpStatus), clientResponse -> Mono.empty())
                .bodyToMono(Countries.class)
                .subscribe(countries -> countries.getData().forEach(country -> {

            if (getReferenceService().findCountryOptional(country.getName()).isEmpty()) {
                log.error("Unknow or new country detected - {}/{}", country.getIso2(), country.getName());
            }
        }), throwable -> log.error("throwable = ", throwable));
    }

    //==========================================================================
    private void synchronizePorts() {
        getWebClient()
                .get()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(uriGetPorts)
                        .queryParam(QP_TOKEN, token)
                        .build())
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.empty())
                .bodyToMono(AstroPorts.class)
                .subscribe(
                        astroports -> {

                            // validate existing ports
                            getProxyService()
                                    .findActualProxies(PP_ASTRO)
                                    .forEach(proxy -> {

                                        // proxy removed
                                        if (astroports.getData().getPorts().stream().filter(port -> port.getNode().getAddress().equals(proxy.getUrl())).count() == 0) {
                                            proxy.setProxyStatus(getReferenceService().findProxyStatus(PS_EXPRIRED));
                                            proxy.setDateEnd(LocalDateTime.now());
                                            getProxyService().getProxyDao().saveProxy(proxy);
                                        }
                                    });

                            // check new ports
                            astroports.getData().getPorts().forEach(port -> {
/**/
                                final String url = port.getNode().getAddress();
                                final Integer httpPort = port.getPorts().getHttp();

                                final Proxy proxy = getProxyService().findProxy(url, httpPort).orElseGet(getProxyService()::createProxy);

                                StmtProcessor.ifNull(proxy.getProxyId(), () -> {
                                    proxy.setUrl(url);
                                    proxy.setPort(httpPort);
                                    proxy.setDateBegin(LocalDateTime.now());

                                    log.debug("Create static proxy ({}): {} [{}] ", 2, url, port);
                                });

                                StmtProcessor.ifNotNull(port.getCountry(), country -> proxy.setCountry(getReferenceService().findCountry(country)), () -> proxy.setCountry(null));

                                proxy.setActualDate(LocalDateTime.now());
                                proxy.setProxyProvider(getReferenceService().findProxyProvider(1));
                                proxy.setProxyType(getReferenceService().findProxyType(PT_MOBILE.getCode()));
                                proxy.setLogin(port.getAccess().getLogin());
                                proxy.setPass(port.getAccess().getPassword());

                                proxy.setTraffic(port.getTraffic().getLeft().intValue());

                                proxy.setProxyStatus(getReferenceService().findProxyStatus(proxy.getTraffic() > outOfTrafic ? PS_ACTUAL : PS_OUT_OF_TRAFFIC));

                                proxy.setSocksPort(port.getPorts().getSocks());
                                proxy.setUrlIpChange(String.format("%s:%d/api/changeIP?apiToken=%s", url, httpPort, token));

                                getProxyService().getProxyDao().saveProxy(proxy);
                            });

                        }, throwable -> log.error("throwable = ", throwable));
    }

//==========================================================================
    @Override
    public WebClient getWebClient() {

        return Optional.ofNullable(super.getWebClient())
                .orElseGet(() -> {
                    StmtProcessor.assertNotNull(String.class,
                            uriBase,
                            "parameter ${config.providers.astro.import.uri}'");
                    setWebClient(WebClient.builder()
                            .baseUrl(uriBase)
                            .exchangeStrategies(ExchangeStrategies.builder()
                                    .codecs(configurer -> configurer
                                    .defaultCodecs()
                                    .maxInMemorySize(16 * 1024 * 1024))
                                    .build())
                            .build());
                    log.debug("webClient: succefull created ({})", uriBase);
                    return super.getWebClient();
                });
    }
}
