/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     26.07.2020 18:58:36                          */
/*==============================================================*/


drop table chess_Checkerboard_Actions;

drop table chess_Games;

drop table chess_Moves;

drop table chess_Players;

drop table chess_checkerboard;

drop table chess_engines;

drop table chess_move_estimations;

drop table chess_moves_notations;

drop table chess_pieces;

drop domain TStr2;

drop sequence sec_chess_Checkerboard_Actions;

drop sequence seq_chess_Moves;

create sequence sec_chess_Checkerboard_Actions
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_chess_Moves
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

/*==============================================================*/
/* Domain: TStr2                                                */
/*==============================================================*/
create domain TStr2 as CHAR(2);

/*==============================================================*/
/* Table: chess_Checkerboard_Actions                            */
/*==============================================================*/
create table chess_Checkerboard_Actions (
   move_action_id       TIdBigCode           not null,
   move_id              TIdBigCode           not null,
   piece_code           TStr3                not null,
   checkerboard_code    TStr2                not null,
   move_direction       TBoolean             not null,
   is_white             TBoolean             not null,
   constraint PK_CHESS_CHECKERBOARD_ACTIONS primary key (move_action_id)
);

comment on table chess_Checkerboard_Actions is
'Действие над шахматной клеткой (прибытие и убытие фигур)';

/*==============================================================*/
/* Table: chess_Games                                           */
/*==============================================================*/
create table chess_Games (
   chess_game_id        TIdBigCode           not null,
   white_player_id      TIdBigCode           not null,
   black_player_id      TIdBigCode           not null,
   white_rating         TPercRate            not null,
   black_rating         TPercRate            not null,
   game_start_date      TDateTime            not null,
   game_finish_date     TDateTime            null,
   white_player_points  TMoney               null,
   black_player_points  TMoney               null,
   constraint PK_CHESS_GAMES primary key (chess_game_id),
   constraint AK_KEY_CHESS_GAME_W unique (chess_game_id, white_player_id),
   constraint AK_KEY_CHESS_GAME_B unique (chess_game_id, black_player_id)
);

comment on table chess_Games is
'Шахматные партии';

/*==============================================================*/
/* Table: chess_Moves                                           */
/*==============================================================*/
create table chess_Moves (
   move_id              TIdBigCode           not null,
   chess_game_id        TIdBigCode           not null,
   parent_move_id       TIdBigCode           null,
   notice_id            TStr3                null,
   constraint PK_CHESS_MOVES primary key (move_id)
);

comment on table chess_Moves is
'Шахматные ходы';

/*==============================================================*/
/* Table: chess_Players                                         */
/*==============================================================*/
create table chess_Players (
   player_id            TIdBigCode           not null,
   last_name            TStr100              not null,
   first_name           TStr100              not null,
   current_rating       TPercRate            not null,
   is_blocked           TBoolean             not null,
   wins                 TIdCode              not null,
   total_games          TIdCode              not null,
   losts                TIdCode              not null,
   constraint PK_CHESS_PLAYERS primary key (player_id)
);

comment on table chess_Players is
'Перечень игроков';

/*==============================================================*/
/* Table: chess_checkerboard                                    */
/*==============================================================*/
create table chess_checkerboard (
   checkerboard_code    TStr2                not null,
   checkerboard_name    TStr100              not null,
   constraint PK_CHESS_CHECKERBOARD primary key (checkerboard_code)
);

comment on table chess_checkerboard is
'Справочник адресов шахматных клеток';

/*==============================================================*/
/* Table: chess_engines                                         */
/*==============================================================*/
create table chess_engines (
   engine_id            TIdCode              not null,
   engine_name          TStr100              not null,
   constraint PK_CHESS_ENGINES primary key (engine_id)
);

comment on table chess_engines is
'Справочник шахматных движков';

/*==============================================================*/
/* Table: chess_move_estimations                                */
/*==============================================================*/
create table chess_move_estimations (
   move_id              TIdBigCode           not null,
   engine_id            TIdCode              not null,
   estimation           TPercRate            not null,
   constraint PK_CHESS_MOVE_ESTIMATIONS primary key (move_id, engine_id)
);

comment on table chess_move_estimations is
'Оценки шахматных ходов';

/*==============================================================*/
/* Table: chess_moves_notations                                 */
/*==============================================================*/
create table chess_moves_notations (
   notice_id            TStr3                not null,
   notice_name          TStr100              not null,
   constraint PK_CHESS_MOVES_NOTATIONS primary key (notice_id)
);

comment on table chess_moves_notations is
'Справочник нотаций к ходу';

/*==============================================================*/
/* Table: chess_pieces                                          */
/*==============================================================*/
create table chess_pieces (
   piece_code           TStr3                not null,
   piece_name           TStr100              not null,
   constraint PK_CHESS_PIECES primary key (piece_code)
);

comment on table chess_pieces is
'Справочник шахматных фигур';

alter table chess_Checkerboard_Actions
   add constraint FK_cp_checkboard_run foreign key (checkerboard_code)
      references chess_checkerboard (checkerboard_code)
      on delete restrict on update restrict;

alter table chess_Checkerboard_Actions
   add constraint FK_cp_checkerboard_action foreign key (move_id)
      references chess_Moves (move_id)
      on delete restrict on update restrict;

alter table chess_Checkerboard_Actions
   add constraint FK_cp_checkerboard_action_piece foreign key (piece_code)
      references chess_pieces (piece_code)
      on delete restrict on update restrict;

alter table chess_Games
   add constraint FK_cp_cg_black_player_id foreign key (black_player_id)
      references chess_Players (player_id)
      on delete restrict on update restrict;

alter table chess_Games
   add constraint FK_cp_cg_white_player_id foreign key (white_player_id)
      references chess_Players (player_id)
      on delete restrict on update restrict;

alter table chess_Games
   add constraint FK_cp_chess_party_id foreign key (chess_game_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;

alter table chess_Moves
   add constraint FK_cp_moves_parent_move foreign key (parent_move_id)
      references chess_Moves (move_id)
      on delete restrict on update restrict;

alter table chess_Moves
   add constraint FK_cp_moves_results foreign key (notice_id)
      references chess_moves_notations (notice_id)
      on delete restrict on update restrict;

alter table chess_Moves
   add constraint FK_cp_party_id foreign key (chess_game_id)
      references chess_Games (chess_game_id)
      on delete restrict on update restrict;

alter table chess_Players
   add constraint FK_cp_player_id foreign key (player_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;

alter table chess_move_estimations
   add constraint FK_cp_move_estimations foreign key (move_id)
      references chess_Moves (move_id)
      on delete restrict on update restrict;

alter table chess_move_estimations
   add constraint FK_cp_move_estimations_engine foreign key (engine_id)
      references chess_engines (engine_id)
      on delete restrict on update restrict;

