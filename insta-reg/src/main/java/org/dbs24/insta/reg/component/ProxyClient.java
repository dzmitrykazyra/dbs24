/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.component;

import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;
import lombok.extern.log4j.Log4j2;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.dbs24.insta.reg.email.UnzippingInterceptor;
import org.dbs24.stmt.StmtProcessor;
import lombok.Getter;
import lombok.Setter;
import java.util.Collection;
import java.time.LocalDateTime;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import static org.dbs24.consts.SysConst.BOOLEAN_TRUE;
import static org.dbs24.consts.SysConst.BOOLEAN_FALSE;

@Log4j2
public class ProxyClient {

    @Getter
    final Integer proxyId;
    final String proxyCredit;
    final String uriChangeIp;
    @Getter
    final OkHttpClient httpProxyClient;
    final OkHttpClient httpClient;
    final String userAgent;
    final String acceptHeader;
    final String acceptLanguage;

    @Getter
    private Boolean isValid = BOOLEAN_TRUE;
    private Integer faild = 0;

    @Getter
    @Setter
    private Boolean iBusy = BOOLEAN_FALSE;

    @Getter
    @Setter
    private LocalDateTime reservedUntil;

    @Getter
    @Setter
    private Integer usedTimes = 0;

    static final Collection<String> blackIplist = ServiceFuncs.<String>createConcurencyCollection();

    //==========================================================================
    public ProxyClient(Integer proxyId, String proxyCredit, String uriChangeIp, String userAgent, String acceptHeader, String acceptLanguage) {

        this.proxyId = proxyId;
        this.proxyCredit = proxyCredit;
        this.uriChangeIp = uriChangeIp;
        this.userAgent = userAgent;
        this.acceptHeader = acceptHeader;
        this.acceptLanguage = acceptLanguage;

        final String[] parts = proxyCredit.split(":");

        log.info("build client proxy: [{}]", proxyCredit);

        final Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(parts[0], Integer.parseInt(parts[1])));

        java.net.Authenticator.setDefault(new java.net.Authenticator() {
            private PasswordAuthentication authentication = new PasswordAuthentication(parts[2], parts[3].toCharArray());

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return authentication;
            }
        });

        httpClient = new OkHttpClient.Builder().addInterceptor(new UnzippingInterceptor()).connectTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).build();

        httpProxyClient = new OkHttpClient.Builder().proxy(proxy)./*proxyAuthenticator(proxyAuthenticator).*/addInterceptor(new UnzippingInterceptor()).connectTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).build();

    }

    //==========================================================================
    public String getNewIp() {

        return StmtProcessor.<String>create(() -> {

            Boolean needUpdate = Boolean.FALSE;
            String newIp = "not assigned";
            Integer attempt = 0;
            Integer attemptLimit = 150;

            try {
                do {
//
                    attempt++;

                    StmtProcessor.ifTrue(needUpdate, () -> {
                        log.info("{}: delay for {}", uriChangeIp, 3000);
                        StmtProcessor.sleep(3000);
                    });

                    final RequestBody formBody = new FormBody.Builder().build();

                    log.info("attempt {}: processing uri: {}", attempt, uriChangeIp);

                    final Request request = new Request.Builder()
                            .url(uriChangeIp)
                            .addHeader("User-Agent", userAgent)
                            .addHeader("accept", acceptHeader)
                            .addHeader("accept-language", acceptLanguage)
                            .post(formBody)
                            .get()
                            .build();

                    final Response response = httpClient.newCall(request).execute();

                    final String changeIp = new String(response.body().bytes());

                    log.info("{}: attempt {}, change ip: {}", proxyCredit, attempt, changeIp);

                    needUpdate = !changeIp.contains("\"IP\":") && !blackIplist.contains(changeIp);
                    newIp = changeIp;

                    if (!needUpdate) {
                        synchronized (blackIplist) {
                            blackIplist.add(newIp);
                        }
                    }

                    if (attemptLimit.equals(attempt)) {
                        throw new RuntimeException("fucking proxy gives out used addresses - ".toUpperCase() + proxyCredit);
                    }

                    StmtProcessor.ifTrue(!needUpdate, () -> log.info("{}: getting new IP = {}, code = {}", uriChangeIp, changeIp, response.code()));

                } while (needUpdate);

                log.info("{}: black list ip size: {}", proxyCredit, blackIplist.size());

            } catch (Throwable th) {
                faild++;

                log.error("can't get new ip: {}", th);

                if (faild.equals(5)) {
                    isValid = false;
                    log.error("proxy is disabled due to multiply error connections ({}, {})".toUpperCase(),
                            proxyId,
                            proxyCredit);
                }

                throw th;
            }

            return newIp;

        });
    }
}
