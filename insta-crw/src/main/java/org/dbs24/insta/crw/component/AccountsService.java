/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.crw.component;

import org.dbs24.proxy.starter.service.UnzippingInterceptor;
import java.util.Collection;
import javax.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.insta.api.IfsServiceTask;
import org.dbs24.insta.api.IfsAccountIsCreated;
import static org.dbs24.insta.api.consts.InstaConsts.Consumers.CON_GROUP_ID;
import static org.dbs24.insta.api.consts.InstaConsts.Topics.IFS_SERVICE_TASK;
import org.dbs24.kafka.AbstractKafkaService;
import org.dbs24.kafka.api.KafkaMessage;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import com.github.instagram4j.instagram4j.requests.users.UsersUsernameInfoRequest;
import static com.github.instagram4j.instagram4j.IGConstants.BASE_API_URL;
import org.springframework.scheduling.annotation.Scheduled;
import com.github.instagram4j.instagram4j.responses.users.UserResponse;
import com.github.instagram4j.instagram4j.models.user.User;
import com.github.instagram4j.instagram4j.IGClient;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import okhttp3.OkHttpClient;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.insta.crw.kafka.KafkaCrowlerService;
import org.dbs24.insta.api.service.SpamDetectorService;
import org.dbs24.proxy.starter.service.ProxyProviderService;
import com.github.instagram4j.instagram4j.utils.IGUtils;

@Log4j2
@Service
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-crw")
public class AccountsService extends AbstractApplicationService {

    final Collection<IfsServiceTask> newTasks = ServiceFuncs.createCollection();
    final KafkaCrowlerService kafkaCrowlerService;
    final IGClientsService iGClientsService;
    final SpamDetectorService spamDetectorService;
    final ProxyProviderService proxyProviderService;

    public AccountsService(KafkaCrowlerService kafkaCrowlerService, IGClientsService iGClientsService, SpamDetectorService spamDetectorService, ProxyProviderService proxyProviderService) {
        this.kafkaCrowlerService = kafkaCrowlerService;
        this.iGClientsService = iGClientsService;
        this.spamDetectorService = spamDetectorService;
        this.proxyProviderService = proxyProviderService;
    }

    @KafkaListener(id = IFS_SERVICE_TASK, groupId = CON_GROUP_ID, topics = IFS_SERVICE_TASK)
    public void receiveTask(Collection<IfsServiceTask> serviceTasks) {
//        log.info("{}: receive kaffka msg: {}",
//                IFS_SERVICE_TASK, serviceTasks.size());

        //log.debug("{}: receive kaffka msg: {}", IFS_SERVICE_TASK, serviceTasks.size());
        // add new tasks 4 research
        newTasks.addAll(serviceTasks
                .stream()
                .filter(userName -> newTasks.stream().filter(existUserName -> existUserName.getInstaUserName().equals(userName)).count() == 0)
                .collect(Collectors.toList()));

        log.debug("{}: pool size = {}", IFS_SERVICE_TASK, newTasks.size());

    }

    public void addNewCustomTask(IfsServiceTask serviceTask) {

        log.debug("{}: add custom task: {}", IFS_SERVICE_TASK, serviceTask);

        newTasks.add(serviceTask);

    }

    @Scheduled(fixedRateString = "${config.kafka.processing-interval:3000}", cron = "${config.kafka.processing-cron:}")
    private void perform() {

        newTasks
                .stream()
                .filter(st -> st.getExecuteDate().compareTo(NLS.localDateTime2long(LocalDateTime.now())) <= 0)
                .findAny()
                .ifPresent(instaAccount -> {

                    log.debug("process account = {}", instaAccount.getInstaUserName());

                    final UsersUsernameInfoRequest unir = new UsersUsernameInfoRequest(instaAccount.getInstaUserName());

                    //final IGClient client = iGClientsService.getIGClient4Uage();
//                    log.debug("sessionid = {}", client.getSessionId());
//
//                    client.setSessionId(client.getSessionId());

//IGUtils.defaultHttpClientBuilder().

                    proxyProviderService.doTask(643, null, 10, "insta-crw", IGUtils.defaultHttpClientBuilder(), okClient -> {

                        log.debug(" okClient = {}", okClient.toString());

                        final IGClient client = IGClient.builder()
                                .username(user)
                                .password(pwd)
                                .client(okClient)
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
                                //                    .simulatedLogin();
                                .login();

//                        client.setHttpClient(okClient);

                        final UserResponse response = client.sendRequest(unir).join();

                        log.debug("response  = {}", response.getStatusCode());

                        final User user = response.getUser();

                        // check spam 
                        if (!spamDetectorService.isSpam(user.getBiography())) {

                            log.debug("register user  = {}/{}, followers = {}, following = {}, media.count = {}", user.getPk(), user.getFull_name(), user.getFollower_count(), user.getFollowing_count(), user.getMedia_count());

                            kafkaCrowlerService.sendConfirmTaskCreation(
                                    StmtProcessor.create(IfsAccountIsCreated.class,
                                            ant -> {
                                                ant.setInstaId(user.getPk());
                                                ant.setFollowees(user.getFollowing_count());
                                                ant.setFollowers(user.getFollower_count());
                                                ant.setInstaBiography(user.getBiography());
                                                ant.setInstaFullName(user.getFull_name());
                                                ant.setInstaUserName(user.getUsername());
                                                ant.setIsPrivate(user.is_private());
                                                ant.setIsVerified(user.is_verified());
                                                ant.setMediacount(user.getMedia_count());
                                                ant.setProfilePicHdUrl(user.getHd_profile_pic_url_info().url);
                                                ant.setProfilePicUrl(user.getProfile_pic_url());
                                            }));
                        }

                    });

                    // remove task
                    newTasks.remove(instaAccount);
                });
    }

    //==========================================================================
//    final String user = "mlonso48";
//    final String pwd = "Dzz1sfy5wg";
    final String user = "cyangurner7";
    final String pwd = "fT!7PyUm7X";

    @PostConstruct
    public void initializeService() {

        StmtProcessor.execute(() -> {

            //addNewCustomTask(StmtProcessor.create(IfsServiceTask.class, st -> st.setInstaLink("alla_orfey")));
            //addNewCustomTask(StmtProcessor.create(IfsServiceTask.class, st -> st.setInstaLink("pesnyary_belarus_official")));
            //addNewCustomTask(StmtProcessor.create(IfsServiceTask.class, st -> st.setInstaLink("pesnyary_belorusskie")));
            //addNewCustomTask(StmtProcessor.create(IfsServiceTask.class, st -> st.setInstaLink("pesnyary_fan")));
            //addNewCustomTask(StmtProcessor.create(IfsServiceTask.class, st -> st.setInstaLink("vsdayn")));
//            addNewCustomTask(StmtProcessor.create(IfsServiceTask.class, st -> {
//                st.setInstaUserName("olunkatka");
//                st.setExecuteDate(NLS.localDateTime2long(LocalDateTime.now().plusSeconds(15)));
//            }));
//            addNewCustomTask(StmtProcessor.create(IfsServiceTask.class, st -> {
//                st.setInstaUserName("ikakurin");
//                st.setExecuteDate(NLS.localDateTime2long(LocalDateTime.now().plusSeconds(30)));
//            }));
//            addNewCustomTask(StmtProcessor.create(IfsServiceTask.class, st -> {
//                st.setInstaUserName("nikolaiko63");
//                st.setExecuteDate(NLS.localDateTime2long(LocalDateTime.now().plusSeconds(30)));
//            }));
//            addNewCustomTask(StmtProcessor.create(IfsServiceTask.class, st -> {
//                st.setInstaUserName("alla_orfey");
//                st.setExecuteDate(NLS.localDateTime2long(LocalDateTime.now().plusSeconds(10)));
//            }));
        });
    }

    //==========================================================================
}
