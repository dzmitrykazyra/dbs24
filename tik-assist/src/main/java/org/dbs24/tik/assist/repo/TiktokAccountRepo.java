package org.dbs24.tik.assist.repo;

import org.dbs24.spring.core.data.ApplicationJpaRepository;
import org.dbs24.tik.assist.entity.domain.TiktokAccount;
import org.dbs24.tik.assist.entity.domain.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface TiktokAccountRepo extends ApplicationJpaRepository<TiktokAccount, Integer>, JpaSpecificationExecutor<TiktokAccount>, PagingAndSortingRepository<TiktokAccount, Integer> {

    List<TiktokAccount> findBySecUserId(String secUserId);

    List<TiktokAccount> findByUser(User user);

    Optional<TiktokAccount> findBySecUserIdAndUser(String secUserId, User user);
    Optional<TiktokAccount> findByAccountUsernameAndUser(String username, User user);
}