/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.entity.domain;

import java.time.LocalDateTime;
import javax.persistence.*;
import static javax.persistence.FetchType.LAZY;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

@Data
@Entity
@Table(name = "tik_phone_usages")
public class PhoneUsage extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tik_phone_usages")
    @SequenceGenerator(name = "seq_tik_phone_usages", sequenceName = "seq_tik_phone_usages", allocationSize = 1)
    @NotNull
    @Column(name = "phone_usage_id", updatable = false)
    private Integer phoneUsageId;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "phone_id", referencedColumnName = "phone_id")
    private Phone phone;

    @NotNull
    @Column(name = "actual_date", updatable = false)
    private LocalDateTime actualDate;

    @NotNull
    @Column(name = "is_success")
    private Boolean isSuccess;

    @Column(name = "error_message")
    private String errorMessage;
}
