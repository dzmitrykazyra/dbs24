package org.dbs24.auth.server.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Table(name = "tkn_servers_events")
@IdClass(EventPK.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Event extends ObjectRoot implements PersistenceEntity {

    @Id
    @Column(name = "server_id", updatable = false)
    @EqualsAndHashCode.Include
    private Integer serverId;

    @Id
    @Column(name = "server_event_date", updatable = false)
    @EqualsAndHashCode.Include
    private LocalDateTime serverEventDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "server_event_id", referencedColumnName = "server_event_id", updatable = false)
    private ServerEvent serverEvent;

    @Column(name = "note", updatable = false)
    private String note;
}
