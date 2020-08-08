/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity;

/**
 *
 * @author Козыро Дмитрий
 */
import java.io.Serializable;
import lombok.Data;
import org.dbs24.references.ChessEngine;

@Data
public class MoveEstimationPK implements Serializable {

    private GameAction gameMove;
    private ChessEngine chessEngine;

}
