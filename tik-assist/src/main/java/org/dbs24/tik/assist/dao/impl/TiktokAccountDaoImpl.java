package org.dbs24.tik.assist.dao.impl;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.dao.TiktokAccountDao;
import org.dbs24.tik.assist.entity.domain.TiktokAccount;
import org.dbs24.tik.assist.entity.domain.User;
import org.dbs24.tik.assist.repo.TiktokAccountRepo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Log4j2
@Component
public class TiktokAccountDaoImpl implements TiktokAccountDao {

    final TiktokAccountRepo tiktokAccountRepo;

    public TiktokAccountDaoImpl(TiktokAccountRepo tiktokAccountRepo) {

        this.tiktokAccountRepo = tiktokAccountRepo;
    }

    @Override
    public TiktokAccount save(TiktokAccount tiktokAccount) {

        return tiktokAccountRepo.save(tiktokAccount);
    }

    /**
     * Remove
     * @param tiktokUsername account from
     * @param user profile, or 'loose'
     */
    @Override
    public TiktokAccount setLooseToTiktokAccount(String tiktokUsername, User user) {

        TiktokAccount accountToLoose = findOptionalByTiktokUsernameAndUser(tiktokUsername, user)
                .orElseThrow(() -> new RuntimeException("Cannon find bounded account in repo"));
        accountToLoose.setUser(null);

        return save(accountToLoose);
    }

    @Override
    public void removeByTiktokNameAndUser(String tiktokName, User user) {
        Optional<TiktokAccount> account = tiktokAccountRepo.findByAccountUsernameAndUser(tiktokName, user);
        tiktokAccountRepo.deleteById(account.get().getAccountId());
    }

    @Override
    public TiktokAccount findByAccountId(Integer accountId) {

        return tiktokAccountRepo
                .findById(accountId)
                .orElseThrow(() -> new RuntimeException("No tiktok account with such id"));
    }

    @Override
    public List<TiktokAccount> findByUser(User user) {

        return tiktokAccountRepo.findByUser(user);
    }

    @Override
    public List<TiktokAccount> findBySecUserId(String secUserId) {

        return tiktokAccountRepo.findBySecUserId(secUserId);
    }

    @Override
    public Optional<TiktokAccount> findOptionalBySecUserIdAndUser(String secUserId, User user) {

        return tiktokAccountRepo.findBySecUserIdAndUser(secUserId, user);
    }

    @Override
    public Optional<TiktokAccount> findOptionalByTiktokUsernameAndUser(String tiktokUsername, User user) {

        return tiktokAccountRepo.findByAccountUsernameAndUser(tiktokUsername, user);
    }
}
