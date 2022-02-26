/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     11/5/2021 8:19:55 PM                         */
/*==============================================================*/


drop table tkn_applications;

drop table tkn_issue_cards;

drop table tkn_servers;

drop table tkn_servers_events;

drop table tkn_servers_events_ref;

drop table tkn_servers_hist;

drop table tkn_servers_statuses_ref;

drop sequence seq_tkn_card_id;

drop sequence seq_tkn_servers;

create sequence seq_tkn_card_id
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_tkn_servers
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

/*==============================================================*/
/* Table: tkn_applications                                      */
/*==============================================================*/
create table tkn_applications (
   application_id       TInteger             not null,
   application_code     TStr10               not null,
   application_name     TStr128              not null,
   constraint PK_TKN_APPLICATIONS primary key (application_id),
   constraint AK_TKN_APP_CODE unique (application_code)
);

comment on table tkn_applications is
'Используемые приложения';

/*==============================================================*/
/* Table: tkn_issue_cards                                       */
/*==============================================================*/
create table tkn_issue_cards (
   card_id              TIdBigCode           not null,
   issue_date           TDateTime            not null,
   valid_until          TDateTime            not null,
   token_card           TStr2000             not null,
   application_id       TIdCode              not null,
   request_id           TStr2000             not null,
   tag                  TStr2000             null,
   constraint PK_TKN_ISSUE_CARDS primary key (card_id)
);

comment on table tkn_issue_cards is
'Выданные токены';

/*==============================================================*/
/* Table: tkn_servers                                           */
/*==============================================================*/
create table tkn_servers (
   server_id            TIdCode              not null,
   pid                  TIdBigCode           not null,
   actual_date          TDateTime            not null,
   server_address       TStr100              not null,
   registry_date        TDateTime            not null,
   close_date           TDateTime            null,
   reboot_date          TStr2000             null,
   deadline_date        TDateTime            not null,
   application_id       TIdCode              not null,
   user_capacity        TInteger             not null,
   country_code         TStr2                not null,
   server_status_id     TIdCode              not null,
   constraint PK_TKN_SERVERS primary key (server_id)
);

comment on table tkn_servers is
'Регистрированные серверы';

/*==============================================================*/
/* Table: tkn_servers_events                                    */
/*==============================================================*/
create table tkn_servers_events (
   server_id            TIdCode              not null,
   server_event_id      TIdCode              not null,
   server_event_date    TDateTime            not null,
   note                 TStr2000             null,
   constraint PK_TKN_SERVERS_EVENTS primary key (server_id)
);

comment on table tkn_servers_events is
'События на сервере';

/*==============================================================*/
/* Table: tkn_servers_events_ref                                */
/*==============================================================*/
create table tkn_servers_events_ref (
   server_event_id      TInteger             not null,
   server_event_name    TStr128              not null,
   constraint PK_TKN_SERVERS_EVENTS_REF primary key (server_event_id)
);

comment on table tkn_servers_events_ref is
'Справочник событий сервера';

/*==============================================================*/
/* Table: tkn_servers_hist                                      */
/*==============================================================*/
create table tkn_servers_hist (
   server_id            TIdCode              not null,
   actual_date          TDateTime            not null,
   pid                  TIdBigCode           not null,
   server_address       TStr100              null,
   registry_date        TDateTime            null,
   close_date           TDateTime            null,
   reboot_date          TStr2000             null,
   deadline_date        TDateTime            null,
   application_id       TIdCode              null,
   user_capacity        TInteger             not null,
   country_code         TStr2                not null,
   server_status_id     TIdCode              null
);

comment on table tkn_servers_hist is
'Регистрированные серверы - hist';

/*==============================================================*/
/* Table: tkn_servers_statuses_ref                              */
/*==============================================================*/
create table tkn_servers_statuses_ref (
   server_status_id     TInteger             not null,
   server_status_name   TStr128              not null,
   constraint PK_TKN_SERVERS_STATUSES_REF primary key (server_status_id)
);

comment on table tkn_servers_statuses_ref is
'Статусы сервера';

alter table tkn_issue_cards
   add constraint FK_tkn_ic_app_id foreign key (application_id)
      references tkn_applications (application_id)
      on delete restrict on update restrict;

alter table tkn_servers
   add constraint FK_tkn_server_app_id foreign key (application_id)
      references tkn_applications (application_id)
      on delete restrict on update restrict;

alter table tkn_servers
   add constraint FK_tkn_servers_status_id foreign key (server_status_id)
      references tkn_servers_statuses_ref (server_status_id)
      on delete restrict on update restrict;

alter table tkn_servers_events
   add constraint FK_tkn_se_event_id foreign key (server_event_id)
      references tkn_servers_events_ref (server_event_id)
      on delete restrict on update restrict;

alter table tkn_servers_events
   add constraint FK_tkn_server_hist_id foreign key (server_id)
      references tkn_servers (server_id)
      on delete restrict on update restrict;

alter table tkn_servers_hist
   add constraint FK_tkn_server_hist_id foreign key (server_id)
      references tkn_servers (server_id)
      on delete restrict on update restrict;

