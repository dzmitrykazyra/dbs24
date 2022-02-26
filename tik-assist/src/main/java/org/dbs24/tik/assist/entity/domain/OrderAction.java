/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.entity.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tik_order_actions")
public class OrderAction extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tik_order_actions")
    @SequenceGenerator(name = "seq_tik_order_actions", sequenceName = "seq_tik_order_actions", allocationSize = 1)
    @NotNull
    @Column(name = "order_action_id", updatable = false)
    private Integer orderActionId;

    @ManyToOne(fetch = EAGER)
    @NotNull
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private UserOrder userOrder;

    @ManyToOne(fetch = LAZY)
    @NotNull
    @JoinColumn(name = "order_action_result_id", referencedColumnName = "order_action_result_id")
    private OrderActionResult orderActionResult;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "finish_date")
    private LocalDateTime finishDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "bot_id", referencedColumnName = "bot_id")
    private Bot bot;

    @Column(name = "note")
    private String note;

    @Column(name = "error_message")
    private String errorMessage;
}
