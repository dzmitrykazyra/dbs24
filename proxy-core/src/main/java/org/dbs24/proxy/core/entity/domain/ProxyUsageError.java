package org.dbs24.proxy.core.entity.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "prx_proxy_usage_errors")
public class ProxyUsageError extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_prx_usage_error")
    @SequenceGenerator(name = "seq_prx_usage_error", sequenceName = "seq_prx_usage_error", allocationSize = 1)
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "error_id", updatable = false)
    private Integer errorId;

    @OneToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "usage_id", referencedColumnName = "usage_id")
    private ProxyUsage proxyUsage;

    @Column(name = "actual_date")
    @NotNull
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "error_type_id", referencedColumnName = "error_type_id")
    private ErrorType errorType;

    @Column(name = "log")
    private String log;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "application_id", referencedColumnName = "application_id")
    private Application application;
}
