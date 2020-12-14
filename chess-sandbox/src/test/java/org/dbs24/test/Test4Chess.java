/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.test;

import org.dbs24.application.core.service.funcs.TestFuncs;
import org.dbs24.entity.*;
import org.dbs24.entity.classic.*;
import org.dbs24.references.*;
import org.dbs24.test.core.AbstractWebTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.dbs24.service.ChessActionExecutionService;
import java.math.BigDecimal;
//import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class Test4Chess<P extends Player, G extends Game> extends AbstractWebTest {

    @Autowired
    private ChessActionExecutionService worldChessActionExecutionService;

    protected P generateTestPlayer() {

        final String testString = TestFuncs.generateTestString20();

        return (P) worldChessActionExecutionService.createPlayer(testString, testString, Boolean.FALSE, Integer.BYTES, Integer.BYTES, Integer.BYTES, Integer.BYTES, Integer.BYTES);
    }

    //==========================================================================
    protected G generateTestGame( P chessPlayer1, P chessPlayer2) {

        //final String testString = TestFuncs.generateTestString20();
        return (G) worldChessActionExecutionService.createGame(chessPlayer1, chessPlayer2, BigDecimal.TEN, BigDecimal.TEN, LocalDateTime.now(), LocalDateTime.now(), BigDecimal.ZERO, BigDecimal.ZERO);
    }

    //==========================================================================
//    protected GameMove generateTestMove(
//            final ClassicGame chessGame,
//            final ChessPlayer chessPlayer,
//            final MoveNotice moveNotice) {
//
//        //final String testString = TestFuncs.generateTestString20();
//        final GameMove gameMove = worldChessActionExecutionService.createGameMove(
//                chessGame,
//                chessPlayer,
//                moveNotice);
//        
//        chessGame.getGameMoves().add(gameMove);
//        
//        return gameMove;
//        
//    }
}
