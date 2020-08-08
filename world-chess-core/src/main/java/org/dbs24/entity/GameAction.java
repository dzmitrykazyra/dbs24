/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity;

import org.dbs24.entity.classic.ClassicGame;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.persistence.api.PersistenceEntity;
import org.dbs24.references.MoveNotice;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@Entity
@Table(name = "wc_GameActions")
public class GameAction extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_wc_GameActions")
    @SequenceGenerator(name = "seq_wc_GameActions", sequenceName = "seq_wc_GameActions", allocationSize = 1)
    @Column(name = "game_action_id", updatable = false)
    private Long moveId;

    @OneToOne(fetch = FetchType.LAZY)
    @NotNull
    @JsonIgnore    
    @JoinColumn(name = "chess_game_id", referencedColumnName = "chess_game_id")
    private ClassicGame classicChessGame;

    @OneToOne
    @JoinColumn(name = "parent_move_id", referencedColumnName = "move_id")
    private GameAction parentMove;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "notice_id", referencedColumnName = "notice_id")
    private MoveNotice moveNotice;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gameMove")
    private Collection<CheckerBoardAction> checkerBoardActions = ServiceFuncs.<CheckerBoardAction>createCollection();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gameMove")
    private Collection<MoveEstimation> moveEstimations = ServiceFuncs.<MoveEstimation>createCollection();

}
