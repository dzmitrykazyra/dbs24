/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity;

import org.dbs24.application.core.sysconst.SysConst;
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
import org.dbs24.consts.WorldChessConst;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@Entity
@Table(name = "chess_Players")
@PrimaryKeyJoinColumn(name = "player_id", referencedColumnName = "entity_id")
@EntityTypeId(entity_type_id = WorldChessConst.WCP_PLAYER,
        entity_type_name = "Игрок в шахматы")
@EntityKindId(entity_kind_id = WorldChessConst.WCP_PLAYER_ODINARY,
        entity_type_id = WorldChessConst.WCP_PLAYER,
        entity_kind_name = "Рядовой пользователь")
@EntityStatusesRef(
        entiy_status = {
            @EntityStatusId(
                    entity_type_id = WorldChessConst.WCP_PLAYER,
                    entity_status_id = SysConst.ES_VALID,
                    entity_status_name = "Действующая игрок")
            ,
            @EntityStatusId(
                    entity_type_id = WorldChessConst.WCP_PLAYER,
                    entity_status_id = SysConst.ES_CLOSED,
                    entity_status_name = "Закрытый аккаунт")
            ,
            @EntityStatusId(
                    entity_type_id = WorldChessConst.WCP_PLAYER,
                    entity_status_id = SysConst.ES_CANCELLED,
                    entity_status_name = "Заблокированный игрок")
        })
@DefaultEntityStatus(entity_status = SysConst.ES_VALID)
@ActionClassesPackages(pkgList = {SysConst.ACTIONS_PACKAGE})
public class AbstractChessPlayer extends AbstractActionEntity implements Player {

    @Column(name = "last_name")
    @NotNull
    private String lastName;

    @Column(name = "first_name")
    @NotNull
    private String firstName;

    @Column(name = "current_rating")
    @NotNull
    private BigDecimal currentRating;

    @Column(name = "is_blocked")
    @NotNull
    private Boolean isBlocked;

    @Column(name = "total_games")
    @NotNull
    private Integer totalGames;

    @Column(name = "wins")
    @NotNull
    private Integer wins;

    @Column(name = "losts")
    @NotNull
    private Integer losts;
}
