package org.dbs24.tik.assist.service.tiktok;

import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.dao.*;
import org.dbs24.tik.assist.entity.domain.TiktokAccount;
import org.dbs24.tik.assist.entity.domain.User;
import org.dbs24.tik.assist.entity.domain.UserPlan;
import org.dbs24.tik.assist.entity.domain.UserSubscription;
import org.dbs24.tik.assist.entity.dto.tiktok.*;
import org.dbs24.tik.assist.entity.dto.tiktok.response.AccountActionResponseDto;
import org.dbs24.tik.assist.entity.dto.tiktok.response.AccountAddResponseDto;
import org.dbs24.tik.assist.entity.dto.tiktok.response.AccountDeleteResponseDto;
import org.dbs24.tik.assist.service.exception.CannotFindTiktokUsernameException;
import org.dbs24.tik.assist.service.exception.NoSuchTiktokAccountException;
import org.dbs24.tik.assist.service.exception.UserHasNoBoundedAccountsException;
import org.dbs24.tik.assist.service.tiktok.resolver.TiktokInteractor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class TiktokAccountService extends AbstractApplicationService {

    private final TiktokInteractor tiktokInteractor;

    private final UserDao userDao;
    private final TiktokAccountDao tiktokAccountDao;
    private final UserPlanDao userPlanDao;
    private final UserSubscriptionDao userSubscriptionDao;
    private final ReferenceDao referenceDao;

    public TiktokAccountService(TiktokInteractor tiktokInteractor, TiktokAccountDao tiktokAccountDao, UserDao userDao, UserPlanDao userPlanDao,
                                UserSubscriptionDao userSubscriptionDao, ReferenceDao referenceDao) {

        this.tiktokInteractor = tiktokInteractor;
        this.tiktokAccountDao = tiktokAccountDao;
        this.userDao = userDao;
        this.userPlanDao = userPlanDao;
        this.userSubscriptionDao = userSubscriptionDao;
        this.referenceDao = referenceDao;
    }

    public TiktokPlanDtoList boundAccountWithProfile(Integer userId, TiktokAccountDto tiktokAccountDto) {

        User user = userDao.findUserById(userId);

       /* return StmtProcessor.create(
                AccountAddResponseDto.class,
                accountAddResponseDto -> {
                    tiktokInteractor.getTiktokAccount(tiktokAccountDto.getTiktokUsername(), user);
                    accountAddResponseDto.setTiktokUsernames(tiktokAccountDao.findByUser(user).
                            stream().
                            map(TiktokAccount::getAccountUsername).
                            collect(Collectors.toList()));
                }
        );*/

        return StmtProcessor.create(
                TiktokPlanDtoList.class,
                tiktokPlanDtoList -> {
                    tiktokInteractor.getTiktokAccount(tiktokAccountDto.getTiktokUsername(), user);
                    tiktokPlanDtoList.setTiktokUserPlans(getAccountPlanInfo(user));
                }
        );
    }

    @Transactional
    public TiktokPlanDtoList unboundAccountWithProfile(Integer userId, TiktokAccountDto tiktokAccountDto) {

        User user = userDao.findUserById(userId);

       /* return StmtProcessor.create(
                AccountDeleteResponseDto.class,
                accountDeleteResponseDto -> {
                    tiktokAccountDao.removeByTiktokNameAndUser(tiktokAccountDto.getTiktokUsername(), user);
                    List<TiktokAccount> accounts = tiktokAccountDao.findByUser(user);
                    accountDeleteResponseDto.setTiktokUsernames(accounts
                            .stream()
                            .map(TiktokAccount::getAccountUsername).collect(Collectors.toList()));
                    accountDeleteResponseDto.getTiktokUsernames()
                            .stream()
                            .findFirst()
                            .ifPresentOrElse(accountDeleteResponseDto::setLastSelectedAccount,
                                    () -> accountDeleteResponseDto.setLastSelectedAccount(null));
                 //   accountDeleteResponseDto.setLastSelectedAccount(getLastSelectedUserAccount(userId, LastSelectedAccountDto.of("")).getTiktokAccountUsername());
                }
        );*/

        return StmtProcessor.create(
                TiktokPlanDtoList.class,
                tiktokPlanDtoList -> {
                    Optional<UserPlan> userPlan = userPlanDao.findActualUserPlanByUserAndTiktokAccountAndStatus(user, tiktokInteractor.getTiktokAccount(tiktokAccountDto.getTiktokUsername(), user), referenceDao.findActivePlanStatus());
                    userPlan.ifPresent(plan -> userSubscriptionDao.removeById(plan.getUserSubscription().getUserSubscriptionId()));
                    tiktokAccountDao.removeByTiktokNameAndUser(tiktokAccountDto.getTiktokUsername(), user);
                    tiktokPlanDtoList.setTiktokUserPlans(getAccountPlanInfo(user));
                }
        );
    }

    public TiktokPlanDtoList getAllUserAccounts(Integer userId) {

        User user = userDao.findUserById(userId);

        /*return StmtProcessor.create(
                TiktokAccountDtoList.class,
                tiktokAccountDtoList -> tiktokAccountDtoList.setTiktokUsernames(
                        tiktokAccountDao
                                .findByUser(user)
                                .stream()
                                .map(TiktokAccount::getAccountUsername)
                                .collect(Collectors.toList())
                )
        );*/
        return StmtProcessor.create(
                TiktokPlanDtoList.class,
                tiktokPlanDtoList -> tiktokPlanDtoList.setTiktokUserPlans(getAccountPlanInfo(user)));
    }

    public TiktokAccountInfoDto getLastSelectedUserAccount(Integer userId, LastSelectedAccountDto lastSelectedAccountDto) {

        TiktokUserDto tiktokUserDto;
        User user = userDao.findUserById(userId);

        if (lastSelectedAccountDto.getLastSelectedAccountUsername().isBlank()) {
            tiktokUserDto = tiktokAccountDao.findByUser(user)
                    .stream()
                    .findFirst()
                    .map(
                            tiktokAccount -> tiktokInteractor.searchTiktokUserByUsername(tiktokAccount.getAccountUsername())
                    )
                    .orElseThrow(() -> new UserHasNoBoundedAccountsException(HttpStatus.NO_CONTENT));
        } else {
            tiktokUserDto = tiktokInteractor.searchTiktokUserByUsername(lastSelectedAccountDto.getLastSelectedAccountUsername());
        }

        if (tiktokUserDto.getName() == null) {

            tiktokInteractor.searchTiktokUserBySecUserId(
                    tiktokAccountDao
                            .findOptionalByTiktokUsernameAndUser(
                                    lastSelectedAccountDto.getLastSelectedAccountUsername(),
                                    user
                            )
                            .map(TiktokAccount::getSecUserId)
                            .orElseThrow(() -> new NoSuchTiktokAccountException(HttpStatus.BAD_REQUEST)
                    )
            );
        }

        return TiktokAccountInfoDto.of(tiktokUserDto);
    }


    public TiktokPlanDto getActualPlanByTiktokAccount(Integer userId, TiktokAccountDto tiktokAccountDto){
        User user = userDao.findUserById(userId);
        TiktokAccount tiktokAccount = tiktokAccountDao.findOptionalByTiktokUsernameAndUser(tiktokAccountDto.getTiktokUsername(), user).orElseThrow(() -> new CannotFindTiktokUsernameException(HttpStatus.BAD_REQUEST));
        return getTiktokPlanDto(user, tiktokAccount);
    }

    public TiktokExistSubscriptionDtoList getAllSubscriptions(Integer userId){
        User user = userDao.findUserById(userId);
        return StmtProcessor.create(TiktokExistSubscriptionDtoList.class,
                tiktokExistSubscriptionDtoList ->
                        tiktokExistSubscriptionDtoList.setAccountsSubscriptions(tiktokAccountDao.findByUser(user)
                                .stream()
                                .map(tiktokAccount ->
                                        TiktokExistSubscriptionDto.of(tiktokAccount, getTiktokPlanDto(user, tiktokAccount).getPlanInfoDto() != null)).
                                collect(Collectors.toList()))
        );
    }

    private List<TiktokPlanDto> getAccountPlanInfo(User user){
        return tiktokAccountDao
                .findByUser(user)
                .stream()
                .map(tiktokAccount ->
                        getTiktokPlanDto(user, tiktokAccount)).collect(Collectors.toList());
    }

    private TiktokPlanDto getTiktokPlanDto(User user, TiktokAccount tiktokAccount){
        TiktokUserDto tiktokUserDto = tiktokInteractor.searchTiktokUserByUsername(tiktokAccount.getAccountUsername());
        Optional<UserPlan> userPlan = userPlanDao.findActualUserPlanByUserAndTiktokAccountAndStatus(user, tiktokAccount, referenceDao.findActivePlanStatus());
        if(userPlan.isPresent()){
            return TiktokPlanDto.of(tiktokUserDto, userPlan.get(), tiktokAccount.getAccountId());
        }
        return TiktokPlanDto.of(tiktokUserDto, tiktokAccount.getAccountId());
    }
}
