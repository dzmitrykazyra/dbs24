/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.dev.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;
import org.dbs24.tik.dev.entity.reference.Endpoint;
import org.dbs24.tik.dev.entity.reference.EndpointResult;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;
import static org.dbs24.tik.dev.consts.TikDevApiConsts.Databases.SEQ_GENERAL;

@Data
@Entity
@Table(name = "tda_endpoints_actions")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class EndpointAction extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQ_GENERAL)
    @SequenceGenerator(name = SEQ_GENERAL, sequenceName = SEQ_GENERAL, allocationSize = 1)
    @NotNull
    @Column(name = "endpoint_action_id", updatable = false)
    @EqualsAndHashCode.Include
    private Long endpointActionId;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "endpoint_ref_id", referencedColumnName = "endpoint_ref_id")
    private Endpoint endpoint;

    @NotNull
    @Column(name = "execution_date")
    private LocalDateTime executionDate;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "contract_id", referencedColumnName = "contract_id")
    private Contract contract;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "endpoint_result_id", referencedColumnName = "endpoint_result_id")
    private EndpointResult endpointResult;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tik_account_id", referencedColumnName = "tik_account_id")
    private TikAccount tikAccount;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "device_id", referencedColumnName = "device_id")
    private Device device;

    @NotNull
    @Column(name = "endpoint_response")
    private Integer endpointResponse;

    @Column(name = "body")
    private String body;

    @Column(name = "used_bytes")
    private Integer usedBytes;

    @Column(name = "headers")
    private String headers;

    @Column(name = "query_params")
    private String queryParams;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "err_log")
    private String errLog;

}
