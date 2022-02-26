package org.dbs24.tik.assist.entity.domain;

import lombok.Data;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Table(name = "tik_plan_templates")
public class PlanTemplateHist extends ObjectRoot implements PersistenceEntity {

    @Id
    @NotNull
    @Column(name = "plan_template_id", updatable = false)
    private Integer planTemplateId;

    @Column(name = "plan_template_name")
    private String planTemplateName;

    @Column(name = "plan_template_sum")
    private BigDecimal planTemplateSum;

    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @Column(name = "plan_template_status_id")
    private Integer planTemplateStatusId;

    @Column(name = "views_amount")
    private Integer viewsAmount;

    @Column(name = "likes_amount")
    private Integer likesAmount;

    @Column(name = "followers_amount")
    private Integer followersAmount;

    @NotNull
    @Column(name = "comments_amount")
    private Integer commentsAmount;

    @NotNull
    @Column(name = "reposts_amount")
    private Integer repostsAmount;
}