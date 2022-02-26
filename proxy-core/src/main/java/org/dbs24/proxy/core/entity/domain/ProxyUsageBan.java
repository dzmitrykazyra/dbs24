package org.dbs24.proxy.core.entity.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@IdClass(ProxyUsageBanPK.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "prx_proxy_usage_bans")
public class ProxyUsageBan extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "proxy_usage_error_id", updatable = false)
    private Integer proxyUsageErrorId;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "application_network_id", referencedColumnName = "application_network_id")
    private ApplicationNetwork applicationNetwork;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "proxy_id", referencedColumnName = "proxy_id")
    private Proxy proxy;

    @Column(name = "banned_until_date")
    private LocalDateTime bannedUntilDate;
}


@Data
class ProxyUsageBanPK implements Serializable {

    private Integer proxyUsageErrorId;
}