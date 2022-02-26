/*==============================================================*/
/* DBMS name:      ORACLE Version 12c                           */
/* Created on:     7/2/2021 5:06:14 PM                          */
/*==============================================================*/


alter table IFS_ACCOUNT
   drop constraint "FK_ifs_accounts_status_id";

alter table IFS_ACCOUNT_HIST
   drop constraint "FK_ifs_account_hist_id";

alter table IFS_BOTS
   drop constraint "FK_ifs_bot_bot_status_id";

alter table IFS_BOTS_HIST
   drop constraint "FK_ifs_bots_hist_bot_id";

alter table IFS_POSTS
   drop constraint "FK_ifs_posts_account_id";

alter table IFS_POSTS
   drop constraint "FK_ifs_posts_post_type_id";

alter table IFS_POSTS
   drop constraint "FK_ifs_post_post_status_id";

alter table IFS_SOURCES
   drop constraint "FK_ids_sources_status_id";

alter table IFS_SOURCES
   drop constraint "FK_ifs_sources_post_id";

alter table IFS_SOURCE_FACES
   drop constraint "FK_ifs_source_faces_source_id";

alter table IFS_TASKS
   drop constraint FK_IFS_TASKS_PARENT_TASK_ID;

alter table IFS_TASKS
   drop constraint "FK_ifs_tasks_task_type";

alter table IFS_TASKS
   drop constraint "FK_ifs_task_account_id";

alter table IFS_TASKS
   drop constraint "FK_ifs_task_account_id_new";

alter table IFS_TASKS
   drop constraint "FK_ifs_task_result";

drop index IDX_IFS_INSTA_ID;

drop index IDX_IFS_USER_NAME;

drop table IFS_ACCOUNT cascade constraints;

drop table IFS_ACCOUNTSTATUSESREF cascade constraints;

drop table IFS_ACCOUNT_HIST cascade constraints;

drop table IFS_BOTS cascade constraints;

drop table IFS_BOTS_HIST cascade constraints;

drop table IFS_BOT_STATUSES_REF cascade constraints;

drop table IFS_POSTS cascade constraints;

drop table IFS_POSTSTATUSESREF cascade constraints;

drop table IFS_POSTTYPESREF cascade constraints;

drop table IFS_SOURCES cascade constraints;

drop table IFS_SOURCESTATUSESREF cascade constraints;

drop table IFS_SOURCE_FACES cascade constraints;

drop table IFS_TASKRESULT_REF cascade constraints;

drop table IFS_TASKS cascade constraints;

drop table IFS_TASKTYPES_REF cascade constraints;

/*==============================================================*/
/* Table: IFS_ACCOUNT                                           */
/*==============================================================*/
create table IFS_ACCOUNT (
   ACCOUNT_ID           NUMBER                not null,
   ACCOUNT_STATUS_ID    INTEGER               not null,
   INSTA_ID             NUMBER                not null,
   ACTUAL_DATE          TIMESTAMP             not null,
   USER_NAME            VARCHAR2(100)         not null,
   FULL_NAME            VARCHAR2(200),
   MEDIACOUNT           INTEGER,
   FOLLOWERS            INTEGER,
   FOLLOWEES            INTEGER,
   BIOGRAPHY            VARCHAR2(10000),
   IS_PRIVATE           NUMBER(1),
   IS_VERIFIED          NUMBER(1),
   PROFILE_PIC_URL      VARCHAR2(2000),
   PROFILE_PIC_HD_URL   VARCHAR2(2000),
   constraint PK_IFS_ACCOUNT primary key (ACCOUNT_ID),
   constraint AK_KEY_INSTA_ACC_ID unique (INSTA_ID)
);

comment on table IFS_ACCOUNT is
'Аккаунт инстаграмма';

/*==============================================================*/
/* Index: IDX_IFS_USER_NAME                                     */
/*==============================================================*/
create index IDX_IFS_USER_NAME on IFS_ACCOUNT (
   USER_NAME ASC
);

/*==============================================================*/
/* Index: IDX_IFS_INSTA_ID                                      */
/*==============================================================*/
create index IDX_IFS_INSTA_ID on IFS_ACCOUNT (
   INSTA_ID ASC
);

/*==============================================================*/
/* Table: IFS_ACCOUNTSTATUSESREF                                */
/*==============================================================*/
create table IFS_ACCOUNTSTATUSESREF (
   ACCOUNT_STATUS_ID    INTEGER               not null,
   ACCOUNT_STATUS_NAME  VARCHAR2(100)         not null,
   constraint PK_IFS_ACCOUNTSTATUSESREF primary key (ACCOUNT_STATUS_ID)
);

comment on table IFS_ACCOUNTSTATUSESREF is
'Код статуса аккаунта';

/*==============================================================*/
/* Table: IFS_ACCOUNT_HIST                                      */
/*==============================================================*/
create table IFS_ACCOUNT_HIST (
   ACCOUNT_ID           NUMBER                not null,
   ACTUAL_DATE          TIMESTAMP             not null,
   ACCOUNT_STATUS_ID    INTEGER,
   INSTA_ID             NUMBER,
   USER_NAME            VARCHAR2(100),
   FULL_NAME            VARCHAR2(200),
   MEDIACOUNT           INTEGER,
   FOLLOWERS            INTEGER,
   FOLLOWEES            INTEGER,
   BIOGRAPHY            VARCHAR2(10000),
   IS_PRIVATE           NUMBER(1),
   IS_VERIFIED          NUMBER(1),
   PROFILE_PIC_URL      VARCHAR2(200),
   PROFILE_PIC_HD_URL   VARCHAR2(200),
   constraint PK_IFS_ACCOUNT_HIST primary key (ACCOUNT_ID, ACTUAL_DATE)
);

comment on table IFS_ACCOUNT_HIST is
'Аккаунт инстаграмма';

/*==============================================================*/
/* Table: IFS_BOTS                                              */
/*==============================================================*/
create table IFS_BOTS (
   BOT_ID               INTEGER               not null,
   ACTUAL_DATE          TIMESTAMP             not null,
   BOT_STATUS_ID        INTEGER               not null,
   EMAIL                VARCHAR2(100),
   USERNAME             VARCHAR2(100),
   PASSWORD             VARCHAR2(100),
   SESSIONID            VARCHAR2(200),
   USER_AGENT           VARCHAR2(200),
   constraint PK_IFS_BOTS primary key (BOT_ID)
);

comment on table IFS_BOTS is
'Сервисные агенты';

/*==============================================================*/
/* Table: IFS_BOTS_HIST                                         */
/*==============================================================*/
create table IFS_BOTS_HIST (
   BOT_ID               INTEGER               not null,
   ACTUAL_DATE          TIMESTAMP             not null,
   BOT_STATUS_ID        INTEGER               not null,
   EMAIL                VARCHAR2(100),
   USERNAME             VARCHAR2(100),
   PASSWORD             VARCHAR2(100),
   SESSIONID            VARCHAR2(200),
   USER_AGENT           VARCHAR2(200),
   constraint PK_IFS_BOTS_HIST primary key (BOT_ID, ACTUAL_DATE)
);

comment on table IFS_BOTS_HIST is
'Сервисные агенты - hist';

/*==============================================================*/
/* Table: IFS_BOT_STATUSES_REF                                  */
/*==============================================================*/
create table IFS_BOT_STATUSES_REF (
   BOT_STATUS_ID        INTEGER               not null,
   BOT_STATUS_NAME      VARCHAR2(100)         not null,
   constraint PK_IFS_BOT_STATUSES_REF primary key (BOT_STATUS_ID)
);

comment on table IFS_BOT_STATUSES_REF is
'Статус агента';

/*==============================================================*/
/* Table: IFS_POSTS                                             */
/*==============================================================*/
create table IFS_POSTS (
   POST_ID              NUMBER                not null,
   ACTUAL_DATE          TIMESTAMP             not null,
   ACCOUNT_ID           NUMBER                not null,
   POST_STATUS_ID       INTEGER               not null,
   MEDIA_ID             NUMBER                not null,
   SHORT_CODE           VARCHAR2(100)         not null,
   POST_TYPE_ID         INTEGER               not null,
   constraint PK_IFS_POSTS primary key (POST_ID),
   constraint AK_KEY_IFS_POSTS unique (SHORT_CODE),
   constraint AK_KEY_3_IFS_POST unique (MEDIA_ID)
);

comment on table IFS_POSTS is
'Посты в инсте';

/*==============================================================*/
/* Table: IFS_POSTSTATUSESREF                                   */
/*==============================================================*/
create table IFS_POSTSTATUSESREF (
   POST_STATUS_ID       INTEGER               not null,
   POST_STATUS_NAME     VARCHAR2(100)         not null,
   constraint PK_IFS_POSTSTATUSESREF primary key (POST_STATUS_ID)
);

comment on table IFS_POSTSTATUSESREF is
'Код статуса поста';

/*==============================================================*/
/* Table: IFS_POSTTYPESREF                                      */
/*==============================================================*/
create table IFS_POSTTYPESREF (
   POST_TYPE_ID         INTEGER               not null,
   POST_TYPE_NAME       VARCHAR2(100)         not null,
   constraint PK_IFS_POSTTYPESREF primary key (POST_TYPE_ID)
);

comment on table IFS_POSTTYPESREF is
'Код типа сообщения';

/*==============================================================*/
/* Table: IFS_SOURCES                                           */
/*==============================================================*/
create table IFS_SOURCES (
   SOURCE_ID            NUMBER                not null,
   POST_ID              NUMBER                not null,
   SOURCE_STATUS_ID     INTEGER               not null,
   SOURCE_URL           VARCHAR2(2000)        not null,
   ACTUAL_DATE          TIMESTAMP             not null,
   MAIN_FACE_BOX        RAW,
   SOURCE_HASH          VARCHAR2(200),
   constraint PK_IFS_SOURCES primary key (SOURCE_ID)
);

comment on table IFS_SOURCES is
'Источники данных в посте';

/*==============================================================*/
/* Table: IFS_SOURCESTATUSESREF                                 */
/*==============================================================*/
create table IFS_SOURCESTATUSESREF (
   SOURCE_STATUS_ID     INTEGER               not null,
   SOURCE_STATUS_NAME   VARCHAR2(100)         not null,
   constraint PK_IFS_SOURCESTATUSESREF primary key (SOURCE_STATUS_ID)
);

comment on table IFS_SOURCESTATUSESREF is
'Код статуса источника';

/*==============================================================*/
/* Table: IFS_SOURCE_FACES                                      */
/*==============================================================*/
create table IFS_SOURCE_FACES (
   SOURCE_FACE_ID       NUMBER                not null,
   SOURCE_ID            NUMBER                not null,
   FACE_BOX             RAW                   not null,
   FACE_VECTOR          RAW                   not null,
   ACTUAL_DATE          TIMESTAMP             not null,
   constraint PK_IFS_SOURCE_FACES primary key (SOURCE_FACE_ID)
);

comment on table IFS_SOURCE_FACES is
'Выделенные лица';

/*==============================================================*/
/* Table: IFS_TASKRESULT_REF                                    */
/*==============================================================*/
create table IFS_TASKRESULT_REF (
   TASK_RESULT_ID       INTEGER               not null,
   TASK_RESULT_NAME     VARCHAR2(100)         not null,
   constraint PK_IFS_TASKRESULT_REF primary key (TASK_RESULT_ID)
);

comment on table IFS_TASKRESULT_REF is
'Рез-т выполнения таска';

/*==============================================================*/
/* Table: IFS_TASKS                                             */
/*==============================================================*/
create table IFS_TASKS (
   TASK_ID              NUMBER                not null,
   ACCOUNT_ID           NUMBER,
   PARENT_TASK_ID       NUMBER,
   TASK_START_DATE      TIMESTAMP             not null,
   TASK_FINISH_DATE     TIMESTAMP,
   TASK_TYPE_ID         INTEGER               not null,
   TASK_RESULT_ID       INTEGER               not null,
   BOT_ID               NUMBER,
   ERROR                VARCHAR2(10000),
   REQUEST_PAGINATION_ID VARCHAR2(100),
   constraint PK_IFS_TASKS primary key (TASK_ID)
);

comment on table IFS_TASKS is
'Таски';

/*==============================================================*/
/* Table: IFS_TASKTYPES_REF                                     */
/*==============================================================*/
create table IFS_TASKTYPES_REF (
   TASK_TYPE_ID         INTEGER               not null,
   TASK_TYPE_NAME       VARCHAR2(100)         not null,
   constraint PK_IFS_TASKTYPES_REF primary key (TASK_TYPE_ID)
);

comment on table IFS_TASKTYPES_REF is
'Код типа таска';

alter table IFS_ACCOUNT
   add constraint "FK_ifs_accounts_status_id" foreign key (ACCOUNT_STATUS_ID)
      references IFS_ACCOUNTSTATUSESREF (ACCOUNT_STATUS_ID);

alter table IFS_ACCOUNT_HIST
   add constraint "FK_ifs_account_hist_id" foreign key (ACCOUNT_ID)
      references IFS_ACCOUNT (ACCOUNT_ID);

alter table IFS_BOTS
   add constraint "FK_ifs_bot_bot_status_id" foreign key (BOT_STATUS_ID)
      references IFS_BOT_STATUSES_REF (BOT_STATUS_ID);

alter table IFS_BOTS_HIST
   add constraint "FK_ifs_bots_hist_bot_id" foreign key (BOT_ID)
      references IFS_BOTS (BOT_ID);

alter table IFS_POSTS
   add constraint "FK_ifs_posts_account_id" foreign key (ACCOUNT_ID)
      references IFS_ACCOUNT (ACCOUNT_ID);

alter table IFS_POSTS
   add constraint "FK_ifs_posts_post_type_id" foreign key (POST_TYPE_ID)
      references IFS_POSTTYPESREF (POST_TYPE_ID);

alter table IFS_POSTS
   add constraint "FK_ifs_post_post_status_id" foreign key (POST_STATUS_ID)
      references IFS_POSTSTATUSESREF (POST_STATUS_ID);

alter table IFS_SOURCES
   add constraint "FK_ids_sources_status_id" foreign key (SOURCE_STATUS_ID)
      references IFS_SOURCESTATUSESREF (SOURCE_STATUS_ID);

alter table IFS_SOURCES
   add constraint "FK_ifs_sources_post_id" foreign key (POST_ID)
      references IFS_POSTS (POST_ID);

alter table IFS_SOURCE_FACES
   add constraint "FK_ifs_source_faces_source_id" foreign key (SOURCE_ID)
      references IFS_SOURCES (SOURCE_ID);

alter table IFS_TASKS
   add constraint FK_IFS_TASKS_PARENT_TASK_ID foreign key (PARENT_TASK_ID)
      references IFS_TASKS (TASK_ID);

alter table IFS_TASKS
   add constraint "FK_ifs_tasks_task_type" foreign key (TASK_TYPE_ID)
      references IFS_TASKTYPES_REF (TASK_TYPE_ID);

alter table IFS_TASKS
   add constraint "FK_ifs_task_account_id" foreign key (ACCOUNT_ID)
      references IFS_ACCOUNT (ACCOUNT_ID);

alter table IFS_TASKS
   add constraint "FK_ifs_task_account_id_new" foreign key (BOT_ID)
      references IFS_BOTS (BOT_ID);

alter table IFS_TASKS
   add constraint "FK_ifs_task_result" foreign key (TASK_RESULT_ID)
      references IFS_TASKRESULT_REF (TASK_RESULT_ID);

