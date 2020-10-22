/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity;

import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;
import org.dbs24.references.ChessEngine;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@Entity
@Table(name = "wc_MoveEstimations")
@IdClass(MoveEstimationPK.class)
public class MoveEstimation extends ObjectRoot implements PersistenceEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "move_id")
    private GameAction gameMove;
    @Id
    @ManyToOne
    @JoinColumn(name = "engine_id")
    private ChessEngine chessEngine;
    
    @Column(name = "estimation")
    @NotNull
    private BigDecimal estimation;    
}
