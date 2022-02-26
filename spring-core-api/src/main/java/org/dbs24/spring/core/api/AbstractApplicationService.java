/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.spring.core.api;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import static org.dbs24.stmt.StmtProcessor.execute;

@Log4j2
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractApplicationService extends AbstractApplicationBean {

    @Setter
    @Getter
    private WebClient webClient;

    //==========================================================================
    @Override
    public void initialize() {
        final String className = this.getClass().getSimpleName();
        log.info("Service '{}' is activated", className.indexOf("$$") > 0 ? className.substring(0, className.indexOf("$$")) : className);
    }

    //==========================================================================    
    @Override
    public void destroy() {
        final String className = this.getClass().getSimpleName();
        log.info("Service '{}' is destroyed", className.indexOf("$$") > 0 ? className.substring(0, className.indexOf("$$")) : className);
    }

    protected String buildDefaultErrMsg(Throwable throwable) {
        return String.format("Unexpected error - go home or see the fucked log4j for details: %s: %s",
                throwable.getClass().getSimpleName(), throwable.getMessage());
    }

    /////////////////////////////////////////////////////////////////////////////
    protected WebClient.Builder setupSSL(WebClient.Builder builder, String uri) {

        if (uri.startsWith("https://")) {
            setupClientConnector(builder);
        }

        return builder;

    }

    //===========================================================================
    private void setupClientConnector(WebClient.Builder builder) {

        execute(() -> {

            final SslContext sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            log.debug("SslContext is created: {} ", sslContext.getClass().getCanonicalName());

            final HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));

            log.debug("HttpClient is created: {} ", httpClient.getClass().getCanonicalName());

            final ReactorClientHttpConnector reactorClientHttpConnector = new ReactorClientHttpConnector(httpClient);

            log.debug("ReactorClientHttpConnector is created: {} ", reactorClientHttpConnector.getClass().getCanonicalName());

            builder.clientConnector(reactorClientHttpConnector);

        });
    }
}
