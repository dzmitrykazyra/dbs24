/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.entity.Payment;
import org.dbs24.entity.SubscriptionPhone;
import org.dbs24.entity.VisitNote;
import org.dbs24.jpa.spec.*;
import org.dbs24.repository.*;
import org.dbs24.rest.api.*;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.time.LocalDateTime;
import java.util.Collection;

import static org.dbs24.consts.WaConsts.*;

@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "wa-migration")
public class WaMigrationRest extends CommonRest {

    final AppUserRepository appUserRepository;
    final PersistenceService persistenceEntityManager;
    final AuthKeyRepository authKeyRepository;
    final SubscriptionPhoneRepository subscriptionPhoneRepository;
    final VisitNoteRepository visitNoteRepository;
    final PaymentRepository paymentRepository;


    public WaMigrationRest(PersistenceService persistenceEntityManager,
                           AppUserRepository appUserRepository,
                           AuthKeyRepository authKeyRepository,
                           SubscriptionPhoneRepository subscriptionPhoneRepository,
                           VisitNoteRepository visitNoteRepository,
                           PaymentRepository paymentRepository,
                           GenericApplicationContext genericApplicationContext) {
        super(genericApplicationContext);
        this.persistenceEntityManager = persistenceEntityManager;
        this.appUserRepository = appUserRepository;
        this.authKeyRepository = authKeyRepository;
        this.subscriptionPhoneRepository = subscriptionPhoneRepository;
        this.visitNoteRepository = visitNoteRepository;
        this.paymentRepository = paymentRepository;
    }
    //==========================================================================

    final AgentsFilter agentsFilter = (idFrom, idTo) -> (r, cq, cb) -> {

        final Predicate predicate = cb.conjunction();

        predicate.getExpressions().add(cb.between(r.get("id"), idFrom, idTo));

        return predicate;
    };

    public Mono<ServerResponse> getAgents(ServerRequest request) {

        return this.<AuthKeyCollection>createResponse(request,
                AuthKeyCollection.class,
                () -> StmtProcessor.create(AUTH_KEY_COLLECTION_CLASS, createdAuthkey -> {

                    final Integer id1 = Integer.valueOf(request.headers().firstHeader("id1"));
                    final Integer id2 = Integer.valueOf(request.headers().firstHeader("id2"));

                    StmtProcessor.assertNotNull(Integer.class, id1, "AuthKeyID from");
                    StmtProcessor.assertNotNull(Integer.class, id2, "AuthKeyID to");

                    log.debug("getting authkeys [{}, {}]", id1, id2);

                    authKeyRepository
                            .findAll(agentsFilter.setFilter(id1, id2))
                            .stream()
                            //                            .filter(agent -> agent.getGroupId().equals(3))
                            .map(agent -> StmtProcessor.create(AUTH_KEY_INFO_CLASS, authKeyInfo -> {
                                authKeyInfo.setId(agent.getId());
                                authKeyInfo.setGroupId(agent.getGroupId());
                                authKeyInfo.setPhoneNum(agent.getPhoneNum());
                                authKeyInfo.setLastChangeTime(agent.getLastChangeTime());
                                authKeyInfo.setCreateTime(agent.getCreateTime());
                                authKeyInfo.setPayLoad(agent.getPayLoad());
                                //authKeyInfo.set

                            }))
                            .forEach(agent -> createdAuthkey.getCollection().add(agent));

                    log.debug("returning {} agents", createdAuthkey.getCollection().size());

                }));
    }
    //==========================================================================

    final AppUsersFilter usersFilter = (idFrom, idTo) -> (r, cq, cb) -> {

        final Predicate predicate = cb.conjunction();

        predicate.getExpressions().add(cb.between(r.get("id"), idFrom, idTo));

        return predicate;
    };

    final PaymentsFilter paymentsFilter = (idFrom, idTo) -> (r, cq, cb) -> {

        final Predicate predicate = cb.conjunction();

        predicate.getExpressions().add(cb.between(r.get("appUser"), idFrom, idTo));

        return predicate;
    };

    final SubscriptionPhoneFilter subscriptionPhoneFilter = (idFrom, idTo) -> (r, cq, cb) -> {

        final Predicate predicate = cb.conjunction();

        predicate.getExpressions().add(cb.between(r.get("appUser"), idFrom, idTo));
        predicate.getExpressions().add(cb.isNotNull(r.get("authKey")));
        predicate.getExpressions().add(cb.equal(r.get("isRemoved"), 0));
        predicate.getExpressions().add(cb.equal(r.get("isValid"), 1));

        return predicate;
    };

    final VisitNoteD1D2 visitNoteFilter = (idFrom, idTo) -> (r, cq, cb) -> {

        final Predicate predicate = cb.conjunction();

        final Subquery<SubscriptionPhone> spSubquery = cq.subquery(SP_CLASS);
        final Root<SubscriptionPhone> spRoot = spSubquery.from(SP_CLASS);

        final Predicate subQuryPredicate = cb.conjunction();
        subQuryPredicate.getExpressions()
                .add(cb.between(spRoot.get("appUser"), idFrom, idTo));

        spSubquery.select(spRoot.get("id"))
                .distinct(true)
                .where(subQuryPredicate);

        predicate.getExpressions().add(cb.in(r.get("subscriptionPhone")).value(spSubquery));
        predicate.getExpressions().add(cb.greaterThan(r.get("addTime"), LocalDateTime.now().minusDays(1)));

        return predicate;
    };

    final VisitNoteAddTime visitNoteAddTime = (from, to) -> (r, cq, cb) -> {

        final Predicate predicate = cb.conjunction();

        predicate.getExpressions().add(cb.between(r.get("addTime"), from, to));

        return predicate;

    };

    //==========================================================================
    public Mono<ServerResponse> getUsers(ServerRequest request) {

        return this.<AppUserCollection>createResponse(request,
                AppUserCollection.class,
                () -> StmtProcessor.create(APP_USER_COLLECTION_CLASS, createdUserCollection -> {

                    final Integer id1 = Integer.valueOf(request.headers().firstHeader("id1"));
                    final Integer id2 = Integer.valueOf(request.headers().firstHeader("id2"));

                    StmtProcessor.assertNotNull(Integer.class, id1, "UserID from");
                    StmtProcessor.assertNotNull(Integer.class, id2, "UserID to");

                    log.debug("getting payments [{}, {}]", id1, id2);

                    final Collection<Payment> payments
                            = paymentRepository
                            .findAll(paymentsFilter.setFilter(id1, id2));

                    log.debug("found {} payments ", payments.size());

                    log.debug("getting subscriptions [{}, {}]", id1, id2);

                    final Collection<SubscriptionPhone> subscriptions
                            = subscriptionPhoneRepository
                            .findAll(subscriptionPhoneFilter.setFilter(id1, id2));

                    log.debug("found {} subscriptions ", subscriptions.size());

                    log.debug("getting visitNotes [{}, {}]", id1, id2);

                    final Collection<VisitNote> visitNotes
                            = visitNoteRepository
                            .findAll(visitNoteFilter.setFilter(id1, id2));
//                                    .findAll(visitNoteAddTime.setFilter(LocalDateTime.now().minusDays(1), LocalDateTime.now()));

                    log.debug("found {} visitNotes ", visitNotes.size());

                    log.debug("getting users [{}, {}]", id1, id2);

                    appUserRepository
                            .findAll(usersFilter.setFilter(id1, id2))
                            .stream()
                            .map(appUser -> StmtProcessor.create(USER_INFO_CLASS, userInfo -> {

                                userInfo.assign(appUser);

                                // add payments
                                userInfo.setPayments(ServiceFuncs.<PaymentInfo>createCollection());
                                payments
                                        .stream()
                                        .filter(pmt -> pmt.getAppUser().getId().equals(appUser.getId()))
                                        .filter(pmt -> ((pmt.getPrice().signum() == 1)
                                                || (pmt.getPayType().equals("gift"))
                                                || (pmt.getSubsAmount() > 1)
                                                || (StmtProcessor.notNull(pmt.getGpOrderId()))))
                                        .forEach(pmt -> userInfo.getPayments().add(StmtProcessor.create(GPI_CLASS, p -> p.assign(pmt))));

                                // add subscription
                                userInfo.setSubscriptions(ServiceFuncs.<SubscriptionPhoneInfo>createCollection());

                                // add visitNote
                                userInfo.setVisitNotes(ServiceFuncs.<VisitNoteInfo>createCollection());

                                subscriptions
                                        .stream()
                                        .filter(spi -> spi.getAppUser().getId().equals(appUser.getId()))
                                        .forEach(spi -> {
                                            userInfo.getSubscriptions().add(StmtProcessor.create(SUBSCRIPTION_PHONE_INFO_CLASS, p -> p.assign(spi)));

                                            visitNotes
                                                    .stream()
                                                    .filter(vn -> vn.getSubscriptionPhone().getId().equals(spi.getId()))
                                                    .forEach(vn -> userInfo.getVisitNotes().add(StmtProcessor.create(VISIT_NOTE_INFO_CLASS, p -> p.assign(vn))));
                                        });
                            }))
                            .forEach(user -> createdUserCollection.getCollection().add(user));

                    log.debug("returning {} users", createdUserCollection.getCollection().size());

                }));
    }

    //==========================================================================
    public Mono<ServerResponse> getActualUsers(ServerRequest request) {

        return this.<AppUserIdCollection>createResponse(request,
                AppUserIdCollection.class,
                () -> StmtProcessor.create(APP_USER_ID_COLLECTION_CLASS, createdUserCollection -> {

                    log.debug("getting actual users ");

                    appUserRepository
                            .findActualUAppsers()
                            .stream()
                            .sorted((a, b) -> a.getId().compareTo(b.getId()))
                            .forEach(user -> createdUserCollection.getCollection().add(StmtProcessor.create(AppUserId.class, u -> u.setUserId(user.getId()))));

                    log.debug("returning {} actual ids users", createdUserCollection.getCollection().size());

                }));
    }

    //==========================================================================
    public Mono<ServerResponse> getSubscriptions(ServerRequest request) {

        return this.<SubscriptionPhoneInfo, SubscriptionPhoneCollection>createResponse(request,  SUBSCRIPTION_PHONE_INFO_CLASS, SubscriptionPhoneCollection.class,
                userSubscriptionInfo -> StmtProcessor.create(SUBSCRIPTION_PHONE_COLLECTION_CLASS, createdUserSubscriptionCollection -> {

                    subscriptionPhoneRepository
                            .findAll()
                            .stream()
                            .limit(1000)
                            .map(sp -> StmtProcessor.create(SUBSCRIPTION_PHONE_INFO_CLASS, subscriptionInfo -> {
                                subscriptionInfo.setAddTime(sp.getAddTime());
                                subscriptionInfo.setId(sp.getId());
                                subscriptionInfo.setAssignedName(sp.getAssignedName());
                                //subscriptionInfo.set

                            }))
                            .forEach(si -> createdUserSubscriptionCollection.getCollection().add(si));

                    log.debug("returning {} users subscription", createdUserSubscriptionCollection.getCollection().size());

                }));
    }
    //==========================================================================
//    @Override
//    protected ShutdownRequest ready4ShutDown() {
//        return StmtProcessor.create(ShutdownRequest.class, sr -> {
//            
//            sr.setCanShutDown(!visitNoteReactor.isBusy());
//            sr.setCanShutDown(true);
//            sr.setStatus(sr.getCanShutDown() ? "is ready 4 Shutdown" : " Processor is busy");
//            
//        });
//    }
//    
//    public Mono<ServerResponse> createAppUser(ServerRequest request) {
//        
//        return this.<UserInfo, CreatedUser>processServerRequest(request, USER_INFO_CLASS,
//                user -> StmtProcessor.create(CREATED_USER_CLASS, createdUser -> {
//                    
//                    final AppUser appUser = (Optional.ofNullable(user.getID())
//                            .orElseGet(() -> 0) > 0)
//                            ? appUserRepository
//                                    .findById(user.getID())
//                                    .orElseThrow(() -> new AppUserIsNotFound(String.format("userId not found ({})", user.getID())))
//                            : StmtProcessor.create(APP_USER_CLASS, u -> u.setRegTime(LocalDateTime.now()));
//                    
//                    log.debug("try 2 creating/update user: {}", user);
//                    
//                    appUser.setAndriodSecureId(user.getANDROID_SECURE_ID());
//                    appUser.setAppName(user.getAPP_NAME());
//                    appUser.setAppVersion(user.getAPP_VERSION());
//                    appUser.setAuthToken(user.getAUTH_TOKEN());
//                    appUser.setCountryCode(user.getCOUNTRY_CODE());
//                    appUser.setDeviceFingerPring(user.getDEVICE_FINGERPRINT());
//                    appUser.setGcmTokeN(user.getGSF_ID());
//                    appUser.setGsfId(user.getGSF_ID());
//                    appUser.setIpAddress(user.getIP_ADDRESS());
//                    
//                    appUserRepository.save(appUser);
//                    
//                    createdUser.setId(appUser.getId());
//                    
//                    log.debug("created/update user: {}", createdUser);
//                    
//                }));
//    }
//    
//    public Mono<ServerResponse> createAuthKey(ServerRequest request) {
//        
//        return this.<AuthKeyInfo, CreatedAuthKey>processServerRequest(request, AUTH_KEY_INFO_CLASS,
//                keyInfo -> StmtProcessor.create(CREATED_AUTH_KEY_CLASS, createdAuthkey -> {
//                    
//                    final AuthKey authKey = (Optional.ofNullable(keyInfo.getId())
//                            .orElseGet(() -> 0) > 0)
//                            ? authKeyRepository
//                                    .findById(keyInfo.getId())
//                                    .orElseThrow(() -> new AppUserIsNotFound(String.format("authKey not found ({})", keyInfo.getId())))
//                            : StmtProcessor.create(AUTH_KEY_CLASS, a -> a.setCreateTime(LocalDateTime.now()));
//                    
//                    log.debug("try 2 creating/update authKey: {}", keyInfo);
//                    
//                    authKey.setGroupId(keyInfo.getGroupId());
//                    authKey.setLastChangeTime(keyInfo.getLastChangeTime());
//                    authKey.setPayLoad(keyInfo.getPayLoad());
//                    authKey.setPhoneNum(keyInfo.getPhoneNum());
//                    authKey.setLastChangeTime(LocalDateTime.now());
//                    
//                    authKeyRepository.save(authKey);
//                    
//                    createdAuthkey.setId(authKey.getId());
//                    
//                    log.debug("created/update authKey: {}", createdAuthkey);
//                    
//                }));
//    }
//    
//    public Mono<ServerResponse> createSubscriptionPhone(ServerRequest request) {
//        
//        return this.<SubscriptionPhoneInfo, CreatedSubscriptionPhone>processServerRequest(request, SPI_CLASS,
//                spi -> StmtProcessor.create(CREATED_SP_CLASS, createdSp -> {
//                    
//                    final SubscriptionPhone subscriptionPhone = (Optional.ofNullable(spi.getId())
//                            .orElseGet(() -> 0) > 0)
//                            ? subscriptionPhoneRepository
//                                    .findById(spi.getId())
//                                    .orElseThrow(() -> new RuntimeException(String.format("SubscriptionPhoneKey not found ({})", spi.getId())))
//                            : StmtProcessor.create(SP_CLASS, a -> a.setAddTime(LocalDateTime.now()));
//                    
//                    log.debug("try 2 creating/update SubscriptionPhone: {}", spi);
//                    
//                    //subscriptionPhone.setAppUser(appUserRepository.getOne(spi.getUserId()));
//                    subscriptionPhone.setAssignedName(spi.getAssignedName());
//                    subscriptionPhone.setAuthKey(authKeyRepository.getOne(spi.getAuthKeyId()));
//                    //subscriptionPhone.setAvatar(avatar);
//                    subscriptionPhone.setIsRemoved(spi.getIsRemoved());
//                    subscriptionPhone.setIsValid(spi.getIsValid());
//                    subscriptionPhone.setNotify(spi.getNotify());
//                    subscriptionPhone.setPhoneNum(spi.getPhoneNum());
//                    
//                    subscriptionPhoneRepository.save(subscriptionPhone);
//                    
//                    createdSp.setId(subscriptionPhone.getId());
//                    
//                    log.debug("created/update SubscriptionPhone: {}", createdSp);
//                    
//                }));
//    }
//    
//    public Mono<ServerResponse> createVisitNote(ServerRequest request) {
//        
//        return this.<VisitNoteInfo, CreatedVisitNote>processServerRequest(request, VISIT_NOTE_INFO_CLASS,
//                vni -> StmtProcessor.create(CREATED_VISIT_NOTE_CLASS, createdVn -> {
//                    
//                    final VisitNote visitNote = (Optional.ofNullable(vni.getId())
//                            .orElseGet(() -> Long.valueOf(0)) > 0)
//                            ? visitNoteRepository
//                                    .findById(vni.getId())
//                                    .orElseThrow(() -> new RuntimeException(String.format("SubscriptionPhoneKey not found ({})", vni.getId())))
//                            : StmtProcessor.create(VISIT_NOTE_CLASS, a -> a.setAddTime(LocalDateTime.now()));
//                    
//                    log.debug("try 2 creating/update visitNote: {}", vni);
//                    
//                    visitNote.setIsOnline(vni.getIsOnline());
//                    visitNote.setSubscriptionPhone(subscriptionPhoneRepository.getOne(vni.getPhoneId()));
//                    
//                    visitNoteRepository.save(visitNote);
//                    
//                    createdVn.setId(visitNote.getId());
//                    
//                    log.debug("created/update visitNote: {}", createdVn);
//                    
//                }));
//    }
//    
//    public Mono<ServerResponse> createPayment(ServerRequest request) {
//        
//        return this.<GooglePaymentInfo, CreatedGooglePayment>processServerRequest(request, GPI_CLASS,
//                pmtInfo -> StmtProcessor.create(CREATED_GP_CLASS, createdPmt -> {
//                    
//                    final GooglePayment googlePayment = (Optional.ofNullable(pmtInfo.getId())
//                            .orElseGet(() -> 0) > 0)
//                            ? googlePaymentRepository
//                                    .findById(pmtInfo.getId())
//                                    .orElseThrow(() -> new RuntimeException(String.format("Payment not found ({})", pmtInfo.getId())))
//                            : StmtProcessor.create(GP_CLASS, a -> a.setFulfilTime(LocalDateTime.now()));
//                    
//                    log.debug("try 2 creating/update payment: {}", pmtInfo);
//                    
//                    //googlePayment.setAppUser(appUserRepository.getOne(pmtInfo.getUser_id()));
//                    googlePayment.setCurCode(pmtInfo.getCurCode());
//                    googlePayment.setFulfilTime(pmtInfo.getFulfilTime());
//                    googlePayment.setGpOrderId(pmtInfo.getGpOrderId());
//                    googlePayment.setGpPurchaseToken(pmtInfo.getGpPurchaseToken());
//                    googlePayment.setGpStrPrice(pmtInfo.getGpStrPrice());
//                    googlePayment.setPrice(pmtInfo.getPrice());
//                    googlePayment.setPayType(pmtInfo.getPayType());
//                    googlePayment.setSubsAmount(pmtInfo.getSubsAmount());
//                    googlePayment.setValidUntil(pmtInfo.getValidUntil());
//                    
//                    googlePaymentRepository.save(googlePayment);
//                    
//                    createdPmt.setId(googlePayment.getId());
//                    
//                    log.debug("created/update payment: {}", googlePayment);
//                    
//                }));
//    }
//    
//    public Mono<ServerResponse> createHiLoadVisitNote(ServerRequest request) {
//        
//        return this.<VisitNoteInfo, CreatedVisitNote>processServerRequest(request, VISIT_NOTE_INFO_CLASS,
//                vni -> StmtProcessor.create(CREATED_VISIT_NOTE_CLASS, createdVn -> {
//                    
//                    //visitNoteReactor.emitEvent(vni);
//                    
//                    createdVn.setId(Long.valueOf(0));
//                    
//                }));
//    }
}
