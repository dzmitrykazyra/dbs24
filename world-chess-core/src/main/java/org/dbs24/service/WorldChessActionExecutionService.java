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
import org.dbs24.config.WorldChessConfig;
import org.dbs24.references.*;
import org.dbs24.entity.*;
import org.dbs24.entity.classic.*;
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
@Import({WorldChessConfig.class})
public class WorldChessActionExecutionService<P extends Player, G extends Game> extends ActionExecutionService {

    public P createPlayer(
            final String lastName,
            final String firstName,
            final Boolean isBlocked,
            final Integer totalGames,
            final Integer whiteWins,
            final Integer blackWins,
            final Integer whiteLosts,
            final Integer blackLosts) {

        return (P) this.<AbstractPlayer>createActionEntity(AbstractPlayer.class,
                (abstractChessPlayer) -> {

                    //retailLoanContract.setCreation_date(LocalDateTime.now());
                    abstractChessPlayer.setEntityStatus(EntityStatus.findEntityStatus(WorldChessConst.WCP_PLAYER, SysConst.ES_VALID));
                    abstractChessPlayer.setFirstName(firstName);
                    abstractChessPlayer.setLastName(lastName);
                    abstractChessPlayer.setTotalGames(totalGames);
                    abstractChessPlayer.setWhiteWins(whiteWins);
                    abstractChessPlayer.setWhiteLosts(whiteLosts);
                    abstractChessPlayer.setBlackWins(blackWins);
                    abstractChessPlayer.setBlackLosts(blackLosts);
                    abstractChessPlayer.setIsBlocked(isBlocked);
                    //EntityStatus.getExistEntityStatus(TariffConst.ENTITY_TARIFF_PLAN, 0));
                    // построить графики погашения
                    //retailLoanContract.createBondschedules();
                });

    }
//
//    //==========================================================================

    public G createGame(final P chessPlayer1,
            final P chessPlayer2,
            final BigDecimal whiteRating,
            final BigDecimal blackRating,
            final LocalDateTime gameStartDate,
            final LocalDateTime gameFinishDate,
            final BigDecimal whitePlayerPoints, final BigDecimal blackPlayerPoints) {
        return (G) this.<ClassicGame>createActionEntity(ClassicGame.class,
                (abstractChessGame) -> {

                    //retailLoanContract.setCreation_date(LocalDateTime.now());
                    abstractChessGame.setEntityStatus(EntityStatus.findEntityStatus(WorldChessConst.WCP_GAME, SysConst.ES_VALID));
                    abstractChessGame.setWhitePlayer((AbstractPlayer) chessPlayer1);
                    abstractChessGame.setBlackPlayer((AbstractPlayer) chessPlayer2);
                    abstractChessGame.setWhitePlayerPoints(whitePlayerPoints);
                    abstractChessGame.setWhiteRating(whiteRating);
                    abstractChessGame.setBlackRating(blackRating);
                    abstractChessGame.setBlackPlayerPoints(blackPlayerPoints);
                    abstractChessGame.setGameStartDate(gameStartDate);
                    abstractChessGame.setGameFinishDate(gameFinishDate);

                    //EntityStatus.getExistEntityStatus(TariffConst.ENTITY_TARIFF_PLAN, 0));
                    // построить графики погашения
                    //retailLoanContract.createBondschedules();
                });
    }

//    //==========================================================================
//    public GameAction createGameMove(
//            final ChessGame chessGame, 
//            final ChessPlayer chessPlayer,
//            final MoveNotice moveNotice) {
//        return this.<GameAction>createPersistenceEntity(GameAction.class,
//                (move) -> {
//
//                    //retailLoanContract.setCreation_date(LocalDateTime.now());
//                    move.setClassicGame((ClassicGame)chessGame);
//                    move.setMoveNotice(moveNotice);
//
//
//                    //EntityStatus.getExistEntityStatus(TariffConst.ENTITY_TARIFF_PLAN, 0));
//                    // построить графики погашения
//                    //retailLoanContract.createBondschedules();
//                });
//    }
}
