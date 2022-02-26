package org.dbs24.email.spammer.repo;

import org.dbs24.email.spammer.entity.domain.Spammer;
import org.dbs24.spring.core.data.ApplicationJpaRepository;

import java.util.Optional;

public interface SpammerRepository extends ApplicationJpaRepository<Spammer, Integer> {

    Optional<Spammer> findBySpammerEmail(String spammerEmail);
}
