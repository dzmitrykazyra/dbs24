/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import org.dbs24.application.core.sysconst.SysConst;
import org.dbs24.entity.core.api.CachedReferencesClasses;
import org.dbs24.entity.core.api.EntityClassesPackages;
import lombok.Data;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
//import org.dbs24.config.WorldChessConfig;
import org.dbs24.references.*;
//import org.dbs24.chess.api.*;
import org.dbs24.entity.*;
import org.dbs24.entity.status.EntityStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.dbs24.consts.WorldChessConst;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@Service
@EntityClassesPackages(pkgList = {SysConst.ENTITY_PACKAGE})
@CachedReferencesClasses(classes = {Piece.class, MoveNotice.class, CheckerBoard.class, ChessEngine.class})
//@Import({WorldChessConfig.class})
public class WorldChessActionExecutionService extends ActionExecutionService {

//    public ChessPlayer createPlayer(
//            final String lastName,
//            final String firstName,
//            final BigDecimal currentRating,
//            final Boolean isBlocked,
//            final Integer totalGames,
//            final Integer wins,
//            final Integer losts) {
//
//        return this.<AbstractChessPlayer>createActionEntity(AbstractChessPlayer.class,
//                (abstractChessPlayer) -> {
//
//                    //retailLoanContract.setCreation_date(LocalDateTime.now());
//                    abstractChessPlayer.setEntityStatus(EntityStatus.findEntityStatus(WorldChessConst.WCP_PLAYER, SysConst.ES_VALID));
//                    abstractChessPlayer.setFirstName(firstName);
//                    abstractChessPlayer.setLastName(lastName);
//                    abstractChessPlayer.setCurrentRating(currentRating);
//                    abstractChessPlayer.setTotalGames(totalGames);
//                    abstractChessPlayer.setLosts(losts);
//                    abstractChessPlayer.setWins(wins);
//                    abstractChessPlayer.setIsBlocked(isBlocked);
//                    //EntityStatus.getExistEntityStatus(TariffConst.ENTITY_TARIFF_PLAN, 0));
//                    // построить графики погашения
//                    //retailLoanContract.createBondschedules();
//                });
//
//    }
//
//    //==========================================================================
//    public ClassicChessGame createGame(final ChessPlayer chessPlayer1,
//            final ChessPlayer chessPlayer2,
//            final BigDecimal whiteRating,
//            final BigDecimal blackRating,
//            final LocalDateTime gameStartDate,
//            final LocalDateTime gameFinishDate,
//            final BigDecimal whitePlayerPoints, final BigDecimal blackPlayerPoints) {
//        return this.<ClassicChessGame>createActionEntity(ClassicChessGame.class,
//                (abstractChessGame) -> {
//
//                    //retailLoanContract.setCreation_date(LocalDateTime.now());
//                    abstractChessGame.setEntityStatus(EntityStatus.findEntityStatus(WorldChessConst.WCP_GAME, SysConst.ES_VALID));
//                    abstractChessGame.setWhitePlayer((AbstractChessPlayer) chessPlayer1);
//                    abstractChessGame.setBlackPlayer((AbstractChessPlayer) chessPlayer2);
//                    abstractChessGame.setWhitePlayerPoints(whitePlayerPoints);
//                    abstractChessGame.setWhiteRating(whiteRating);
//                    abstractChessGame.setBlackRating(blackRating);
//                    abstractChessGame.setBlackPlayerPoints(blackPlayerPoints);
//                    abstractChessGame.setGameStartDate(gameStartDate);
//                    abstractChessGame.setGameFinishDate(gameFinishDate);
//
//                    //EntityStatus.getExistEntityStatus(TariffConst.ENTITY_TARIFF_PLAN, 0));
//                    // построить графики погашения
//                    //retailLoanContract.createBondschedules();
//                });
//    }
//
//    //==========================================================================
//    public GameAction createGameMove(
//            final ChessGame chessGame, 
//            final ChessPlayer chessPlayer,
//            final MoveNotice moveNotice) {
//        return this.<GameAction>createPersistenceEntity(GameAction.class,
//                (move) -> {
//
//                    //retailLoanContract.setCreation_date(LocalDateTime.now());
//                    move.setClassicChessGame((ClassicChessGame)chessGame);
//                    move.setMoveNotice(moveNotice);
//
//
//                    //EntityStatus.getExistEntityStatus(TariffConst.ENTITY_TARIFF_PLAN, 0));
//                    // построить графики погашения
//                    //retailLoanContract.createBondschedules();
//                });
//    }
}
