/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.btitz;

import javax.persistence.Entity;
import lombok.Data;
import static org.dbs24.application.core.sysconst.SysConst.*;
import org.dbs24.consts.WorldChessConst;
import org.dbs24.entity.AbstractGame;
import org.dbs24.entity.core.api.ActionClassesPackages;
import org.dbs24.entity.core.api.DefaultEntityStatus;
import org.dbs24.entity.core.api.EntityKindId;
import org.dbs24.entity.core.api.EntityStatusesRef;
import org.dbs24.entity.core.api.EntityTypeId;

/**
 *
 * @author N76VB
 */

import org.dbs24.application.core.service.funcs.ServiceFuncs;
import static org.dbs24.application.core.sysconst.SysConst.*;
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
@EntityKindId(entity_kind_id = WorldChessConst.WCP_BLITZ_GAME,
        entity_type_id = WorldChessConst.WCP_GAME,
        entity_kind_name = "Партия в блиц-шахматы")
@EntityStatusesRef(
        entiy_status = {
            @EntityStatusId(
                    entity_type_id = WorldChessConst.WCP_GAME,
                    entity_status_id = ES_VALID,
                    entity_status_name = "Действующая партия")
            ,
            @EntityStatusId(
                    entity_type_id = WorldChessConst.WCP_GAME,
                    entity_status_id = ES_CLOSED,
                    entity_status_name = "Завершенная партия")
            ,
            @EntityStatusId(
                    entity_type_id = WorldChessConst.WCP_GAME,
                    entity_status_id = ES_CANCELLED,
                    entity_status_name = "Отложенная партия")
        })
@DefaultEntityStatus(entity_status = ES_VALID)
@ActionClassesPackages(pkgList = {ACTIONS_PACKAGE})
public class BlitzGame extends AbstractGame {

}

