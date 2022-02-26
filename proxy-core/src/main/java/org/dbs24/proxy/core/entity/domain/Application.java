package org.dbs24.proxy.core.entity.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.FetchType.EAGER;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "prx_applications")
public class Application extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_prx_application")
    @SequenceGenerator(name = "seq_prx_application", sequenceName = "seq_prx_application", allocationSize = 1)
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "application_id", updatable = false)
    private Integer applicationId;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = EAGER)
    @NotNull
    @JoinColumn(name = "application_network_id", referencedColumnName = "application_network_id")
    private ApplicationNetwork applicationNetwork;

    @ManyToOne(fetch = EAGER)
    @NotNull
    @JoinColumn(name = "application_status_id", referencedColumnName = "application_status_id")
    private ApplicationStatus applicationStatus;
}