package org.dbs24.email.spammer.dao;

import org.dbs24.email.spammer.entity.domain.Subscriber;

import java.util.List;

public interface SubscriberDao {

    Subscriber save(Subscriber subscriberToSave);

    List<Subscriber> findEnabledSpamSubscribers();
}
