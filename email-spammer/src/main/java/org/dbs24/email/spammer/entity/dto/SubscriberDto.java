package org.dbs24.email.spammer.entity.dto;

import lombok.Data;
import org.dbs24.email.spammer.entity.domain.Application;
import org.dbs24.email.spammer.entity.domain.Subscriber;

@Data
public class SubscriberDto {

    private String subscriberEmail;
    private Integer applicationId;
    private Boolean isEnabled;

    /** Method creates subscriber entity with null id and application */
    public Subscriber toSubscriber() {

        return Subscriber.builder()
                .subscriberEmail(this.getSubscriberEmail())
                .isSpamEnabled(this.getIsEnabled())
                .build();
    }

    /** Method creates subscriber entity with null id */
    public Subscriber toSubscriberByApplication(Application application) {

        return Subscriber.builder()
                .application(application)
                .subscriberEmail(this.getSubscriberEmail())
                .isSpamEnabled(this.getIsEnabled())
                .build();
    }
}
