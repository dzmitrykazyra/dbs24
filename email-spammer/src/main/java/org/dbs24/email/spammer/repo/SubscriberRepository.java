package org.dbs24.email.spammer.repo;

import org.dbs24.email.spammer.entity.domain.Subscriber;
import org.dbs24.spring.core.data.ApplicationJpaRepository;

import java.util.List;

public interface SubscriberRepository extends ApplicationJpaRepository<Subscriber, Integer> {

    List<Subscriber> findByIsSpamEnabled(Boolean isSpamEnabled);
}
