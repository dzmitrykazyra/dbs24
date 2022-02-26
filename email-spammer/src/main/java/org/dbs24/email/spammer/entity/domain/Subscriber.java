package org.dbs24.email.spammer.entity.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.FetchType.LAZY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "spm_subscribers")
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_spm_subscribers")
    @SequenceGenerator(name = "seq_spm_subscribers", sequenceName = "seq_spm_subscribers", allocationSize = 1)
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "subscriber_id")
    private Integer subscriberId;

    @NotNull
    @Column(name = "subscriber_email")
    private String subscriberEmail;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "application_id", referencedColumnName = "application_id")
    private Application application;

    @NotNull
    @Column(name = "is_enabled")
    private Boolean isSpamEnabled;
}
