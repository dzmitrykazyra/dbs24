/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.rapid;

import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.application.core.sysconst.SysConst;
import org.dbs24.entity.core.AbstractActionEntity;
import javax.persistence.*;
import lombok.Data;
import org.dbs24.entity.AbstractGame;
import org.dbs24.consts.WorldChessConst;
import org.dbs24.entity.core.api.ActionClassesPackages;
import org.dbs24.entity.core.api.DefaultEntityStatus;
import org.dbs24.entity.core.api.EntityKindId;
import org.dbs24.entity.core.api.EntityStatusesRef;
import org.dbs24.entity.core.api.EntityTypeId;
import org.dbs24.entity.status.EntityStatusId;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import org.dbs24.entity.AbstractPlayer;
import org.dbs24.entity.Game;
import org.dbs24.entity.GameAction;

/**
 *
 * @author Козыро Дмитрий
 */

@Entity
@Data
@EntityTypeId(entity_type_id = WorldChessConst.WCP_GAME,
        entity_type_name = "Шахматная партия")
@EntityKindId(entity_kind_id = WorldChessConst.WCP_CLASSIC_GAME,
        entity_type_id = WorldChessConst.WCP_GAME,
        entity_kind_name = "Классическая шахматная партия")
@EntityStatusesRef(
        entiy_status = {
            @EntityStatusId(
                    entity_type_id = WorldChessConst.WCP_GAME,
                    entity_status_id = SysConst.ES_VALID,
                    entity_status_name = "Действующая партия")
            ,
            @EntityStatusId(
                    entity_type_id = WorldChessConst.WCP_GAME,
                    entity_status_id = SysConst.ES_CLOSED,
                    entity_status_name = "Завершенная партия")
            ,
            @EntityStatusId(
                    entity_type_id = WorldChessConst.WCP_GAME,
                    entity_status_id = SysConst.ES_CANCELLED,
                    entity_status_name = "Отложенная партия")
        })
@DefaultEntityStatus(entity_status = SysConst.ES_VALID)
@ActionClassesPackages(pkgList = {SysConst.ACTIONS_PACKAGE})
public class RapidGame extends AbstractGame {

//    @ManyToOne
//    @JoinColumn(name = "white_player_id", referencedColumnName = "player_id")
//    private AbstractChessPlayer whitePlayer;
//
//    @ManyToOne
//    @JoinColumn(name = "black_player_id", referencedColumnName = "player_id")
//    private AbstractChessPlayer blackPlayer;
//
//    @Column(name = "white_rating")
//    @NotNull
//    private BigDecimal whiteRating;
//
//    @Column(name = "black_rating")
//    @NotNull
//    private BigDecimal blackRating;
//
//    @Column(name = "game_start_date")
//    @NotNull
//    private LocalDateTime gameStartDate;
//
//    @Column(name = "game_finish_date")
//    @NotNull
//    private LocalDateTime gameFinishDate;
//
//    @Column(name = "white_player_points")
//    @NotNull
//    private BigDecimal whitePlayerPoints;
//
//    @Column(name = "black_player_points")
//    @NotNull
//    private BigDecimal blackPlayerPoints;
//    
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "classicChessGame")
//    private Collection<GameAction> gameMoves = ServiceFuncs.<GameAction>createCollection(); 
}
