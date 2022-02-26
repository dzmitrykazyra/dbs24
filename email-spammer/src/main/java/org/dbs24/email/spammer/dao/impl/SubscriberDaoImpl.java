package org.dbs24.email.spammer.dao.impl;

import lombok.extern.log4j.Log4j2;
import org.dbs24.email.spammer.dao.SubscriberDao;
import org.dbs24.email.spammer.entity.domain.Subscriber;
import org.dbs24.email.spammer.repo.SubscriberRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
public class SubscriberDaoImpl implements SubscriberDao {

    private final SubscriberRepository subscriberRepository;

    public SubscriberDaoImpl(SubscriberRepository subscriberRepository) {

        this.subscriberRepository = subscriberRepository;
    }

    @Override
    public Subscriber save(Subscriber subscriberToSave) {

        return subscriberRepository.save(subscriberToSave);
    }

    @Override
    public List<Subscriber> findEnabledSpamSubscribers() {

        return subscriberRepository.findByIsSpamEnabled(Boolean.TRUE);
    }
}
