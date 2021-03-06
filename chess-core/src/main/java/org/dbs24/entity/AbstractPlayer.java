/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity;

import static org.dbs24.consts.SysConst.*;
import static org.dbs24.consts.EntityConst.*;
import static org.dbs24.consts.WorldChessConst.*;
import org.dbs24.entity.core.AbstractActionEntity;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import lombok.Data;
import org.dbs24.entity.core.api.ActionClassesPackages;
import org.dbs24.entity.core.api.DefaultEntityStatus;
import org.dbs24.entity.core.api.EntityKindId;
import org.dbs24.entity.core.api.EntityStatusesRef;
import org.dbs24.entity.core.api.EntityTypeId;
import org.dbs24.entity.status.EntityStatusId;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "chess_Players")
@PrimaryKeyJoinColumn(name = "player_id", referencedColumnName = "entity_id")
@EntityTypeId(entity_type_id = WCP_PLAYER,
        entity_type_name = "Игрок в шахматы")
@EntityKindId(entity_kind_id = WCP_PLAYER_ODINARY,
        entity_type_id = WCP_PLAYER,
        entity_kind_name = "Рядовой пользователь")
@EntityStatusesRef(
        entiy_status = {
            @EntityStatusId(
                    entity_type_id = WCP_PLAYER,
                    entity_status_id = ES_ACTUAL,
                    entity_status_name = "Действующая игрок")
            ,
            @EntityStatusId(
                    entity_type_id = WCP_PLAYER,
                    entity_status_id = ES_CLOSED,
                    entity_status_name = "Закрытый аккаунт")
            ,
            @EntityStatusId(
                    entity_type_id = WCP_PLAYER,
                    entity_status_id = ES_CANCELLED,
                    entity_status_name = "Заблокированный игрок")
        })
@DefaultEntityStatus(entity_status = ES_ACTUAL)
@ActionClassesPackages(pkgList = {ACTIONS_PACKAGE})
public class AbstractPlayer extends AbstractActionEntity implements Player {

    @Column(name = "last_name")
    @NotNull
    private String lastName;

    @Column(name = "first_name")
    @NotNull
    private String firstName;

    @Column(name = "is_blocked")
    @NotNull
    private Boolean isBlocked;

    @Column(name = "total_games")
    @NotNull
    private Integer totalGames;

    @Column(name = "white_wins")
    @NotNull
    private Integer whiteWins;

    @Column(name = "black_wins")
    @NotNull
    private Integer blackWins;
    
    @Column(name = "white_losts")
    @NotNull
    private Integer whiteLosts;

    @Column(name = "black_losts")
    @NotNull
    private Integer blackLosts;

}
