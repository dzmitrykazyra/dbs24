/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.serv.component;

import java.time.LocalDateTime;
import java.util.Collection;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.insta.api.IfsCreatedFollowers;
import org.dbs24.insta.api.IfsGetFollowersTask;
import static org.dbs24.insta.api.consts.InstaConsts.Consumers.CON_GROUP_ID;
import static org.dbs24.insta.api.consts.InstaConsts.Topics.IFS_CREATE_FOLLOWERS_LIST;
import org.dbs24.insta.serv.kafka.InstaServKafkaService;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import com.ibm.icu.text.Transliterator;
import java.util.concurrent.atomic.AtomicInteger;
import org.dbs24.insta.api.IfsServiceTask;
import org.dbs24.insta.api.service.SpamDetectorService;

@Log4j2
@Service
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-serv")
public class FollowersBuilderService extends AbstractApplicationService {

    @Value("${config.dictionary.followers.ru:nonames}")
    private String legalNamesRu;

    final Collection<String> listlegalNamesRu;

    @Value("${config.dictionary.followers.en:nonames}")
    private String legalNamesEn;
    final Collection<String> listlegalNamesEn;

    final InstaServKafkaService instaServKafkaService;
    final String CYRILLIC_TO_LATIN = "Cyrillic-Latin; Latin-ASCII";
    final Transliterator toLatinTrans = Transliterator.getInstance(CYRILLIC_TO_LATIN);
    final SpamDetectorService spamDetectorService;

    public FollowersBuilderService(InstaServKafkaService instaServKafkaService, SpamDetectorService spamDetectorService) {
        this.instaServKafkaService = instaServKafkaService;
        this.spamDetectorService = spamDetectorService;

        listlegalNamesRu = ServiceFuncs.createConcurencyCollection();
        listlegalNamesEn = ServiceFuncs.createConcurencyCollection();

    }

    @PostConstruct
    public void initializeService() {

        listlegalNamesRu.addAll(new ArrayList<String>(Arrays.asList(legalNamesRu.replaceAll(" ", "").toLowerCase().split(","))));
        listlegalNamesEn.addAll(new ArrayList<String>(Arrays.asList(legalNamesEn.replaceAll(" ", "").toLowerCase().split(","))));

        listlegalNamesRu.forEach(fn -> listlegalNamesEn.add(toLatinTrans.transliterate(fn)));

        log.debug("listlegalNamesRu = {}", listlegalNamesRu);
        log.debug("listlegalNamesEn = {}", listlegalNamesEn);

    }

    @KafkaListener(id = IFS_CREATE_FOLLOWERS_LIST, groupId = CON_GROUP_ID, topics = IFS_CREATE_FOLLOWERS_LIST)
    public void confirmFollowersList(Collection<IfsCreatedFollowers> followers4creation) {
//        log.info("{}: receive kaffka msg: {}",
//                IFS_SERVICE_TASK, serviceTasks.size());

        log.debug("{}: receive kaffka msg: {}", IFS_CREATE_FOLLOWERS_LIST, followers4creation.size());

        final Collection<String> ignoreList = ServiceFuncs.createCollection();
        final Collection<String> whiteList = ServiceFuncs.createCollection();
        final Collection<String> whiteListFull = ServiceFuncs.createCollection();

        followers4creation.stream().forEach(f -> {

            log.debug("isnataId = {}, nextMaxId = {}, isMoreAvailable = {}", f.getInstaId(), f.getNextMaxId(), f.getIsMoreAvailAble());

            f.getFollowers().forEach(follower -> {

                final String instaFullName = follower.getInstaFullUserName().toLowerCase();
                final String instaUserName = follower.getInstaUserName().toLowerCase();

                Boolean isWhite = listlegalNamesRu.stream().anyMatch(s -> instaFullName.equals(s));

                if (!isWhite) {
                    isWhite = listlegalNamesRu.stream().anyMatch(s -> instaFullName.contains(s.concat(" ")));
                }

                if (!isWhite) {
                    isWhite = listlegalNamesRu.stream().anyMatch(s -> instaFullName.contains(" ".concat(s)));
                }

                if (!isWhite) {
                    isWhite = listlegalNamesEn.stream().anyMatch(s -> instaFullName.equals(s));
                }

                if (!isWhite) {
                    isWhite = listlegalNamesEn.stream().anyMatch(s -> instaFullName.contains(s.concat(" ")));
                }

                if (!isWhite) {
                    isWhite = listlegalNamesEn.stream().anyMatch(s -> instaFullName.contains(" ".concat(s)));
                }

                // check blackList
                if (isWhite) {
                    isWhite = !spamDetectorService.isSpam(instaUserName);
                }

                // check blackList
                if (isWhite) {
                    isWhite = !spamDetectorService.isSpam(instaFullName);
                }

                if (isWhite) {
                    whiteList.add(instaUserName);
                    whiteListFull.add(String.format("%s/%s", follower.getInstaUserName(), follower.getInstaFullUserName()));
                } else {
                    ignoreList.add(String.format("%s/%s", follower.getInstaUserName(), follower.getInstaFullUserName()));
                }
            });

            // get next followers
            if (f.getIsMoreAvailAble()) {
                // get next followers
                instaServKafkaService.sendGetFollwersTask(
                        StmtProcessor.create(IfsGetFollowersTask.class, igf -> {
                            igf.setInstaId(f.getInstaId());
                            igf.setExecuteDate(NLS.localDateTime2long(LocalDateTime.now().plusSeconds(30)));
                            igf.setNewMaxId(f.getNextMaxId());
                        }));
            }

        });

        log.debug("white list size {}/{}", whiteListFull.size(), whiteListFull.size() + ignoreList.size());
        //log.debug("white list is {}", whiteListFull);
        //log.debug("ignore list is {}", ignoreList);

        // task 2 create accounts from follower profile
        final Long startPoint = NLS.localDateTime2long(LocalDateTime.now());
        final Long interval = Long.valueOf(30000);

        final AtomicInteger accounts = new AtomicInteger();

        // register accounts from followers
        whiteList.forEach(followerName
                -> instaServKafkaService.sendCreateAccountTask(StmtProcessor.create(IfsServiceTask.class,
                        st -> {
                            st.setInstaUserName(followerName);
                            st.setExecuteDate(startPoint + (interval * accounts.getAndIncrement()));
                        })));
    }
}
