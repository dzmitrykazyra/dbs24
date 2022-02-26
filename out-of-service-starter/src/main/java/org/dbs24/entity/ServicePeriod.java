package org.dbs24.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "core_out_of_service")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ServicePeriod extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @Column(name = "service_date_start")
    private LocalDateTime serviceDateStart;

    @Column(name = "service_date_finish")
    private LocalDateTime serviceDateFinish;

    @Column(name = "note")
    private String note;

}
