/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     09.08.2020 21:32:16                          */
/*==============================================================*/


drop table wc_CertTypeRef;

drop table wc_Certificates;

drop table wc_CheckerboardActions;

drop table wc_CheckerboardsRef;

drop table wc_EnginesRef;

drop table wc_GameActions;

drop table wc_GameActionsEstimations;

drop table wc_Games;

drop table wc_GamesResultTypeRef;

drop table wc_GamesTypeRef;

drop table wc_Invites;

drop table wc_InvitesTypesRef;

drop table wc_MovesNotationsRef;

drop table wc_PiecesRef;

drop table wc_Players;

drop table wc_PlayersRatings;

drop table wc_PrivateInvites;

drop table wc_RatingTypeRef;

drop table wc_TimerAddAlgRef;

drop table wc_TimerTypesRef;

drop sequence seq_wc_CheckerboardActions;

drop sequence seq_wc_GameActions;

create sequence seq_wc_CheckerboardActions
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_wc_GameActions
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

/*==============================================================*/
/* Table: wc_CertTypeRef                                        */
/*==============================================================*/
create table wc_CertTypeRef (
   cert_type_id         TIdCode              not null,
   cert_type_name       TStr200              not null,
   constraint PK_WC_CERTTYPEREF primary key (cert_type_id)
);

comment on table wc_CertTypeRef is
'Справочник видов сертификатов участия';

/*==============================================================*/
/* Table: wc_Certificates                                       */
/*==============================================================*/
create table wc_Certificates (
   certificate_id       TIdBigCode           not null,
   cert_type_id         TIdCode              not null,
   player_id            TIdBigCode           not null,
   cert_issue_date      TDateTime            not null,
   game_date            TIdCode              not null,
   cert_info            TStr2000             not null,
   constraint PK_WC_CERTIFICATES primary key (certificate_id)
);

comment on table wc_Certificates is
'Картотека сертификатов';

/*==============================================================*/
/* Table: wc_CheckerboardActions                                */
/*==============================================================*/
create table wc_CheckerboardActions (
   move_action_id       TIdBigCode           not null,
   game_action_id       TIdBigCode           not null,
   piece_code           TStr3                not null,
   checkerboard_code    TStr2                not null,
   move_direction       TBoolean             not null,
   is_white             TBoolean             not null,
   constraint PK_WC_CHECKERBOARDACTIONS primary key (move_action_id)
);

comment on table wc_CheckerboardActions is
'Действие над шахматной клеткой (прибытие и убытие фигур)';

/*==============================================================*/
/* Table: wc_CheckerboardsRef                                   */
/*==============================================================*/
create table wc_CheckerboardsRef (
   checkerboard_code    TStr2                not null,
   checkerboard_name    TStr100              not null,
   constraint PK_WC_CHECKERBOARDSREF primary key (checkerboard_code)
);

comment on table wc_CheckerboardsRef is
'Справочник адресов шахматных клеток';

/*==============================================================*/
/* Table: wc_EnginesRef                                         */
/*==============================================================*/
create table wc_EnginesRef (
   engine_id            TIdCode              not null,
   engine_code          TStr10               not null,
   engine_name          TStr200              not null,
   constraint PK_WC_ENGINESREF primary key (engine_id),
   constraint AK_ENGINE_CODE unique (engine_code)
);

comment on table wc_EnginesRef is
'Справочник шахматных движкой';

/*==============================================================*/
/* Table: wc_GameActions                                        */
/*==============================================================*/
create table wc_GameActions (
   game_action_id       TIdBigCode           not null,
   chess_game_id        TIdBigCode           not null,
   parent_game_action_id TIdBigCode           null,
   notice_code          TStr3                null,
   constraint PK_WC_GAMEACTIONS primary key (game_action_id)
);

comment on table wc_GameActions is
'Шахматные ходы';

/*==============================================================*/
/* Table: wc_GameActionsEstimations                             */
/*==============================================================*/
create table wc_GameActionsEstimations (
   game_action_id       TIdBigCode           not null,
   engine_id            TIdCode              not null,
   estimation           TPercRate            not null,
   constraint PK_WC_GAMEACTIONSESTIMATIONS primary key (game_action_id, engine_id)
);

comment on table wc_GameActionsEstimations is
'Оценки шахматных ходов';

/*==============================================================*/
/* Table: wc_Games                                              */
/*==============================================================*/
create table wc_Games (
   chess_game_id        TIdBigCode           not null,
   timer_type_id        TIdCode              not null,
   game_result_type_id  TIdCode              not null,
   white_player_id      TIdBigCode           not null,
   black_player_id      TIdBigCode           not null,
   game_start_date      TDateTime            not null,
   game_finish_date     TDateTime            null,
   white_player_points  TMoney               null,
   black_player_points  TMoney               null,
   constraint PK_WC_GAMES primary key (chess_game_id),
   constraint AK_WC_GAMES_WHITE unique (chess_game_id, white_player_id),
   constraint AK_WC_GAMES_BLACK unique (chess_game_id, black_player_id)
);

comment on table wc_Games is
'Шахматные партии';

/*==============================================================*/
/* Table: wc_GamesResultTypeRef                                 */
/*==============================================================*/
create table wc_GamesResultTypeRef (
   game_result_type_id  TIdCode              not null,
   game_result_type_name TStr100              not null,
   constraint PK_WC_GAMESRESULTTYPEREF primary key (game_result_type_id)
);

comment on table wc_GamesResultTypeRef is
'Результаты окончания партий';

/*==============================================================*/
/* Table: wc_GamesTypeRef                                       */
/*==============================================================*/
create table wc_GamesTypeRef (
   game_type_id         TIdCode              not null,
   game_type_name       TStr100              not null,
   constraint PK_WC_GAMESTYPEREF primary key (game_type_id)
);

comment on table wc_GamesTypeRef is
'Тип игры';

/*==============================================================*/
/* Table: wc_Invites                                            */
/*==============================================================*/
create table wc_Invites (
   invite_id            TIdBigCode           not null,
   player_id            TIdBigCode           not null,
   invoice_type_id      TIdCode              not null,
   invate_date          TDateTime            not null,
   turnament_date       TDate                not null,
   tournament_name      TStr2000             not null,
   tournament_start_date TDate                not null,
   tournament_finish_date TDate                not null,
   prize                TMoney               not null,
   constraint PK_WC_INVITES primary key (invite_id)
);

comment on table wc_Invites is
'Перечень приглашений';

/*==============================================================*/
/* Table: wc_InvitesTypesRef                                    */
/*==============================================================*/
create table wc_InvitesTypesRef (
   invite_type_id       TIdCode              not null,
   invite_type_name     TStr100              not null,
   constraint PK_WC_INVITESTYPESREF primary key (invite_type_id)
);

comment on table wc_InvitesTypesRef is
'Виды приглашений';

/*==============================================================*/
/* Table: wc_MovesNotationsRef                                  */
/*==============================================================*/
create table wc_MovesNotationsRef (
   notice_code          TStr3                not null,
   notice_name          TStr100              not null,
   constraint PK_WC_MOVESNOTATIONSREF primary key (notice_code)
);

comment on table wc_MovesNotationsRef is
'Справочник нотаций к ходу';

/*==============================================================*/
/* Table: wc_PiecesRef                                          */
/*==============================================================*/
create table wc_PiecesRef (
   piece_code           TStr3                not null,
   piece_name           TStr100              not null,
   constraint PK_WC_PIECESREF primary key (piece_code)
);

comment on table wc_PiecesRef is
'Справочник шахматных фигур';

/*==============================================================*/
/* Table: wc_Players                                            */
/*==============================================================*/
create table wc_Players (
   player_id            TIdBigCode           not null,
   last_name            TStr100              not null,
   first_name           TStr100              not null,
   is_blocked           TBoolean             not null,
   white_wins           TIdCode              not null,
   black_wins           TIdCode              not null,
   total_games          TIdCode              not null,
   white_losts          TIdCode              not null,
   black_losts          TIdCode              not null,
   constraint PK_WC_PLAYERS primary key (player_id)
);

comment on table wc_Players is
'Перечень игроков';

/*==============================================================*/
/* Table: wc_PlayersRatings                                     */
/*==============================================================*/
create table wc_PlayersRatings (
   rate_id              TIdBigCode           not null,
   player_id            TIdBigCode           not null,
   rating_type_id       TIdCode              not null,
   rating_value         TMoney               not null,
   rating_actual_date   TDateTime            not null,
   assign_note          TStr2000             not null,
   constraint PK_WC_PLAYERSRATINGS primary key (rate_id)
);

comment on table wc_PlayersRatings is
'Рейтинги игроков';

/*==============================================================*/
/* Table: wc_PrivateInvites                                     */
/*==============================================================*/
create table wc_PrivateInvites (
   invite_id            TIdBigCode           not null,
   player_id            TIdBigCode           not null,
   invate_date          TDateTime            not null,
   game_date            TDate                not null,
   constraint PK_WC_PRIVATEINVITES primary key (invite_id)
);

comment on table wc_PrivateInvites is
'Перечень частных приглашений';

/*==============================================================*/
/* Table: wc_RatingTypeRef                                      */
/*==============================================================*/
create table wc_RatingTypeRef (
   rating_type_id       TIdCode              not null,
   rating_type_name     TStr100              not null,
   constraint PK_WC_RATINGTYPEREF primary key (rating_type_id)
);

comment on table wc_RatingTypeRef is
'Тип рейтинга';

/*==============================================================*/
/* Table: wc_TimerAddAlgRef                                     */
/*==============================================================*/
create table wc_TimerAddAlgRef (
   timer_add_alg_id     TIdCode              not null,
   timer_add_alg_name   TStr200              not null,
   constraint PK_WC_TIMERADDALGREF primary key (timer_add_alg_id)
);

comment on table wc_TimerAddAlgRef is
'Код алгоритма добавления времени к ходу';

/*==============================================================*/
/* Table: wc_TimerTypesRef                                      */
/*==============================================================*/
create table wc_TimerTypesRef (
   timer_type_id        TIdCode              not null,
   timer_add_alg_id     TIdCode              not null,
   timer_type_name      TStr200              not null,
   constraint PK_WC_TIMERTYPESREF primary key (timer_type_id)
);

comment on table wc_TimerTypesRef is
'Виды временного учета шахматной партии';

alter table wc_Certificates
   add constraint FK_wc_cert_cert_id foreign key (certificate_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;

alter table wc_Certificates
   add constraint FK_wc_cert_player_id foreign key (player_id)
      references wc_Players (player_id)
      on delete restrict on update restrict;

alter table wc_Certificates
   add constraint FK_wc_cert_type_id foreign key (cert_type_id)
      references wc_CertTypeRef (cert_type_id)
      on delete restrict on update restrict;

alter table wc_CheckerboardActions
   add constraint FK_wc_cb_game_action foreign key (game_action_id)
      references wc_GameActions (game_action_id)
      on delete restrict on update restrict;

alter table wc_CheckerboardActions
   add constraint FK_wc_checkerboard_action_piece foreign key (piece_code)
      references wc_PiecesRef (piece_code)
      on delete restrict on update restrict;

alter table wc_CheckerboardActions
   add constraint FK_wc_cp_checkboard_run foreign key (checkerboard_code)
      references wc_CheckerboardsRef (checkerboard_code)
      on delete restrict on update restrict;

alter table wc_GameActions
   add constraint FK_wc_cp_moves_results foreign key (notice_code)
      references wc_MovesNotationsRef (notice_code)
      on delete restrict on update restrict;

alter table wc_GameActions
   add constraint FK_wc_game_actions_parent_move foreign key (parent_game_action_id)
      references wc_GameActions (game_action_id)
      on delete restrict on update restrict;

alter table wc_GameActions
   add constraint FK_wc_party_id foreign key (chess_game_id)
      references wc_Games (chess_game_id)
      on delete restrict on update restrict;

alter table wc_GameActionsEstimations
   add constraint FK_wc_ga_estimations foreign key (game_action_id)
      references wc_GameActions (game_action_id)
      on delete restrict on update restrict;

alter table wc_GameActionsEstimations
   add constraint FK_wc_me_engine_id foreign key (engine_id)
      references wc_EnginesRef (engine_id)
      on delete restrict on update restrict;

alter table wc_Games
   add constraint FK_wc_cg_white_player_id foreign key (white_player_id)
      references wc_Players (player_id)
      on delete restrict on update restrict;

alter table wc_Games
   add constraint FK_wc_Games_game_id foreign key (chess_game_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;

alter table wc_Games
   add constraint FK_wc_games_result_type_id foreign key (game_result_type_id)
      references wc_GamesResultTypeRef (game_result_type_id)
      on delete restrict on update restrict;

alter table wc_Games
   add constraint FK_wc_Games_timer_type_id foreign key (timer_type_id)
      references wc_TimerTypesRef (timer_type_id)
      on delete restrict on update restrict;

alter table wc_Games
   add constraint FK_wp_cg_black_player_id foreign key (black_player_id)
      references wc_Players (player_id)
      on delete restrict on update restrict;

alter table wc_Invites
   add constraint FK_wc_Invite_type_id foreign key (invoice_type_id)
      references wc_InvitesTypesRef (invite_type_id)
      on delete restrict on update restrict;

alter table wc_Invites
   add constraint FK_wc_Invites_Player_id foreign key (player_id)
      references wc_Players (player_id)
      on delete restrict on update restrict;

alter table wc_Invites
   add constraint FK_wc_Invites_id foreign key (invite_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;

alter table wc_Players
   add constraint FK_wc_Players_player_id foreign key (player_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;

alter table wc_PlayersRatings
   add constraint FK_wc_pr_player_id foreign key (player_id)
      references wc_Players (player_id)
      on delete restrict on update restrict;

alter table wc_PlayersRatings
   add constraint FK_wc_pr_rate_id foreign key (rate_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;

alter table wc_PlayersRatings
   add constraint FK_wc_pr_rating_type_id foreign key (rating_type_id)
      references wc_RatingTypeRef (rating_type_id)
      on delete restrict on update restrict;

alter table wc_PrivateInvites
   add constraint FK_wc_pi_invite_id foreign key (invite_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;

alter table wc_PrivateInvites
   add constraint FK_wc_pi_player_id foreign key (player_id)
      references wc_Players (player_id)
      on delete restrict on update restrict;

alter table wc_TimerTypesRef
   add constraint FK_wc_ttr_add_alg_id foreign key (timer_add_alg_id)
      references wc_TimerAddAlgRef (timer_add_alg_id)
      on delete restrict on update restrict;

