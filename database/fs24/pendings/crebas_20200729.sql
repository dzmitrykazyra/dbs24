/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     29.07.2020 18:25:39                          */
/*==============================================================*/


drop table msn_DialogUserStatusesRef;

drop table msn_Dialogs;

drop table msn_DialogsModerators;

drop table msn_DialogsModeratorsActions;

drop table msn_ModeratorRightsRef;

drop table msn_User2Dialog;

drop table msn_User2DialogActions;

drop table msn_UserPosts;

drop table msn_UserPosts_Hist;

drop table msn_Users;

drop table msn_UsersHist;

drop sequence seq_msn_Dialogs;

drop sequence seq_msn_UserPosts;

drop sequence seq_msn_UserPosts_Hist;

drop sequence seq_msn_UsersHist;

create sequence seq_msn_Dialogs
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_msn_UserPosts
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_msn_UserPosts_Hist
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_msn_UsersHist
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

/*==============================================================*/
/* Table: msn_DialogUserStatusesRef                             */
/*==============================================================*/
create table msn_DialogUserStatusesRef (
   dialog_user_status_code TIdCode              not null,
   dialog_user_status_name TStr100              not null,
   constraint PK_MSN_DIALOGUSERSTATUSESREF primary key (dialog_user_status_code)
);

comment on table msn_DialogUserStatusesRef is
'Статус участия пользователя в диалоге';

/*==============================================================*/
/* Table: msn_Dialogs                                           */
/*==============================================================*/
create table msn_Dialogs (
   dialog_id            TIdBigCode           not null,
   dialog_name          TStr200              not null,
   short_dialog_name    TStr50               not null,
   constraint PK_MSN_DIALOGS primary key (dialog_id)
);

comment on table msn_Dialogs is
'Картотека диалогов';

/*==============================================================*/
/* Table: msn_DialogsModerators                                 */
/*==============================================================*/
create table msn_DialogsModerators (
   dialog_id            TIdBigCode           not null,
   user_id              TIdBigCode           not null,
   right_id             TIdCode              not null,
   constraint PK_MSN_DIALOGSMODERATORS primary key (dialog_id, user_id)
);

comment on table msn_DialogsModerators is
'Картотека модераторов диалога';

/*==============================================================*/
/* Table: msn_DialogsModeratorsActions                          */
/*==============================================================*/
create table msn_DialogsModeratorsActions (
   dialog_action_id     TIdBigCode           not null,
   dialog_id            TIdBigCode           not null,
   user_id              TIdBigCode           not null,
   right_id             TIdCode              not null,
   action_date          TDateTime            not null,
   note                 TStr2000             null,
   constraint PK_MSN_DIALOGSMODERATORSACTION primary key (dialog_action_id)
);

comment on table msn_DialogsModeratorsActions is
'Картотека изменений прав модераторов диалога';

/*==============================================================*/
/* Table: msn_ModeratorRightsRef                                */
/*==============================================================*/
create table msn_ModeratorRightsRef (
   right_id             TIdCode              not null,
   right_name           TStr100              not null,
   constraint PK_MSN_MODERATORRIGHTSREF primary key (right_id)
);

comment on table msn_ModeratorRightsRef is
'Тип полномочий модератора в группе';

/*==============================================================*/
/* Table: msn_User2Dialog                                       */
/*==============================================================*/
create table msn_User2Dialog (
   dialog_id            TIdBigCode           not null,
   user_id              TIdBigCode           not null,
   dialog_user_status_code TIdCode              not null,
   constraint PK_MSN_USER2DIALOG primary key (dialog_id, user_id)
);

comment on table msn_User2Dialog is
'Картотека участников диалога';

/*==============================================================*/
/* Table: msn_User2DialogActions                                */
/*==============================================================*/
create table msn_User2DialogActions (
   dialog_action_id     TIdBigCode           not null,
   dialog_id            TIdBigCode           not null,
   user_id              TIdBigCode           not null,
   dialog_action_user_id TIdBigCode           not null,
   dialog_user_status_code TIdCode              not null,
   dialog_action_date   TDateTime            not null,
   constraint PK_MSN_USER2DIALOGACTIONS primary key (dialog_action_id)
);

comment on table msn_User2DialogActions is
'Изменение статуса пользователя в диалоге';

/*==============================================================*/
/* Table: msn_UserPosts                                         */
/*==============================================================*/
create table msn_UserPosts (
   post_id              TIdBigCode           not null,
   dialog_id            TIdBigCode           not null,
   user_id              TIdBigCode           not null,
   post_header          TStr200              not null,
   post_body            TStr2000             null,
   create_date          TDateTime            not null,
   receive_date         TDateTime            not null,
   constraint PK_MSN_USERPOSTS primary key (post_id)
);

comment on table msn_UserPosts is
'Картотека сообщений от пользователя';

/*==============================================================*/
/* Table: msn_UserPosts_Hist                                    */
/*==============================================================*/
create table msn_UserPosts_Hist (
   id_upd               TIdBigCode           not null,
   post_id              TIdBigCode           not null,
   upd_date             TDateTime            not null,
   old_post_body        TStr2000             not null,
   constraint PK_MSN_USERPOSTS_HIST primary key (id_upd)
);

comment on table msn_UserPosts_Hist is
'Картотека редактур сообщений пользователя';

/*==============================================================*/
/* Table: msn_Users                                             */
/*==============================================================*/
create table msn_Users (
   user_id              TIdBigCode           not null,
   last_name            TStr100              not null,
   first_name           TStr100              not null,
   uid                  TStr100              not null,
   hash_pwd             TStr200              not null,
   email                TStr100              not null,
   mobile               TStr100              not null,
   constraint PK_MSN_USERS primary key (user_id)
);

comment on table msn_Users is
'Картотека учетных записей пользователей';

/*==============================================================*/
/* Table: msn_UsersHist                                         */
/*==============================================================*/
create table msn_UsersHist (
   id_upd               TIdBigCode           not null,
   user_id              TIdBigCode           not null,
   last_name            TStr100              not null,
   first_name           TStr100              not null,
   uid                  TStr100              not null,
   hash_pwd             TStr200              not null,
   email                TStr100              not null,
   mobile               TStr100              not null,
   update_date          TDateTime            not null,
   constraint PK_MSN_USERSHIST primary key (id_upd)
);

comment on table msn_UsersHist is
'Картотека изменения учетных записей';

alter table msn_DialogsModerators
   add constraint FK_msn_dm_dialog_id foreign key (dialog_id)
      references msn_Dialogs (dialog_id)
      on delete restrict on update restrict;

alter table msn_DialogsModerators
   add constraint FK_msn_dm_rigth_id foreign key (right_id)
      references msn_ModeratorRightsRef (right_id)
      on delete restrict on update restrict;

alter table msn_DialogsModerators
   add constraint FK_msn_dm_user_id foreign key (user_id)
      references msn_Users (user_id)
      on delete restrict on update restrict;

alter table msn_DialogsModeratorsActions
   add constraint FK_msn_dm_actions foreign key (dialog_id, user_id)
      references msn_DialogsModerators (dialog_id, user_id)
      on delete restrict on update restrict;

alter table msn_DialogsModeratorsActions
   add constraint FK_msn_dm_actions_right_id foreign key (right_id)
      references msn_ModeratorRightsRef (right_id)
      on delete restrict on update restrict;

alter table msn_User2Dialog
   add constraint FK_msn_User2Dialog_dialog_id foreign key (dialog_id)
      references msn_Dialogs (dialog_id)
      on delete restrict on update restrict;

alter table msn_User2Dialog
   add constraint FK_msn_User2Dialog_user_id foreign key (user_id)
      references msn_Users (user_id)
      on delete restrict on update restrict;

alter table msn_User2Dialog
   add constraint FK_msn_u2d_status_code foreign key (dialog_user_status_code)
      references msn_DialogUserStatusesRef (dialog_user_status_code)
      on delete restrict on update restrict;

alter table msn_User2DialogActions
   add constraint FK_msn_U2D_Actions_da_user_id foreign key (dialog_action_user_id)
      references msn_Users (user_id)
      on delete restrict on update restrict;

alter table msn_User2DialogActions
   add constraint FK_msn_U2D_Actions_user_id foreign key (dialog_id, user_id)
      references msn_User2Dialog (dialog_id, user_id)
      on delete restrict on update restrict;

alter table msn_User2DialogActions
   add constraint FK_msn_U2_Actions_status_code foreign key (dialog_user_status_code)
      references msn_DialogUserStatusesRef (dialog_user_status_code)
      on delete restrict on update restrict;

alter table msn_UserPosts
   add constraint FK_msn_UserPosts_dialog_id foreign key (dialog_id)
      references msn_Dialogs (dialog_id)
      on delete restrict on update restrict;

alter table msn_UserPosts
   add constraint FK_msn_UserPosts_user_id foreign key (user_id)
      references msn_Users (user_id)
      on delete restrict on update restrict;

alter table msn_UserPosts_Hist
   add constraint FK_msn_UserPosts_Hist_post_id foreign key (post_id)
      references msn_UserPosts (post_id)
      on delete restrict on update restrict;

alter table msn_Users
   add constraint FK_msn_users_user_id foreign key (user_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;

alter table msn_UsersHist
   add constraint FK_msn_uh_user_id foreign key (user_id)
      references msn_Users (user_id)
      on delete restrict on update restrict;

