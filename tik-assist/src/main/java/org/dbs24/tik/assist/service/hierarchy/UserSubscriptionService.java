package org.dbs24.tik.assist.service.hierarchy;

import io.vavr.control.Either;
import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.dto.payment.MonthlySubscriptionPaymentDto;
import org.dbs24.tik.assist.entity.dto.plan.CustomPlanConstraint;
import org.dbs24.tik.assist.dao.*;
import org.dbs24.tik.assist.entity.domain.*;
import org.dbs24.tik.assist.entity.dto.subscription.*;
import org.dbs24.tik.assist.entity.dto.subscription.UserSubscriptionIdDto;
import org.dbs24.tik.assist.entity.dto.subscription.UserSubscriptionPlanStatisticsDto;
import org.dbs24.tik.assist.entity.dto.tiktok.TiktokPlanDto;
import org.dbs24.tik.assist.entity.dto.tiktok.TiktokPlanDtoList;
import org.dbs24.tik.assist.entity.dto.tiktok.TiktokUserDto;
import org.dbs24.tik.assist.entity.dto.user.UserIdDto;
import org.dbs24.tik.assist.service.exception.NoActiveUserSubscriptionException;
import org.dbs24.tik.assist.service.exception.TiktokAccountAlsoHaveSubscriptionException;
import org.dbs24.tik.assist.service.exception.TiktokAccountIsNotLinkedException;
import org.dbs24.tik.assist.service.hierarchy.resolver.SumResolver;
import org.dbs24.tik.assist.service.hierarchy.split.SubscriptionToPlanSplitter;
import org.dbs24.tik.assist.service.tiktok.resolver.TiktokInteractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class UserSubscriptionService extends AbstractApplicationService {

    @Value("${constraint.subscription.duration-in-days}")
    private int userSubscriptionDurationInDays;

    private final UserDao userDao;
    private final UserPlanDao userPlanDao;
    private final ReferenceDao referenceDao;
    private final PromocodeDao promocodeDao;
    private final PlanTemplateDao planTemplateDao;
    private final UserSubscriptionDao userSubscriptionDao;
    private final TiktokAccountDao tiktokAccountDao;


    private final SumResolver sumResolver;
    private final TiktokInteractor tiktokInteractor;
    private final SubscriptionToPlanSplitter subscriptionToPlanSplitter;

    public UserSubscriptionService(UserSubscriptionDao userSubscriptionDao,
                                   ReferenceDao referenceDao,
                                   PromocodeDao promocodeDao,
                                   UserDao userDao,
                                   PlanTemplateDao planTemplateDao,
                                   UserPlanDao userPlanDao,
                                   SumResolver sumResolver,
                                   SubscriptionToPlanSplitter subscriptionToPlanSplitter,
                                   TiktokInteractor tiktokInteractor,
                                   TiktokAccountDao tiktokAccountDao) {
        this.userSubscriptionDao = userSubscriptionDao;
        this.referenceDao = referenceDao;
        this.promocodeDao = promocodeDao;
        this.userDao = userDao;
        this.planTemplateDao = planTemplateDao;
        this.userPlanDao = userPlanDao;
        this.sumResolver = sumResolver;
        this.subscriptionToPlanSplitter = subscriptionToPlanSplitter;
        this.tiktokInteractor = tiktokInteractor;
        this.tiktokAccountDao = tiktokAccountDao;
    }

    @Transactional
    public UserSubscriptionIdDto createUserSubscriptionByTemplate(Integer userId, ByTemplateUserSubscriptionDto byTemplateUserSubscriptionDto) {

        return createUserSubscription(
                userId,
                byTemplateUserSubscriptionDto,
                Either.left(planTemplateDao.findPlanTemplateById(byTemplateUserSubscriptionDto.getSubscriptionTemplateId()))
        );
    }

    @Transactional
    public UserSubscriptionIdDto createUserSubscriptionCustom(Integer userId, CustomUserSubscriptionDto customUserSubscriptionDto) {

        return createUserSubscription(
                userId,
                customUserSubscriptionDto,
                Either.right(customUserSubscriptionDto.getCustomPlanConstraint())
        );
    }

    private UserSubscriptionIdDto createUserSubscription(Integer userId,
                                                         AbstractUserSubscriptionDto userSubscriptionDto,
                                                         Either<PlanTemplate, CustomPlanConstraint> templateOrCustomInfo) {
        User user = userDao.findUserById(userId);
        Promocode promocode = promocodeDao.activatePromocode(userId, userSubscriptionDto.getPromocodeValue());
        TiktokAccount tiktokAccount = tiktokAccountDao.findOptionalByTiktokUsernameAndUser(userSubscriptionDto.getTiktokUsername(), user).orElseThrow(() -> new TiktokAccountIsNotLinkedException(HttpStatus.BAD_REQUEST));

        if(userPlanDao.findActualUserPlanByUserAndTiktokAccountAndStatus(user,tiktokAccount, referenceDao.findActivePlanStatus()).isPresent()){
            throw new TiktokAccountAlsoHaveSubscriptionException(HttpStatus.FORBIDDEN);
        }

        BigDecimal totalSubscriptionSum = sumResolver.calculateSubscriptionSum(
                templateOrCustomInfo,
                Optional.ofNullable(promocode),
              //  userSubscriptionDto.getSubscriptionAccountUrls().length
                1
        );

        UserSubscription subscriptionToSave = createFreshUserSubscription(Optional.ofNullable(promocode), user);
        subscriptionToSave.setSubscriptionSum(totalSubscriptionSum);

        UserSubscription savedUserSubscription = userSubscriptionDao.saveUserSubscription(subscriptionToSave);


        /*Runnable subscriptionSplittingThread = () -> subscriptionToPlanSplitter.split(
                savedUserSubscription,
                tiktokInteractor.createOrGetExistingTiktokAccounts(userSubscriptionDto.getSubscriptionAccountUrls(), user),
                templateOrCustomInfo
        );*/

        Runnable subscriptionSplittingThread = () -> {
            List<UserPlan> userPlan = subscriptionToPlanSplitter.split(
                    savedUserSubscription,
                    List.of(tiktokAccount),
                    templateOrCustomInfo
            );
        };
        new Thread(subscriptionSplittingThread).start();

        return UserSubscriptionIdDto.toDto(savedUserSubscription);
    }

    /**
     * Method allows creating newly created UserSubscription object with null userId and totalSubscriptionSub fields
     */
    public UserSubscription createFreshUserSubscription(Optional<Promocode> promocodeOptional, User user) {

        UserSubscription userSubscription = UserSubscription.builder()
                .actualDate(LocalDateTime.now())
                .beginDate(LocalDateTime.now())
                .endDate(getCurrentSubscriptionEndDateTime())
                .userSubscriptionStatus(referenceDao.findActiveUserSubscriptionStatus())
                .user(user)
                .build();

        promocodeOptional.ifPresent(userSubscription::setPromocode);

        return userSubscription;
    }

    private LocalDateTime getCurrentSubscriptionEndDateTime() {

        return LocalDateTime.now().plusDays(userSubscriptionDurationInDays);
    }

    public UserSubscriptionIdDto updateUserSubscriptionByTemplate(Integer userId, UpdateUserSubscriptionByTemplateDto updateUserSubscriptionByTemplateDto) {

        User user = userDao.findUserById(userId);
        PlanTemplate planTemplate = planTemplateDao.findPlanTemplateById(updateUserSubscriptionByTemplateDto.getNewPlanTemplateId());

        return createNewSubscriptionBasedOnActiveSubscription(user, Either.left(planTemplate));
    }

    @Transactional
    public UserSubscriptionIdDto updateUserSubscriptionCustom(Integer userId, CustomPlanConstraint customPlanConstraint) {

        User user = userDao.findUserById(userId);

        return createNewSubscriptionBasedOnActiveSubscription(user, Either.right(customPlanConstraint));
    }

    /**
     * Method allows creating new active user subscription with bounded with elder active user subscription
     */
    private UserSubscriptionIdDto createNewSubscriptionBasedOnActiveSubscription(
            User user,
            Either<PlanTemplate, CustomPlanConstraint> templateOrCustomInfo
    ) {

        //TODO NOTIFY PAYMENT BACKEND
        UserSubscription activeUserSubscription = getActiveUserSubscriptionOptional(user);

        List<TiktokAccount> boundedTiktokAccounts = userPlanDao
                .findUserPlansBySubscription(activeUserSubscription)
                .stream()
                .map(UserPlan::getTiktokAccount)
                .collect(Collectors.toList());


        BigDecimal totalSubscriptionSum = sumResolver.calculateSubscriptionSum(
                templateOrCustomInfo,
                Optional.empty(),
                boundedTiktokAccounts.size()
        );

        UserSubscription subscriptionToSave = createFreshUserSubscription(Optional.empty(), user);
        subscriptionToSave.setSubscriptionSum(totalSubscriptionSum);

        UserSubscription savedNewUserSubscription = userSubscriptionDao.saveUserSubscription(subscriptionToSave);

        subscriptionToPlanSplitter.split(
                savedNewUserSubscription,
                boundedTiktokAccounts,
                templateOrCustomInfo
        );

        return UserSubscriptionIdDto.toDto(savedNewUserSubscription);
    }

    /**
     * Method allows get active user tiktok account boost subscription by getting the latest by end date plan and converting it ot DTO
     * (because one subscription consist of many same plans with disparate bounded accounts)
     */
    public UserSubscriptionPlanStatisticsDto getActiveSubscriptionDtoByUserIdAndTiktokUsername(Integer userId, String tiktokUsername) {

        return getEndDateLatestUserPlanOptionalByTiktokUsername(userDao.findUserById(userId), tiktokUsername)
                .map(UserSubscriptionPlanStatisticsDto::toDto)
                .orElseThrow(() -> new NoActiveUserSubscriptionException(HttpStatus.NO_CONTENT));
    }

    private UserSubscription getActiveUserSubscriptionOptional(User user) {

        Optional<UserSubscription> activeUserSubscriptionOptional = Optional.empty();
        Optional<UserPlan> userPlanOptional = getEndDateLatestUserPlanOptional(user);

        if (userPlanOptional.isPresent()) {
            activeUserSubscriptionOptional = Optional.ofNullable(userPlanOptional.get().getUserSubscription());
        }

        return activeUserSubscriptionOptional.orElseThrow(() -> new NoActiveUserSubscriptionException(HttpStatus.NO_CONTENT));
    }

    /**
     * Method allows get the latest by end date user plan
     */
    private Optional<UserPlan> getEndDateLatestUserPlanOptional(User user) {

        return userPlanDao
                .findActiveUserPlansByUser(user)
                .stream()
                .max(Comparator.comparing(UserPlan::getEndDate));
    }

    private Optional<UserPlan> getEndDateLatestUserPlanOptionalByTiktokUsername(User user, String tiktokUsername) {

        return userPlanDao
                .findActiveUserPlansByTiktokAccount(tiktokInteractor.getTiktokAccount(tiktokUsername, user))
                .stream()
                .max(Comparator.comparing(UserPlan::getEndDate));
    }

    /**
     * Method allows update active user subscription data by monthly payment
     */
    @Transactional
    public UserIdDto extendMonthlySubscription(MonthlySubscriptionPaymentDto paymentDto) {

        User user = userDao.findUserById(paymentDto.getUserId());
        UserSubscription subscriptionToExtend = userSubscriptionDao.findLatestUserSubscription(user)
                .orElseThrow(() -> new NoActiveUserSubscriptionException(HttpStatus.NO_CONTENT));

        subscriptionToExtend.setEndDate(getCurrentSubscriptionEndDateTime());
        userSubscriptionDao.updateUserSubscription(subscriptionToExtend);

        subscriptionToPlanSplitter.saveSplitBefore(subscriptionToExtend);

        return UserIdDto.of(user);
    }

    /**
     * Method undoes required by id subscription (complete to the end this paid for),
     * notifies payment backend to undo monthly debiting of funds
     */
    public TiktokPlanDtoList undoUserSubscription(Integer userId, Integer userSubscriptionId) {
        User user = userDao.findUserById(userId);
        Optional<UserSubscription> userSubscriptionFind = userSubscriptionDao.findUserSubscriptionByUserAndId(user, userSubscriptionId);
        userSubscriptionFind.ifPresent(userSubscription -> {
            userPlanDao.updateUserPlansToDone(userPlanDao.findUserPlansBySubscription(userSubscription));
            userSubscriptionDao.updateUserSubscriptionToNonActive(userSubscription);
        });
        //TODO NOTIFY PAYMENT BACKED

        return StmtProcessor
                .create(TiktokPlanDtoList.class,
                        tiktokPlanDtoList -> {
                            tiktokPlanDtoList.setTiktokUserPlans(
                                    tiktokAccountDao.findByUser(user)
                                            .stream()
                                            .map(tiktokAccount -> {
                                                TiktokUserDto tiktokUserDto = tiktokInteractor.searchTiktokUserByUsername(tiktokAccount.getAccountUsername());
                                                Optional<UserPlan> userPlan = userPlanDao.findActualUserPlanByUserAndTiktokAccountAndStatus(user, tiktokAccount, referenceDao.findActivePlanStatus());
                                                if(userPlan.isPresent()){
                                                    return TiktokPlanDto.of(tiktokUserDto, userPlan.get(), tiktokAccount.getAccountId());
                                                }
                                                return TiktokPlanDto.of(tiktokUserDto, tiktokAccount.getAccountId());
                                            }).collect(Collectors.toList()));
                        } );
    }
}
