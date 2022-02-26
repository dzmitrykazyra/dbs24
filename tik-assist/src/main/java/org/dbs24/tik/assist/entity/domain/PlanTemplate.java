package org.dbs24.tik.assist.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tik_plan_templates")
public class PlanTemplate extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tik_plan_templates")
    @SequenceGenerator(name = "seq_tik_plan_templates", sequenceName = "seq_tik_plan_templates", allocationSize = 1)
    @NotNull
    @Column(name = "plan_template_id", updatable = false)
    private Integer planTemplateId;

    @Column(name = "plan_template_name")
    private String planTemplateName;

    @NotNull
    @Column(name = "plan_template_sum")
    private BigDecimal planTemplateSum;

    @NotNull
    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "plan_template_status_id", referencedColumnName = "plan_template_status_id")
    private PlanTemplateStatus planTemplateStatus;

    @NotNull
    @Column(name = "views_amount")
    private Integer viewsAmount;

    @NotNull
    @Column(name = "likes_amount")
    private Integer likesAmount;

    @NotNull
    @Column(name = "followers_amount")
    private Integer followersAmount;

    @NotNull
    @Column(name = "comments_amount")
    private Integer commentsAmount;

    @NotNull
    @Column(name = "reposts_amount")
    private Integer repostsAmount;
}