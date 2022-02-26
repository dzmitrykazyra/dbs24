/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.proxy.core.importer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.proxy.core.component.ProxyService;
import org.dbs24.proxy.core.component.ReferenceService;
import org.dbs24.proxy.core.entity.domain.Proxy;
import org.dbs24.proxy.core.entity.dto.refapi.HostListDto;
import org.dbs24.proxy.core.entity.dto.refapi.HostToIsoMap;
import org.dbs24.proxy.core.imm.grpc.InfoMessage;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.net.*;
import java.io.*;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import static org.dbs24.proxy.core.imm.grpc.InfoMessage.ConnectedProxyInfo;
import static org.dbs24.proxy.core.consts.ProxyConsts.ProxyProviderEnum.PP_ZETA;
import static org.dbs24.proxy.core.consts.ProxyConsts.ProxyStatusEnum.PS_EXPRIRED;
import com.google.protobuf.ByteString;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import static org.dbs24.proxy.core.consts.ProxyConsts.ProxyStatusEnum.PS_ACTUAL;
import static org.dbs24.proxy.core.consts.ProxyConsts.ProxyTypeEnum.PT_OWN_MOBILE;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Data
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "proxy-core")
public class ZetaProxyImporter extends AbstractApplicationService {

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${config.providers.imm.import.host:207.246.81.21}")
    private String host;

    @Value("${config.providers.imm.import.port:1500}")
    private Integer port;

    @Value("${refs.api.endpoint.hosts-to-countries}")
    private String hostsToCountriesEndpointUrl;

    final ReferenceService referenceService;
    private final ProxyService proxyService;

    @Autowired
    public ZetaProxyImporter(ReferenceService referenceService, ProxyService proxyService) {
        this.referenceService = referenceService;
        this.proxyService = proxyService;
    }

    @Scheduled(fixedRateString = "${config.providers.imm.import.refresh:10000}")
    private void doImport() {

        StmtProcessor.execute(() -> {

            try (Socket clientSocket = new Socket(host, port);
                 InputStream inputStream = clientSocket.getInputStream()) {

                final byte[] data = inputStream.readAllBytes();
                log.debug("inputStream.readAllBytes() = {} byte(s)", data.length);

                final ConnectedProxyInfo connectedProxyInfo = ConnectedProxyInfo.parseFrom(data);

                handleReceivedProxies(connectedProxyInfo);

            } catch (WebClientRequestException e) {
                log.error("Cannot connect to immediate proxy provider");
            }
        });
    }

    private void handleReceivedProxies(ConnectedProxyInfo connectedProxyInfo) {

        final Collection<Proxy> actualProxies = proxyService.findActualProxies(PP_ZETA);
        final Collection<Proxy> expriredProxies = ServiceFuncs.createCollection();
        final List<Proxy> newProxies = new ArrayList<>(connectedProxyInfo.getInfosList().size());

        log.debug("{}: There are {} actual proxies", PP_ZETA, actualProxies.size());

        // current list
        actualProxies.forEach(proxy -> {

            if (connectedProxyInfo
                    .getInfosList()
                    .stream()
                    .noneMatch(newProxy -> getRealIp(newProxy.getIp()).equals(proxy.getExternalIpAddress()) && proxy.getSocksPort().equals(newProxy.getPort()))) {

                // markAsExpired
                expriredProxies.add(proxy);
                setExpired(proxy);

            }
        });

        // new list
        connectedProxyInfo
                .getInfosList()
                .forEach(newProxy -> {
                    if (actualProxies.stream().noneMatch(proxy -> getRealIp(newProxy.getIp()).equals(proxy.getExternalIpAddress()) && proxy.getSocksPort().equals(newProxy.getPort()))) {
                        newProxies.add(createFreshProxyEntity(newProxy));
                    }
                });

        // save exprired
        getProxyService().getProxyDao().batchSaveProxies(expriredProxies);

        setCountriesToProxies(newProxies);


        getProxyService().getProxyDao().batchSaveProxies(newProxies);
    }


    /**
     * Set got from DAO Proxy entity fields expired values
     *
     * @param proxy expired Proxy
     */
    private void setExpired(Proxy proxy) {

        proxy.setProxyStatus(getReferenceService().findProxyStatus(PS_EXPRIRED));
        proxy.setDateEnd(LocalDateTime.now());

        //log.debug("{}: expire proxy ({}:{}, {})", PP_ZETA, proxy.getExternalIpAddress(), proxy.getSocksPort(), NLS.d1d2Diff(ChronoUnit.MILLIS.between(proxy.getDateBegin(), LocalDateTime.now())));
    }

    /**
     * Create new proxy entity with null id to DAO save based on received
     * protobuf data
     *
     * @param proxyInfo received protobuf object
     * @return new Proxy entity
     */
    private Proxy createFreshProxyEntity(InfoMessage.ProxyInfo proxyInfo) {

        final String externalIpAddress = getRealIp(proxyInfo.getIp());
        final Integer socksPort = proxyInfo.getPort();

        return StmtProcessor.create(
                Proxy.class, proxy -> {
                    proxy.setProxyId(null);
                    proxy.setActualDate(LocalDateTime.now());
                    proxy.setProxyProvider(getReferenceService().findProxyProvider(PP_ZETA.getId()));
                    proxy.setUrl(host);
                    proxy.setSocksPort(socksPort);
                    proxy.setPort(null);
                    proxy.setLogin(null);
                    proxy.setPass(null);
                    proxy.setUrlIpChange(null);
                    proxy.setCountry(null);
                    proxy.setExternalIpAddress(externalIpAddress);
                    proxy.setProxyStatus(getReferenceService().findProxyStatus(PS_ACTUAL));
                    proxy.setProxyType(getReferenceService().findProxyType(PT_OWN_MOBILE.getCode()));
                    proxy.setDateBegin(LocalDateTime.now());
                    proxy.setDateEnd(null);

                    if (proxyInfo.getMethod() != InfoMessage.SocksAuthMethods.UNRECOGNIZED) {
                        proxy.setSocksAuthMethod(null);
                    } else {
                        proxy.setSocksAuthMethod(getReferenceService().findSocksAuthMethodById(proxyInfo.getMethod().getNumber()));
                    }

                    proxy.setSocksClientData(proxyInfo.getClientData().toByteArray());
                    proxy.setTraffic(null);

                    //log.debug("{}: new proxy: {}:{} ", PP_ZETA, host, socksPort);
                });
    }

    private void setCountriesToProxies(List<Proxy> proxies) {

        referenceService.refreshCountriesRepo();

        final Mono<HostListDto> countryDtoMono = Mono.just(
                StmtProcessor.create(
                        HostListDto.class,
                        hostListDto -> hostListDto.setHostList(proxies
                                .stream()
                                .map(Proxy::getExternalIpAddress)
                                .collect(Collectors.toList()))
                ));

        HostToIsoMap hostToIsoMap = referenceService.getWebClient().post()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(hostsToCountriesEndpointUrl)
                        .build())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(countryDtoMono, HostListDto.class)
                .retrieve()
                .bodyToMono(HostToIsoMap.class)
                .toProcessor()
                .block();

        log.info("MAP SIZE {}", hostToIsoMap.getHostToCountry().size());

        setCountries(hostToIsoMap.getHostToCountry(), proxies);
    }

    private void setCountries(Map<String, String> hostToCountryIso, List<Proxy> proxies) {

        proxies.forEach(
                proxy -> proxy.setCountry(
                            referenceService.findCountryByIso(
                                    hostToCountryIso.get(
                                            proxy.getExternalIpAddress()
                                    ))
                    )
        );
    }

    private String getRealIp(ByteString byteString) {

        final StringBuilder ipBuilder = new StringBuilder(32);
        byteString.forEach(b -> {

            ipBuilder.append(".");
            ipBuilder.append(b & 0xff);
        });

        return ipBuilder.toString().substring(1);
    }
}