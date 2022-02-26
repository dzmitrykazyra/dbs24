/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.crw.component;

import org.dbs24.proxy.starter.service.UnzippingInterceptor;
import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.models.user.Profile;
import com.github.instagram4j.instagram4j.models.user.User;
import com.github.instagram4j.instagram4j.requests.friendships.FriendshipsFeedsRequest;
import com.github.instagram4j.instagram4j.requests.users.UsersUsernameInfoRequest;
import com.github.instagram4j.instagram4j.responses.feed.FeedUsersResponse;
import com.github.instagram4j.instagram4j.responses.users.UserResponse;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.insta.api.IfsAccountIsCreated;
import org.dbs24.insta.api.IfsGetFollowersTask;
import org.dbs24.insta.api.IfsServiceTask;
import static org.dbs24.insta.api.consts.InstaConsts.Consumers.CON_GROUP_ID;
import static org.dbs24.insta.api.consts.InstaConsts.Topics.IFS_GET_FOLLOWERS_TASK;
import org.dbs24.insta.crw.kafka.KafkaCrowlerService;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.dbs24.insta.api.IfsCreatedFollowers;
import org.dbs24.insta.api.ShortFollowerRecord;

@Log4j2
@Service
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-crw")
public class FollowersService extends AbstractApplicationService {

    final Collection<IfsGetFollowersTask> newTasks = ServiceFuncs.createCollection();
    final IGClientsService iGClientsService;
    final KafkaCrowlerService kafkaCrowlerService;
       
    public FollowersService(IGClientsService iGClientsService, KafkaCrowlerService kafkaCrowlerService) {
        this.iGClientsService = iGClientsService;
        this.kafkaCrowlerService = kafkaCrowlerService;
    }

    @KafkaListener(id = IFS_GET_FOLLOWERS_TASK, groupId = CON_GROUP_ID, topics = IFS_GET_FOLLOWERS_TASK)
    public void receiveTask(Collection<IfsGetFollowersTask> serviceTasks) {
//        log.info("{}: receive kaffka msg: {}",
//                IFS_SERVICE_TASK, serviceTasks.size());

        log.debug("{}: receive kaffka msg: {}", IFS_GET_FOLLOWERS_TASK, serviceTasks.size());

        // add new tasks 4 research
        newTasks.addAll(serviceTasks);

    }

    @Scheduled(fixedRateString = "${config.kafka.processing-interval:3000}", cron = "${config.kafka.processing-cron:}")
    private void perform() {

        newTasks.stream()
                .filter(st -> st.getExecuteDate().compareTo(NLS.localDateTime2long(LocalDateTime.now())) <= 0)
                .findAny()
                .ifPresent(instaTask -> {

                    final FeedUsersResponse feedUsersResponse
                            = new FriendshipsFeedsRequest(instaTask.getInstaId(), FriendshipsFeedsRequest.FriendshipsFeeds.FOLLOWERS, instaTask.getNewMaxId()).execute(iGClientsService.getIGClient4Uage()).join();

                    log.debug("{}/FOLLOWERS: count  = {}, getNext_max_id = {}, isMore_available = {}",
                            instaTask.getInstaId(),
                            feedUsersResponse.getUsers().size(),
                            feedUsersResponse.getNext_max_id(),
                            feedUsersResponse.isMore_available());

                    final List<Profile> users = feedUsersResponse.getUsers();

                    log.debug("instaId: {}: receive {} followers ", instaTask.getInstaId(), users.size());

                    // send followers 2 confirm
                    kafkaCrowlerService.createFollowersList(StmtProcessor.create(IfsCreatedFollowers.class,
                            cf -> {
                                cf.setInstaId(instaTask.getInstaId());
                                cf.setIsMoreAvailAble(feedUsersResponse.isMore_available());
                                cf.setNextMaxId(feedUsersResponse.getNext_max_id());

                                cf.setFollowers(ServiceFuncs.createCollection());

                                users.forEach(user -> cf.getFollowers().add(
                                StmtProcessor.create(ShortFollowerRecord.class,
                                        sfr -> {
                                            sfr.setInstaUserName(user.getUsername());
                                            sfr.setInstaFullUserName(user.getFull_name());
                                        })));
                            }));

                    newTasks.remove(instaTask);
                });
    }
    //==========================================================================
    private OkHttpClient okHttpClient;

    private OkHttpClient getProxy() {

        return StmtProcessor.notNull(okHttpClient) ? okHttpClient : createProxy();

    }

    private OkHttpClient createProxy() {

        final String proxyCredit = "node-ru-185.astroproxy.com:10020:allisoncatcher4185:f3d331";

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

        okHttpClient = new OkHttpClient.Builder().proxy(proxy)./*proxyAuthenticator(proxyAuthenticator).*/addInterceptor(new UnzippingInterceptor()).connectTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).build();

        return okHttpClient;

    }
}
