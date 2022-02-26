/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.component;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.insta.reg.repo.*;
import org.dbs24.insta.reg.entity.*;
import static org.dbs24.insta.reg.consts.InstaConsts.ProxyStatuses.PS_ACTUAL;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.consts.SysConst;
import org.dbs24.service.JavaFakerService;
import java.util.Map.Entry;
import org.springframework.transaction.annotation.Transactional;

@Data
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-reg")
public class ProxyService extends AbstractApplicationService {

    final ProxyRepo proxyRepo;
    final RefsService refsService;
    final AccountService accountService;
    final JavaFakerService javaFakerService;

    @Value("${config.proxy.accept-header}")
    private String acceptHeader;

    @Value("${config.proxy.accept-language}")
    private String acceptLanguage;

    private String userAgent;

    private Map<Integer, ProxyClient> proxyClients = ServiceFuncs.createConcurencyMap();

    // statistics
    private Map<Integer, Integer> proxyFails = ServiceFuncs.createMap();
    final AtomicInteger totalFails = new AtomicInteger();
    final AtomicInteger lastSuccessfullProxy = new AtomicInteger();

//    @Value("${proxy.preferred:0}")
//    private Integer preferredProxyId;

    //==========================================================================
    public ProxyService(ProxyRepo proxyRepo, JavaFakerService javaFakerService, RefsService refsService, AccountService accountService) {
        this.proxyRepo = proxyRepo;
        this.javaFakerService = javaFakerService;
        this.refsService = refsService;
        this.accountService = accountService;

        totalFails.set(0);
        lastSuccessfullProxy.set(0);

    }

    public String getUserAgent() {
        StmtProcessor.ifNull(userAgent, () -> userAgent = javaFakerService.createUserAgent());
        return userAgent;
    }

    //==========================================================================
    @Cacheable("proxies")
    public Proxy findProxy(Integer proxyId) {

        return StmtProcessor.assertNotNull(Proxy.class,
                proxyRepo.getOne(proxyId), String.format("Unknown Proxy (%d)", proxyId));
    }

    //==========================================================================
    @Cacheable("proxiesByAddrr")
    public Optional<Proxy> findProxy(String address) {

        return proxyRepo.findByAddress(address);
    }

    //==========================================================================
    @Override
    public void initialize() {

        super.initialize();
        this.initializeProxyList();

        accountService.findUsedIPs()
                .stream()
                .forEach(ip -> ProxyClient.blackIplist.add(ip));
    }

    //==========================================================================
    public void registerSuccessProxy(Integer proxyId) {
        lastSuccessfullProxy.set(proxyId);
    }

    public void registerProxyFails(Integer proxyId) {
        log.info("register fail 4 proxyId = {}", proxyId);

        totalFails.incrementAndGet();
        lastSuccessfullProxy.set(0);

        Optional.ofNullable(proxyFails
                .entrySet()
                .stream()
                .filter(proxy -> proxy.getKey().equals(proxyId))
                .findAny()
                .orElseThrow(() -> new RuntimeException("registerProxyFails: Unknown proxyId " + proxyId.toString())))
                .ifPresent(proxy -> proxy.setValue(proxy.getValue().intValue() + 1));

        log.info("proxyFails details: {}", proxyFails);

    }

    //==========================================================================
    @Transactional(readOnly = true)
    @Deprecated
    public synchronized Proxy findAvailAbleProxy() {

        StmtProcessor.ifTrue(proxyClients.isEmpty(), () -> {
            throw new RuntimeException("Available proxies is not found");
        });

        final Proxy proxy;

        final Optional<Entry<Integer, ProxyClient>> opProxyClient
                = proxyClients
                        .entrySet()
                        .stream()
                        .filter(p -> p.getValue().getIsValid()
                        && (!p.getValue().getIBusy() || (p.getValue().getReservedUntil().compareTo(LocalDateTime.now()) < 0)))
                        .sorted((a, b) -> a.getValue().getUsedTimes().compareTo(b.getValue().getUsedTimes()))
                        .findFirst();

        if (opProxyClient.isPresent()) {

            final ProxyClient proxyClient = opProxyClient.get().getValue();

            proxyClient.setIBusy(SysConst.BOOLEAN_TRUE);
            proxyClient.setReservedUntil(LocalDateTime.now().plusMinutes(15));
            proxyClient.setUsedTimes(proxyClient.getUsedTimes() + 1);

            proxy = findProxy(proxyClient.getProxyId());

            log.info("getting available proxy: {} ", proxy.getProxyId());
        } else {
            proxy = null;
            log.warn("all proxies are busy");
        }

//        log.warn("available proxies: {}/{}",
//                proxyClients.entrySet().stream().filter(p -> p.getValue().getIsValid()).count(),
//                proxyClients.size());
        log.warn("proxi fails: {}", proxyFails);

        return proxy;

    }

    //==========================================================================
    public synchronized void releaseProxy(Integer proxyId) {

        log.info("release proxy: {}", proxyId);

        proxyClients
                .entrySet()
                .stream()
                .filter(p -> p.getValue().getProxyId().equals(proxyId))
                .findAny()
                .orElseThrow(() -> new RuntimeException("releaseProxy: no proxy found: " + proxyId))
                .getValue()
                .setIBusy(SysConst.BOOLEAN_FALSE);

    }

    //==========================================================================
    public void initializeProxyList() {

        proxyClients.clear();

        proxyRepo
                .findByProxyStatus(refsService.findProxyStatus(PS_ACTUAL))
                .stream()
                .forEach(p -> {

                    proxyClients.put(p.getProxyId(), new ProxyClient(p.getProxyId(), p.getCredit(), p.getAddress(), getUserAgent(), acceptHeader, acceptLanguage));
                    proxyFails.put(p.getProxyId(), 0);

                });
    }

    //==========================================================================
    public Integer getThreadsInProgress() {

        return (int) proxyClients
                .entrySet()
                .stream()
                .filter(p -> p.getValue().getIBusy())
                .count();

    }
}
