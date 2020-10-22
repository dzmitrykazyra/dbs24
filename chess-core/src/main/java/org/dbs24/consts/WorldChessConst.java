/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.consts;

import org.dbs24.entity.classic.ClassicGame;
import org.dbs24.entity.*;
import org.dbs24.references.*;

public final class WorldChessConst {

    public static final int WCP_PLAYER = 10002; // Игрок в шахматы
    public static final int WCP_GAME = 10010; // Шахматная партия
    public static final int WCP_SHWEDISH_GAME = 10012; // Шведская шахматная партия

    public static final int WCP_PLAYER_ODINARY = 100020010; // Рядовой пользователь
    public static final int WCP_PLAYER_JUNIOR = 100020015; // гроссмейстер
    public static final int WCP_PLAYER_GROSS = 100020020; // гроссмейстер

    public static final int WCP_CLASSIC_GAME = 100100010; // Классическая шахматная партия (classic)
    public static final int WCP_RAPID_GAME = 100100020; // Быстрая шахматная партия (rapid)
    public static final int WCP_BLITZ_GAME = 100100030; // Блиц партия (bullet)
    //==========================================================================
    public static final int ACT_CREATE_OR_MODIFY_PLAYER = 1000200002; // регистрация пользователя
    public static final int ACT_AUTHORIZE_PLAYER = 1000200003; // авторизация пользователя
    public static final int ACT_CREATE_OR_MODIFY_GAME = 1001000002; // регистрация шахматной партии
    public static final int ACT_AUTHORIZE_GAME = 1001000003; // авторизация шахматной партии
    //==========================================================================
    public static final String URI_API = "/api";
    //==========================================================================
    public static final String URI_CREATE_CHESS_PLAYER = URI_API.concat("/createPlayer");
    public static final String URI_FIND_CHESS_PLAYER = URI_API.concat("/findPlayer");
    public static final String URI_EXECUTE_ACTION = URI_API.concat("/executeAction");
    public static final String URI_CREATE_CHESS_GAME = URI_API.concat("/createGame");
    public static final String URI_FIND_CHESS_GAME = URI_API.concat("/findGame");

    public static final Class<AbstractPlayer> CHESS_PLAYER_CLASS = AbstractPlayer.class;
    public static final Class<ClassicGame> CHESS_GAME_CLASS = ClassicGame.class;
    
    public static final Class<Piece> PIECE_CLASS = Piece.class;
    public static final Class<ChessEngine> CHESS_ENGINE_CLASS = ChessEngine.class;
    public static final Class<MoveNotice> MOVE_NOTICE_CLASS = MoveNotice.class;
    
}
