package org.dbs24.tik.assist.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tik_promocodes")
public class Promocode extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tik_promocodes")
    @SequenceGenerator(name = "seq_tik_promocodes", sequenceName = "seq_tik_promocodes", allocationSize = 1)
    @NotNull
    @Column(name = "promocode_id", updatable = false)
    private Integer promocodeId;

    @NotNull
    @Column(name = "promocode_value")
    private String promocodeValue;

    @NotNull
    @Column(name = "promocode_discount")
    private Integer promocodeDiscount;

    @NotNull
    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "begin_date")
    private LocalDateTime beginDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "actual_date")
    private LocalDateTime actualDate;

}