package org.dbs24.tik.assist.entity.domain;

import lombok.*;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.FetchType.LAZY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "tik_tiktok_accounts")
public class TiktokAccount extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tik_tiktok_accounts")
    @SequenceGenerator(name = "seq_tik_tiktok_accounts", sequenceName = "seq_tik_tiktok_accounts", allocationSize = 1)
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "account_id", updatable = false)
    private Integer accountId;

    @NotNull
    @Column(name = "sec_user_id")
    private String secUserId;

    @Column(name = "username")
    private String accountUsername;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
}