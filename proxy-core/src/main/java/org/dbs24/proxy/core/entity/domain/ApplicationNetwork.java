package org.dbs24.proxy.core.entity.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "prx_application_networks")
public class ApplicationNetwork extends ObjectRoot implements PersistenceEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_prx_application_network")
    @SequenceGenerator(name = "seq_prx_application_network", sequenceName = "seq_prx_application_network", allocationSize = 1)
    @NotNull
    @Column(name = "application_network_id", updatable = false)
    private Integer applicationNetworkId;

    @NotNull
    @Column(name = "name")
    private String applicationNetworkName;
}