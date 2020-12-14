/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.entity.core.AbstractActionEntity;

/**
 *
 * @author N76VB
 */
@Entity
@Table(name = "chess_Games")
@PrimaryKeyJoinColumn(name = "chess_game_id", referencedColumnName = "entity_id")
@Data
public abstract class AbstractGame extends AbstractActionEntity implements Game {

    @ManyToOne
    @JoinColumn(name = "white_player_id", referencedColumnName = "player_id")
    private AbstractPlayer whitePlayer;

    @ManyToOne
    @JoinColumn(name = "black_player_id", referencedColumnName = "player_id")
    private AbstractPlayer blackPlayer;

    @Column(name = "white_rating")
    @NotNull
    private BigDecimal whiteRating;

    @Column(name = "black_rating")
    @NotNull
    private BigDecimal blackRating;

    @Column(name = "game_start_date")
    @NotNull
    private LocalDateTime gameStartDate;

    @Column(name = "game_finish_date")
    @NotNull
    private LocalDateTime gameFinishDate;

    @Column(name = "white_player_points")
    @NotNull
    private BigDecimal whitePlayerPoints;

    @Column(name = "black_player_points")
    @NotNull
    private BigDecimal blackPlayerPoints;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "classicChessGame")
    private Collection<GameAction> gameMoves = ServiceFuncs.<GameAction>createCollection();
}
