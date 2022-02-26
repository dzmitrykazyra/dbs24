package org.dbs24.email.spammer.service;

import lombok.extern.log4j.Log4j2;
import org.dbs24.email.spammer.dao.ReferenceDao;
import org.dbs24.email.spammer.dao.SubscriberDao;
import org.dbs24.email.spammer.entity.domain.Subscriber;
import org.dbs24.email.spammer.entity.dto.SubscriberDto;
import org.dbs24.email.spammer.entity.dto.SubscriberIdDto;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class SubscriberService {

    private final SubscriberDao subscriberDao;
    private final ReferenceDao referenceDao;

    public SubscriberService(SubscriberDao subscriberDao, ReferenceDao referenceDao) {

        this.subscriberDao = subscriberDao;
        this.referenceDao = referenceDao;
    }

    public Mono<SubscriberIdDto> createSubscriber(Mono<SubscriberDto> subscriberDtoMono) {

        return subscriberDtoMono.map(
                subscriberDto -> SubscriberIdDto.of(
                        subscriberDao.save(
                                subscriberDto.toSubscriberByApplication(
                                        referenceDao.findApplicationById(subscriberDto.getApplicationId())
                                )
                        )
                )
        );
    }
}
