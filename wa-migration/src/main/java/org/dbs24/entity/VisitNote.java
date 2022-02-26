/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity;

import java.time.LocalDateTime;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

@Data
@Entity
@Table(name = "visitNote")
public class VisitNote extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_visitnote")
    @SequenceGenerator(name = "seq_visitnote", sequenceName = "seq_visitnote", allocationSize = 1)
    @Column(name = "id", updatable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phone_id", referencedColumnName = "id")
    private SubscriptionPhone subscriptionPhone;

    @NotNull
    @Column(name = "IS_ONLINE")
    private Integer isOnline;

    @NotNull
    @Column(name = "ADD_TIME")
    private LocalDateTime addTime;

}
