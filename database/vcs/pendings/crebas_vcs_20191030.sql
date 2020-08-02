/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     30.10.2019 12:18:28                          */
/*==============================================================*/


drop table log_entityHistory;

drop table log_exceptHistory;

drop table log_requests;

drop table vcs_RegistryParams;

drop table vcs_application_releases;

drop table vcs_application_setup;

drop table vcs_database_setup;

drop table vcs_databases;

drop table vcs_databases_releases;

drop table vcs_servers;

drop table vcs_spaces;

drop domain TBanknotesAmt;

drop domain TBoolean;

drop domain TCoinsAmt;

drop domain TCurrencyISO;

drop domain TCurrencyStr10;

drop domain TDate;

drop domain TDateTime;

drop domain TGPSCoordinates;

drop domain TIdBigCode;

drop domain TIdCode;

drop domain TIdUser;

drop domain TIntCounter;

drop domain TItemType;

drop domain TMoney;

drop domain TPercRate;

drop domain TPercRateExt;

drop domain TReal;

drop domain TStr10;

drop domain TStr100;

drop domain TStr128;

drop domain TStr20;

drop domain TStr200;

drop domain TStr2000;

drop domain TStr3;

drop domain TStr30;

drop domain TStr40;

drop domain TStr50;

drop domain TStr80;

drop domain TSumExt;

drop domain TText;

drop domain TTime;

drop sequence seq_log_requests;

create sequence seq_log_requests
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

/*==============================================================*/
/* Domain: TBanknotesAmt                                        */
/*==============================================================*/
create domain TBanknotesAmt as INTEGER;

/*==============================================================*/
/* Domain: TBoolean                                             */
/*==============================================================*/
create domain TBoolean as BOOL;

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
/* Domain: TIntCounter                                          */
/*==============================================================*/
create domain TIntCounter as INTEGER;

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
/* Domain: TStr128                                              */
/*==============================================================*/
create domain TStr128 as VARCHAR(128);

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
create domain TSumExt as NUMBER(24,4);

/*==============================================================*/
/* Domain: TText                                                */
/*==============================================================*/
create domain TText as TEXT;

/*==============================================================*/
/* Domain: TTime                                                */
/*==============================================================*/
create domain TTime as TIME;

/*==============================================================*/
/* Table: log_entityHistory                                     */
/*==============================================================*/
create table log_entityHistory (
   id                   TIdBigCode           not null,
   entity_id            TIdBigCode           not null,
   modify_date          TDateTime            not null,
   request_id           TIdBigCode           not null,
   entity2json          TText                null,
   constraint PK_LOG_ENTITYHISTORY primary key (id)
);

comment on table log_entityHistory is
'История entity';

/*==============================================================*/
/* Table: log_exceptHistory                                     */
/*==============================================================*/
create table log_exceptHistory (
   id                   TIdBigCode           not null,
   entity_id            TIdBigCode           null,
   except_class         TStr128              not null,
   except_date          TDateTime            not null,
   package              TStr100              not null,
   address              TStr100              not null,
   request_id           TIdBigCode           not null,
   stack_trace          TText                not null,
   user_name            TStr100              null,
   constraint PK_LOG_EXCEPTHISTORY primary key (id)
);

comment on table log_exceptHistory is
'сообщения об исключениях';

/*==============================================================*/
/* Table: log_requests                                          */
/*==============================================================*/
create table log_requests (
   id                   TIdBigCode           not null,
   req_type             TStr20               not null,
   address              TStr100              not null,
   req_body             TText                not null,
   req_answer           TStr128              not null,
   message_date         TDateTime            not null,
   exception_message    TStr2000             null,
   duration             TIntCounter          not null,
   constraint PK_LOG_REQUESTS primary key (id)
);

comment on table log_requests is
'Полученные rest сообщения';

/*==============================================================*/
/* Table: vcs_RegistryParams                                    */
/*==============================================================*/
create table vcs_RegistryParams (
   param_id             TIdCode              not null,
   parent_param_id      TIdCode              null,
   param_name           TStr50               not null,
   param_value          TStr100              not null,
   param_descr          TStr100              null,
   constraint PK_VCS_REGISTRYPARAMS primary key (param_id, param_name)
);

comment on table vcs_RegistryParams is
'Настроченые параметры';

/*==============================================================*/
/* Table: vcs_application_releases                              */
/*==============================================================*/
create table vcs_application_releases (
   id                   TIdCode              not null,
   app_name             TStr100              not null,
   app_size             TIdCode              not null,
   app_body             TPercRate            not null,
   app_version          TStr128              not null,
   actual_date          TDateTime            not null,
   constraint PK_VCS_APPLICATION_RELEASES primary key (id),
   constraint AK_KEY_2_VCS_APPL unique (app_name, app_version)
);

comment on table vcs_application_releases is
'Присланные версии для установки на сервер приложений';

/*==============================================================*/
/* Table: vcs_application_setup                                 */
/*==============================================================*/
create table vcs_application_setup (
   id                   TIdCode              not null,
   app_name             TStr100              not null,
   space_name           TStr100              not null,
   server_name          TStr100              not null,
   app_version          TStr128              not null,
   app_setup_date       TDateTime            not null,
   constraint PK_VCS_APPLICATION_SETUP primary key (app_name, app_version, id)
);

comment on table vcs_application_setup is
'Установленные версии на сервер приложений';

/*==============================================================*/
/* Table: vcs_database_setup                                    */
/*==============================================================*/
create table vcs_database_setup (
   id                   TIdCode              not null,
   db_name              TStr100              not null,
   space_name           TStr100              not null,
   server_name          TStr100              not null,
   script_version       TStr128              not null,
   script_setup_date    TDateTime            not null,
   constraint PK_VCS_DATABASE_SETUP primary key (db_name, script_version, id)
);

comment on table vcs_database_setup is
'Установленные версии на БД';

/*==============================================================*/
/* Table: vcs_databases                                         */
/*==============================================================*/
create table vcs_databases (
   db_name              TStr100              not null,
   connect_string       TStr128              not null,
   diu                  TStr50               not null,
   dwp                  TStr100              not null,
   db_version           TStr100              null,
   constraint PK_VCS_DATABASES primary key (db_name)
);

comment on table vcs_databases is
'Картотека БД';

/*==============================================================*/
/* Table: vcs_databases_releases                                */
/*==============================================================*/
create table vcs_databases_releases (
   id                   TIdCode              not null,
   db_name              TStr100              not null,
   script_size          TIdCode              not null,
   script_body          TPercRate            not null,
   script_version       TStr50               not null,
   actual_date          TDateTime            not null,
   constraint PK_VCS_DATABASES_RELEASES primary key (id),
   constraint AK_KEY_2_VCS_DATA unique (db_name, script_version)
);

comment on table vcs_databases_releases is
'Версии для установки на БД';

/*==============================================================*/
/* Table: vcs_servers                                           */
/*==============================================================*/
create table vcs_servers (
   server_name          TStr100              not null,
   server_address       TStr100              null,
   diu                  TStr100              not null,
   dwp                  TStr100              not null,
   constraint PK_VCS_SERVERS primary key (server_name)
);

comment on table vcs_servers is
'Картотека серверов приложений';

/*==============================================================*/
/* Table: vcs_spaces                                            */
/*==============================================================*/
create table vcs_spaces (
   space_name           TStr100              not null,
   is_actual            TBoolean             not null,
   constraint PK_VCS_SPACES primary key (space_name)
);

comment on table vcs_spaces is
'Картотека условных пространств';

alter table log_entityHistory
   add constraint FK_LOG_ENTI_EH_REQUES_LOG_REQU foreign key (request_id)
      references log_requests (id)
      on delete restrict on update restrict;

alter table log_exceptHistory
   add constraint FK_LOG_EXCE_EH_REQUES_LOG_REQU foreign key (request_id)
      references log_requests (id)
      on delete restrict on update restrict;

alter table vcs_application_setup
   add constraint FK_VCS_APPL_AS_APP_VE_VCS_APPL foreign key (app_name, app_version)
      references vcs_application_releases (app_name, app_version)
      on delete restrict on update restrict;

alter table vcs_application_setup
   add constraint FK_VCS_APPL_AS_SERVER_VCS_SERV foreign key (server_name)
      references vcs_servers (server_name)
      on delete restrict on update restrict;

alter table vcs_application_setup
   add constraint FK_VCS_APPL_AS_SPACE__VCS_SPAC foreign key (space_name)
      references vcs_spaces (space_name)
      on delete restrict on update restrict;

alter table vcs_database_setup
   add constraint FK_VCS_DATA_DS_SCRIPT_VCS_DATA foreign key (db_name, script_version)
      references vcs_databases_releases (db_name, script_version)
      on delete restrict on update restrict;

alter table vcs_database_setup
   add constraint FK_VCS_DATA_DS_SERVER_VCS_SERV foreign key (server_name)
      references vcs_servers (server_name)
      on delete restrict on update restrict;

alter table vcs_database_setup
   add constraint FK_VCS_DATA_DS_SPACE__VCS_SPAC foreign key (space_name)
      references vcs_spaces (space_name)
      on delete restrict on update restrict;

alter table vcs_databases_releases
   add constraint FK_VCS_DATA_DB_VERSIO_VCS_DATA foreign key (db_name)
      references vcs_databases (db_name)
      on delete restrict on update restrict;

