/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.crw.component;

import com.github.instagram4j.instagram4j.IGClient;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import java.util.Collection;
import javax.annotation.PostConstruct;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.stmt.StmtProcessor;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import org.dbs24.proxy.core.rest.api.proxy.ProxyInfo;
import org.dbs24.proxy.core.rest.api.request.CreatedProxyRequest;
import org.dbs24.proxy.core.rest.api.usage.CreatedUsage;
import org.dbs24.proxy.starter.ThreadExecutorService;

@Log4j2
@Service
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-crw")
public class IGClientsService extends AbstractApplicationService {

    final Collection<IGClientServiceUsage> agentsList = ServiceFuncs.createCollection();

    final ThreadExecutorService executorService;

    public IGClientsService(ThreadExecutorService executorService) {
        this.executorService = executorService;
    }

    //==========================================================================
    @PostConstruct
    public void initializeService() {

        StmtProcessor.execute(() -> {

            //addIGClient("cyangurner7", "fT!7PyUm7X", "46207022247%3AY4kJEQpu7i5DS1%3A0");
            //addIGClient("lauracaryl24", "fR#1Awvnc4", "46245578086%3AoabA3u29yVXsSy%3A5");
//            addIGClient("cyangurner7", "fT!7PyUm7X", "");
//            addIGClient("lauracaryl24", "fR#1Awvnc4", "");
//            addIGClient("noya9624", "Mds46z9bvpt0", "");
//            addIGClient("gonathon33", "Hfr9t86fkg8v4vn5", "");
//            addIGClient("mlonso48", "Dzz1sfy5wg", "");
//            proxyProviderService.doTask(643, null, 10, "insta-crw", okClient -> {
//                
//                log.debug(" okClient = {}", okClient.toString());
//                
//            });
        });
    }

    //--------------------------------------------------------------------------
    private void addIGClient(String user, String pwd, String sessionId) {

        StmtProcessor.execute(() -> {

            log.debug("try 2 login with {}/{}: [{}]", user, pwd, sessionId);

            //IGUtils.setCookieValue(okHttpClient.cookieJar(),"sessionid",sessionId);
            //final OkHttpClient client = getProxy();
//            final ArrayList<Cookie> cookies = new ArrayList<>();
//            cookies.add(new Cookie.Builder().name("sessionid").value(sessionId).domain("i.instagram.com").build());
//            client.cookieJar().saveFromResponse(HttpUrl.get(IGConstants.BASE_API_URL), cookies);
            //final IGClient iGClient = new IGClient(user, pwd, client);
            final IGClient iGClient = new IGClient(user, pwd);

            //iGClient.setSessionId(sessionId);
            iGClient.sendLoginRequest().join();

//            final IGClient iGClient = IGClient.builder()
//                    .username(user)
//                    .password(pwd)
//                    .client(getProxy())
//                    .onLogin((cli, response) -> {
//                        
//                        log.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!11");
//                        
//                        cli.setSessionId(sessionId);
//
//                        final ArrayList<Cookie> cookies = new ArrayList<>();
//                        cookies.add(new Cookie.Builder().name("sessionid").value(sessionId).domain("i.instagram.com").build());
//                        cli.getHttpClient().cookieJar().saveFromResponse(HttpUrl.get(IGConstants.BASE_API_URL), cookies);
//
//                        cli.sendRequest(new AccountsCurrentUserRequest()).join();
//                    })
//                    //                    .simulatedLogin();
//                    .login();
            //iGClient.setSessionId(sessionId);
            log.debug("success login with {}/{}", user, pwd);

            agentsList.add(StmtProcessor.create(IGClientServiceUsage.class, clu -> {
                clu.setAgent(iGClient);
                clu.setLastUsed(LocalDateTime.MIN);
            }));

        });
    }

    //--------------------------------------------------------------------------
    public IGClient getIGClient4Uage() {

        final IGClientServiceUsage iGClientServiceusage = agentsList
                .stream()
                .sorted((a, b) -> a.getLastUsed().compareTo(b.getLastUsed()))
                .limit(1)
                .findFirst()
                .orElseThrow();

        iGClientServiceusage.setLastUsed(LocalDateTime.now());

        log.debug("agent is ready: {}/{}",
                iGClientServiceusage.getAgent().getSelfProfile().getPk(),
                iGClientServiceusage.getAgent().getSelfProfile().getUsername());

        return iGClientServiceusage.getAgent();

    }

    //--------------------------------------------------------------------------
}
