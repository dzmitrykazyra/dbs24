/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     7/2/2021 6:12:39 PM                          */
/*==============================================================*/


drop table ifs_account_hist;

drop table ifs_account_statuses_ref;

drop index idx_ifs_insta_id;

drop index idx_ifs_user_name;

drop table ifs_accounts;

drop table ifs_action_result_ref;

drop table ifs_action_types_ref;

drop table ifs_actions;

drop table ifs_post_statuses_ref;

drop table ifs_post_types_ref;

drop table ifs_posts;

drop table ifs_source_faces;

drop table ifs_source_statuses_ref;

drop table ifs_sources;

drop table ifs_task_statuses_ref;

drop table ifs_tasks;

drop table ifs_tasks_hist;

drop domain TBanknotesAmt;

drop domain TBinary;

drop domain TBoolean;

drop domain TChessGameScore;

drop domain TCoinsAmt;

drop domain TCurrencyISO;

drop domain TCurrencyStr10;

drop domain TDate;

drop domain TDateTime;

drop domain TGPSCoordinates;

drop domain TIdBigCode;

drop domain TIdCode;

drop domain TIdUser;

drop domain TImage;

drop domain TIntCounter;

drop domain TInteger;

drop domain TItemType;

drop domain TMoney;

drop domain TPercRate;

drop domain TPercRateExt;

drop domain TReal;

drop domain TStr10;

drop domain TStr100;

drop domain TStr10000;

drop domain TStr128;

drop domain TStr2;

drop domain TStr20;

drop domain TStr200;

drop domain TStr2000;

drop domain TStr3;

drop domain TStr30;

drop domain TStr40;

drop domain TStr400;

drop domain TStr50;

drop domain TStr80;

drop domain TSumExt;

drop domain TText;

drop domain TTime;

drop domain TidByteCode;

drop sequence seq_ifs_Accounts;

drop sequence seq_ifs_Actions;

drop sequence seq_ifs_Posts;

drop sequence seq_ifs_SourceFaces;

drop sequence seq_ifs_Sources;

drop sequence seq_ifs_Tasks;

create sequence seq_ifs_Accounts
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_ifs_Actions
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_ifs_Posts
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_ifs_SourceFaces
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_ifs_Sources
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_ifs_Tasks;

/*==============================================================*/
/* Domain: TBanknotesAmt                                        */
/*==============================================================*/
create domain TBanknotesAmt as INTEGER;

/*==============================================================*/
/* Domain: TBinary                                              */
/*==============================================================*/
create domain TBinary as bytea;

/*==============================================================*/
/* Domain: TBoolean                                             */
/*==============================================================*/
create domain TBoolean as BOOL;

/*==============================================================*/
/* Domain: TChessGameScore                                      */
/*==============================================================*/
create domain TChessGameScore as DECIMAL(3,1);

/*==============================================================*/
/* Domain: TCoinsAmt                                            */
/*==============================================================*/
create domain TCoinsAmt as INTEGER;

/*==============================================================*/
/* Domain: TCurrencyISO                                         */
/*==============================================================*/
create domain TCurrencyISO as VARCHAR(3);

/*==============================================================*/
/* Domain: TCurrencyStr10                                       */
/*==============================================================*/
create domain TCurrencyStr10 as VARCHAR(10);

/*==============================================================*/
/* Domain: TDate                                                */
/*==============================================================*/
create domain TDate as DATE;

/*==============================================================*/
/* Domain: TDateTime                                            */
/*==============================================================*/
create domain TDateTime as TIMESTAMP;

/*==============================================================*/
/* Domain: TGPSCoordinates                                      */
/*==============================================================*/
create domain TGPSCoordinates as POINT;

/*==============================================================*/
/* Domain: TIdBigCode                                           */
/*==============================================================*/
create domain TIdBigCode as BIGINT;

/*==============================================================*/
/* Domain: TIdCode                                              */
/*==============================================================*/
create domain TIdCode as INTEGER;

comment on domain TIdCode is
'Код сущности';

/*==============================================================*/
/* Domain: TIdUser                                              */
/*==============================================================*/
create domain TIdUser as INTEGER;

/*==============================================================*/
/* Domain: TImage                                               */
/*==============================================================*/
create domain TImage as BYTEA;

/*==============================================================*/
/* Domain: TIntCounter                                          */
/*==============================================================*/
create domain TIntCounter as INTEGER;

/*==============================================================*/
/* Domain: TInteger                                             */
/*==============================================================*/
create domain TInteger as INTEGER;

/*==============================================================*/
/* Domain: TItemType                                            */
/*==============================================================*/
create domain TItemType as SMALLINT;

/*==============================================================*/
/* Domain: TMoney                                               */
/*==============================================================*/
create domain TMoney as NUMERIC(22,4);

/*==============================================================*/
/* Domain: TPercRate                                            */
/*==============================================================*/
create domain TPercRate as NUMERIC(12,6);

/*==============================================================*/
/* Domain: TPercRateExt                                         */
/*==============================================================*/
create domain TPercRateExt as NUMERIC(16,8);

/*==============================================================*/
/* Domain: TReal                                                */
/*==============================================================*/
create domain TReal as real;

/*==============================================================*/
/* Domain: TStr10                                               */
/*==============================================================*/
create domain TStr10 as VARCHAR(10);

/*==============================================================*/
/* Domain: TStr100                                              */
/*==============================================================*/
create domain TStr100 as VARCHAR(100);

/*==============================================================*/
/* Domain: TStr10000                                            */
/*==============================================================*/
create domain TStr10000 as VARCHAR(10000);

/*==============================================================*/
/* Domain: TStr128                                              */
/*==============================================================*/
create domain TStr128 as VARCHAR(128);

/*==============================================================*/
/* Domain: TStr2                                                */
/*==============================================================*/
create domain TStr2 as VARCHAR(2);

/*==============================================================*/
/* Domain: TStr20                                               */
/*==============================================================*/
create domain TStr20 as VARCHAR(20);

/*==============================================================*/
/* Domain: TStr200                                              */
/*==============================================================*/
create domain TStr200 as VARCHAR(200);

/*==============================================================*/
/* Domain: TStr2000                                             */
/*==============================================================*/
create domain TStr2000 as VARCHAR(2000);

/*==============================================================*/
/* Domain: TStr3                                                */
/*==============================================================*/
create domain TStr3 as VARCHAR(3);

/*==============================================================*/
/* Domain: TStr30                                               */
/*==============================================================*/
create domain TStr30 as VARCHAR(30);

/*==============================================================*/
/* Domain: TStr40                                               */
/*==============================================================*/
create domain TStr40 as VARCHAR(40);

/*==============================================================*/
/* Domain: TStr400                                              */
/*==============================================================*/
create domain TStr400 as VARCHAR(400);

/*==============================================================*/
/* Domain: TStr50                                               */
/*==============================================================*/
create domain TStr50 as VARCHAR(50);

/*==============================================================*/
/* Domain: TStr80                                               */
/*==============================================================*/
create domain TStr80 as VARCHAR(80);

/*==============================================================*/
/* Domain: TSumExt                                              */
/*==============================================================*/
create domain TSumExt as NUMERIC(24,4);

/*==============================================================*/
/* Domain: TText                                                */
/*==============================================================*/
create domain TText as TEXT;

/*==============================================================*/
/* Domain: TTime                                                */
/*==============================================================*/
create domain TTime as TIME;

/*==============================================================*/
/* Domain: TidByteCode                                          */
/*==============================================================*/
create domain TidByteCode as NUMERIC(1);

/*==============================================================*/
/* Table: ifs_account_hist                                      */
/*==============================================================*/
create table ifs_account_hist (
   account_id           TIdBigCode           not null,
   actual_date          TDateTime            not null,
   account_status_id    TIdCode              null,
   insta_id             TIdBigCode           null,
   user_name            TStr100              null,
   full_name            TStr200              null,
   media_count          TInteger             null,
   followers_count      TInteger             null,
   followees_count      TInteger             null,
   biography            TStr10000            null,
   is_private           TBoolean             null,
   is_verified          TBoolean             null,
   profile_pic_url      TStr200              null,
   profile_pic_hd_url   TStr200              null,
   constraint PK_IFS_ACCOUNT_HIST primary key (account_id, actual_date)
);

comment on table ifs_account_hist is
'Аккаунт инстаграмма';

/*==============================================================*/
/* Table: ifs_account_statuses_ref                              */
/*==============================================================*/
create table ifs_account_statuses_ref (
   account_status_id    TIdCode              not null,
   account_status_name  TStr100              not null,
   constraint PK_IFS_ACCOUNT_STATUSES_REF primary key (account_status_id)
);

comment on table ifs_account_statuses_ref is
'Код статуса аккаунта';

/*==============================================================*/
/* Table: ifs_accounts                                          */
/*==============================================================*/
create table ifs_accounts (
   account_id           TIdBigCode           not null,
   account_status_id    TIdCode              not null,
   insta_id             TIdBigCode           not null,
   actual_date          TDateTime            not null,
   user_name            TStr100              not null,
   full_name            TStr200              null,
   mediacount           TInteger             null,
   followers            TInteger             null,
   followees            TInteger             null,
   biography            TStr10000            null,
   is_private           TBoolean             null,
   is_verified          TBoolean             null,
   profile_pic_url      TStr2000             null,
   profile_pic_hd_url   TStr2000             null,
   constraint PK_IFS_ACCOUNTS primary key (account_id),
   constraint AK_KEY_INSTA_ACC_ID unique (insta_id)
);

comment on table ifs_accounts is
'Аккаунт инстаграмма';

/*==============================================================*/
/* Index: idx_ifs_user_name                                     */
/*==============================================================*/
create  index idx_ifs_user_name on ifs_accounts (
user_name
);

/*==============================================================*/
/* Index: idx_ifs_insta_id                                      */
/*==============================================================*/
create  index idx_ifs_insta_id on ifs_accounts (
insta_id
);

/*==============================================================*/
/* Table: ifs_action_result_ref                                 */
/*==============================================================*/
create table ifs_action_result_ref (
   action_result_id     TIdCode              not null,
   action_result_name   TStr100              not null,
   constraint PK_IFS_ACTION_RESULT_REF primary key (action_result_id)
);

comment on table ifs_action_result_ref is
'Рез-т выполнения действия
';

/*==============================================================*/
/* Table: ifs_action_types_ref                                  */
/*==============================================================*/
create table ifs_action_types_ref (
   action_type_id       TIdCode              not null,
   action_type_name     TStr100              not null,
   constraint PK_IFS_ACTION_TYPES_REF primary key (action_type_id)
);

comment on table ifs_action_types_ref is
'Код типа таска';

/*==============================================================*/
/* Table: ifs_actions                                           */
/*==============================================================*/
create table ifs_actions (
   action_id            TIdBigCode           not null,
   action_start_date    TDateTime            not null,
   action_finish_date   TDateTime            not null,
   action_type_id       TIdCode              not null,
   action_result_id     TIdCode              not null,
   account_id           TIdBigCode           not null,
   post_id              TIdBigCode           null,
   task_id              TIdBigCode           null,
   action_note          TStr10000            null,
   error                TStr10000            null,
   constraint PK_IFS_ACTIONS primary key (action_id)
);

comment on table ifs_actions is
'Действия';

/*==============================================================*/
/* Table: ifs_post_statuses_ref                                 */
/*==============================================================*/
create table ifs_post_statuses_ref (
   post_status_id       TIdCode              not null,
   post_status_name     TStr100              not null,
   constraint PK_IFS_POST_STATUSES_REF primary key (post_status_id)
);

comment on table ifs_post_statuses_ref is
'Код статуса поста';

/*==============================================================*/
/* Table: ifs_post_types_ref                                    */
/*==============================================================*/
create table ifs_post_types_ref (
   post_type_id         TIdCode              not null,
   post_type_name       TStr100              not null,
   constraint PK_IFS_POST_TYPES_REF primary key (post_type_id)
);

comment on table ifs_post_types_ref is
'Код типа сообщения';

/*==============================================================*/
/* Table: ifs_posts                                             */
/*==============================================================*/
create table ifs_posts (
   post_id              TIdBigCode           not null,
   actual_date          TDateTime            not null,
   account_id           TIdBigCode           not null,
   post_status_id       TIdCode              not null,
   media_id             TIdBigCode           not null,
   short_code           TStr100              not null,
   post_type_id         TIdCode              not null,
   constraint PK_IFS_POSTS primary key (post_id),
   constraint AK_KEY_IFS_POSTS unique (short_code),
   constraint AK_KEY_3_IFS_POST unique (media_id)
);

comment on table ifs_posts is
'Посты в инсте';

/*==============================================================*/
/* Table: ifs_source_faces                                      */
/*==============================================================*/
create table ifs_source_faces (
   source_face_id       TIdBigCode           not null,
   source_id            TIdBigCode           not null,
   face_box             TBinary              not null,
   face_vector          TBinary              not null,
   actual_date          TDateTime            not null,
   constraint PK_IFS_SOURCE_FACES primary key (source_face_id)
);

comment on table ifs_source_faces is
'Выделенные лица';

/*==============================================================*/
/* Table: ifs_source_statuses_ref                               */
/*==============================================================*/
create table ifs_source_statuses_ref (
   source_status_id     TIdCode              not null,
   source_status_name   TStr100              not null,
   constraint PK_IFS_SOURCE_STATUSES_REF primary key (source_status_id)
);

comment on table ifs_source_statuses_ref is
'Код статуса источника';

/*==============================================================*/
/* Table: ifs_sources                                           */
/*==============================================================*/
create table ifs_sources (
   source_id            TIdBigCode           not null,
   post_id              TIdBigCode           not null,
   source_status_id     TIdCode              not null,
   source_url           TStr2000             not null,
   actual_date          TDateTime            not null,
   main_face_box        TBinary              null,
   source_hash          TStr200              null,
   constraint PK_IFS_SOURCES primary key (source_id)
);

comment on table ifs_sources is
'Источники данных в посте';

/*==============================================================*/
/* Table: ifs_task_statuses_ref                                 */
/*==============================================================*/
create table ifs_task_statuses_ref (
   task_status_id       TIdCode              not null,
   task_status_name     TStr100              not null,
   constraint PK_IFS_TASK_STATUSES_REF primary key (task_status_id)
);

comment on table ifs_task_statuses_ref is
'Статус задания
';

/*==============================================================*/
/* Table: ifs_tasks                                             */
/*==============================================================*/
create table ifs_tasks (
   task_id              TIdBigCode           not null,
   actual_date          TDateTime            not null,
   account_id           TIdBigCode           not null,
   start_date           TDateTime            not null,
   finish_date          TDateTime            null,
   task_status_id       TIdCode              not null,
   last_page_index      TIdCode              not null,
   post_cnt             TIdCode              not null,
   post_ready           TIdCode              not null,
   last_error           TStr10000            null,
   constraint PK_IFS_TASKS primary key (task_id)
);

comment on table ifs_tasks is
'Задачи';

/*==============================================================*/
/* Table: ifs_tasks_hist                                        */
/*==============================================================*/
create table ifs_tasks_hist (
   task_id              TIdBigCode           not null,
   actual_date          TDateTime            not null,
   account_id           TIdBigCode           null,
   start_date           TDateTime            null,
   finish_date          TDateTime            null,
   task_status_id       TIdCode              null,
   last_page_index      TIdCode              null,
   post_cnt             TIdCode              null,
   post_ready           TIdCode              null,
   last_error           TStr10000            null,
   constraint PK_IFS_TASKS_HIST primary key (task_id, actual_date)
);

comment on table ifs_tasks_hist is
'Задачи - hist';

alter table ifs_account_hist
   add constraint FK_ifs_account_hist_id foreign key (account_id)
      references ifs_accounts (account_id)
      on delete restrict on update restrict;

alter table ifs_accounts
   add constraint FK_ifs_accounts_status_id foreign key (account_status_id)
      references ifs_account_statuses_ref (account_status_id)
      on delete restrict on update restrict;

alter table ifs_actions
   add constraint FK_ifs_action_task_id foreign key (task_id)
      references ifs_tasks (task_id)
      on delete restrict on update restrict;

alter table ifs_actions
   add constraint FK_ifs_acton_post_id foreign key (post_id)
      references ifs_posts (post_id)
      on delete restrict on update restrict;

alter table ifs_actions
   add constraint FK_ifs_task_account foreign key (account_id)
      references ifs_accounts (account_id)
      on delete restrict on update restrict;

alter table ifs_actions
   add constraint FK_ifs_task_result foreign key (action_result_id)
      references ifs_action_result_ref (action_result_id)
      on delete restrict on update restrict;

alter table ifs_actions
   add constraint FK_ifs_tasks_task_type foreign key (action_type_id)
      references ifs_action_types_ref (action_type_id)
      on delete restrict on update restrict;

alter table ifs_posts
   add constraint FK_ifs_post_post_status_id foreign key (post_status_id)
      references ifs_post_statuses_ref (post_status_id)
      on delete restrict on update restrict;

alter table ifs_posts
   add constraint FK_ifs_posts_account_id foreign key (account_id)
      references ifs_accounts (account_id)
      on delete restrict on update restrict;

alter table ifs_posts
   add constraint FK_ifs_posts_post_type_id foreign key (post_type_id)
      references ifs_post_types_ref (post_type_id)
      on delete restrict on update restrict;

alter table ifs_source_faces
   add constraint FK_ifs_source_faces_source_id foreign key (source_id)
      references ifs_sources (source_id)
      on delete restrict on update restrict;

alter table ifs_sources
   add constraint FK_ids_sources_status_id foreign key (source_status_id)
      references ifs_source_statuses_ref (source_status_id)
      on delete restrict on update restrict;

alter table ifs_sources
   add constraint FK_ifs_sources_post_id foreign key (post_id)
      references ifs_posts (post_id)
      on delete restrict on update restrict;

alter table ifs_tasks
   add constraint FK_if_task_account_id foreign key (account_id)
      references ifs_accounts (account_id)
      on delete restrict on update restrict;

alter table ifs_tasks
   add constraint FK_ifs_task_status_id foreign key (task_status_id)
      references ifs_task_statuses_ref (task_status_id)
      on delete restrict on update restrict;

alter table ifs_tasks_hist
   add constraint FK_ifs_task_hist_task_id foreign key (task_id)
      references ifs_tasks (task_id)
      on delete restrict on update restrict;

