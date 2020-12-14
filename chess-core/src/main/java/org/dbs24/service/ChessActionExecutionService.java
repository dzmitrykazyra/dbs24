/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import static org.dbs24.consts.SysConst.*;
import static org.dbs24.consts.EntityConst.*;
import static org.dbs24.consts.WorldChessConst.*;
import org.dbs24.entity.core.api.EntityClassesPackages;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.dbs24.entity.*;
import org.dbs24.entity.classic.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Service
@EntityClassesPackages(pkgList = {ENTITY_PACKAGE})
public class ChessActionExecutionService<P extends Player, G extends Game> extends AbstractActionExecutionService {

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

                    //retailLoanContract.setCreationDate(LocalDateTime.now());
                    abstractChessPlayer.setEntityStatus(this.getEntityReferencesService().findEntityStatus(WCP_PLAYER, ES_ACTUAL));
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

    public G createGame(P chessPlayer1,
            final P chessPlayer2,
            final BigDecimal whiteRating,
            final BigDecimal blackRating,
            final LocalDateTime gameStartDate,
            final LocalDateTime gameFinishDate,
            final BigDecimal whitePlayerPoints, BigDecimal blackPlayerPoints) {
        return (G) this.<ClassicGame>createActionEntity(ClassicGame.class,
                (abstractChessGame) -> {

                    //retailLoanContract.setCreationDate(LocalDateTime.now());
                    abstractChessGame.setEntityStatus(this.getEntityReferencesService().findEntityStatus(WCP_GAME, ES_ACTUAL));
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
//                    //retailLoanContract.setCreationDate(LocalDateTime.now());
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
