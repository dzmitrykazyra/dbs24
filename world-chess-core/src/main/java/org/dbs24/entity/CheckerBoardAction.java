/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity;

import org.dbs24.application.core.api.ObjectRoot;
import org.dbs24.persistence.api.PersistenceEntity;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.dbs24.references.Piece;
import org.dbs24.references.CheckerBoard;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@Entity
@Table(name = "wc_Checkerboard_Actions")
public class CheckerBoardAction extends ObjectRoot implements PersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sec_chess_Checkerboard_Actions")
    @SequenceGenerator(name = "sec_chess_Checkerboard_Actions", sequenceName = "sec_chess_Checkerboard_Actions", allocationSize = 1)
    @Column(name = "move_action_id", updatable = false)
    private Long moveActionId;
    
    @ManyToOne
    @NotNull
    @JoinColumn(name = "move_id", referencedColumnName = "move_id")    
    private GameAction gameMove;
    
    @ManyToOne
    @NotNull
    @JoinColumn(name = "piece_code", referencedColumnName = "piece_code") 
    private Piece piece;
    
    @ManyToOne
    @NotNull
    @JoinColumn(name = "checkerboard_code", referencedColumnName = "checkerboard_code") 
    private CheckerBoard checkerBoard;
    
    @Column(name = "move_direction", updatable = false)
    private Boolean moveDirection;
    
    @Column(name = "is_white", updatable = false)
    private Boolean isWhite;    
    
}
