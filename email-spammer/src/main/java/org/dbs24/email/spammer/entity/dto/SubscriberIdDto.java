package org.dbs24.email.spammer.entity.dto;

import lombok.Data;
import org.dbs24.email.spammer.entity.domain.Subscriber;
import org.dbs24.stmt.StmtProcessor;

@Data
public class SubscriberIdDto {

    private Integer subscriberId;

    public static SubscriberIdDto of(Subscriber subscriber) {

        return StmtProcessor.create(
                SubscriberIdDto.class,
                SubscriberIdDto -> SubscriberIdDto.setSubscriberId(subscriber.getSubscriberId())
        );
    }
}
