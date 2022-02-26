package org.dbs24.tik.assist.dao;

import org.dbs24.tik.assist.entity.domain.TiktokAccount;
import org.dbs24.tik.assist.entity.domain.User;

import java.util.List;
import java.util.Optional;

public interface TiktokAccountDao {

    TiktokAccount save(TiktokAccount tiktokAccount);

    TiktokAccount  setLooseToTiktokAccount(String tiktokUsername, User user);

    void removeByTiktokNameAndUser(String tiktokName,User user);

    TiktokAccount findByAccountId(Integer tiktokAccountId);
    List<TiktokAccount> findByUser(User user);
    List<TiktokAccount> findBySecUserId(String secUserId);
    Optional<TiktokAccount> findOptionalBySecUserIdAndUser(String secUserId, User user);
    Optional<TiktokAccount> findOptionalByTiktokUsernameAndUser(String tiktokUsername, User user);
}
