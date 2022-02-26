/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     10/13/2021 7:12:10 PM                        */
/*==============================================================*/


drop table pr_actions_ref;

drop table pr_actions_results_ref;

drop index idx_pr_batch_template;

drop table pr_batches_setup;

drop table pr_batches_setup_hist;

drop table pr_batches_templates;

drop table pr_batches_templates_hist;

drop table pr_batches_types_ref;

drop table pr_bot_statuses_ref;

drop table pr_bots;

drop table pr_bots_hist;

drop table pr_comments;

drop table pr_order_statuses_ref;

drop table pr_orders_actions;

drop table pr_orders_actions_hist;

drop table pr_package_orders;

drop table pr_package_orders_hist;

drop table pr_packages;

drop table pr_packages_hist;

drop table pr_providers_ref;

drop table pr_used_comments;

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

drop sequence seq_pr_general;

create sequence seq_pr_general
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

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
/* Table: pr_actions_ref                                        */
/*==============================================================*/
create table pr_actions_ref (
   act_ref_id           TIdCode              not null,
   act_ref_name         TStr100              not null,
   constraint PK_PR_ACTIONS_REF primary key (act_ref_id)
);

comment on table pr_actions_ref is
'Атомарные действия';

/*==============================================================*/
/* Table: pr_actions_results_ref                                */
/*==============================================================*/
create table pr_actions_results_ref (
   action_result_id     TIdCode              not null,
   action_result_name   TStr100              not null,
   constraint PK_PR_ACTIONS_RESULTS_REF primary key (action_result_id)
);

comment on table pr_actions_results_ref is
'Результат действия';

/*==============================================================*/
/* Table: pr_batches_setup                                      */
/*==============================================================*/
create table pr_batches_setup (
   batch_setup_id       TIdCode              not null,
   batch_template_id    TIdCode              not null,
   act_ref_id           TIdCode              not null,
   execution_order      TIdCode              not null,
   min_delay            TIdCode              not null,
   max_delay            TIdCode              not null,
   actual_date          TDateTime            not null,
   is_actual            TBoolean             not null,
   batch_note           TStr200              null,
   constraint PK_PR_BATCHES_SETUP primary key (batch_setup_id)
);

comment on table pr_batches_setup is
'Настройка батчей';

/*==============================================================*/
/* Index: idx_pr_batch_template                                 */
/*==============================================================*/
create  index idx_pr_batch_template on pr_batches_setup (
batch_template_id
);

/*==============================================================*/
/* Table: pr_batches_setup_hist                                 */
/*==============================================================*/
create table pr_batches_setup_hist (
   batch_setup_id       TIdCode              null,
   batch_template_id    TIdCode              null,
   act_ref_id           TIdCode              null,
   execution_order      TIdCode              null,
   min_delay            TIdCode              null,
   max_delay            TIdCode              null,
   actual_date          TDateTime            null,
   is_actual            TBoolean             null,
   batch_note           TStr200              null
);

comment on table pr_batches_setup_hist is
'Набор батчей - hist';

/*==============================================================*/
/* Table: pr_batches_templates                                  */
/*==============================================================*/
create table pr_batches_templates (
   batch_template_id    TIdCode              not null,
   actual_date          TDateTime            not null,
   batch_type_id        TIdCode              not null,
   provider_id          TIdCode              not null,
   template_name        TStr200              not null,
   is_actual            TBoolean             not null,
   constraint PK_PR_BATCHES_TEMPLATES primary key (batch_template_id)
);

comment on table pr_batches_templates is
'Набор батчей';

/*==============================================================*/
/* Table: pr_batches_templates_hist                             */
/*==============================================================*/
create table pr_batches_templates_hist (
   batch_template_id    TIdCode              null,
   actual_date          TDateTime            null,
   batch_type_id        TIdCode              null,
   provider_id          TIdCode              null,
   template_name        TStr200              null,
   is_actual            TBoolean             null
);

comment on table pr_batches_templates_hist is
'Набор батчей - hist';

/*==============================================================*/
/* Table: pr_batches_types_ref                                  */
/*==============================================================*/
create table pr_batches_types_ref (
   batch_type_id        TIdCode              not null,
   batch_type_name      TStr100              not null,
   constraint PK_PR_BATCHES_TYPES_REF primary key (batch_type_id)
);

comment on table pr_batches_types_ref is
'Типы батчей';

/*==============================================================*/
/* Table: pr_bot_statuses_ref                                   */
/*==============================================================*/
create table pr_bot_statuses_ref (
   bot_status_id        TIdCode              not null,
   bot_status_name      TStr100              not null,
   constraint PK_PR_BOT_STATUSES_REF primary key (bot_status_id)
);

comment on table pr_bot_statuses_ref is
'Статусы ботов';

/*==============================================================*/
/* Table: pr_bots                                               */
/*==============================================================*/
create table pr_bots (
   bot_id               TIdCode              not null,
   actual_date          TDateTime            not null,
   bot_status_id        TIdCode              not null,
   bot_provider_id      TIdCode              not null,
   pass                 TStr100              null,
   login                TStr100              null,
   session_id           TStr100              null,
   bot_note             TStr200              null,
   constraint PK_PR_BOTS primary key (bot_id)
);

comment on table pr_bots is
'Боты для выполнения действий';

/*==============================================================*/
/* Table: pr_bots_hist                                          */
/*==============================================================*/
create table pr_bots_hist (
   bot_id               TIdCode              null,
   actual_date          TDateTime            not null,
   bot_status_id        TIdCode              null,
   bot_provider_id      TIdCode              null,
   pass                 TStr100              null,
   login                TStr100              null,
   session_id           TStr100              null,
   bot_note             TStr200              null
);

comment on table pr_bots_hist is
'Боты для выполнения действий - hist';

/*==============================================================*/
/* Table: pr_comments                                           */
/*==============================================================*/
create table pr_comments (
   comment_id           TIdCode              not null,
   create_date          TDateTime            not null,
   comment_source       TStr10000            not null,
   constraint PK_PR_COMMENTS primary key (comment_id)
);

comment on table pr_comments is
'Картотека комментариев';

/*==============================================================*/
/* Table: pr_order_statuses_ref                                 */
/*==============================================================*/
create table pr_order_statuses_ref (
   order_status_id      TIdCode              not null,
   order_status_name    TStr100              not null,
   constraint PK_PR_ORDER_STATUSES_REF primary key (order_status_id)
);

comment on table pr_order_statuses_ref is
'Статус заказа';

/*==============================================================*/
/* Table: pr_orders_actions                                     */
/*==============================================================*/
create table pr_orders_actions (
   action_id            TIdCode              not null,
   actual_date          TDateTime            not null,
   act_ref_id           TIdCode              not null,
   batch_setup_id       TIdCode              not null,
   order_id             TIdCode              not null,
   action_result_id     TIdCode              not null,
   used_ip              TStr20               null,
   action_start_date    TDateTime            null,
   action_finish_date   TDateTime            null,
   err_msg              TStr10000            null,
   constraint PK_PR_ORDERS_ACTIONS primary key (action_id)
);

comment on table pr_orders_actions is
'Действия по заказу';

/*==============================================================*/
/* Table: pr_orders_actions_hist                                */
/*==============================================================*/
create table pr_orders_actions_hist (
   action_id            TIdCode              not null,
   actual_date          TDateTime            not null,
   act_ref_id           TIdCode              not null,
   batch_setup_id       TIdCode              not null,
   used_ip              TStr20               null,
   order_id             TIdCode              null,
   action_result_id     TIdCode              null,
   action_start_date    TDateTime            null,
   action_finish_date   TDateTime            null,
   err_msg              TStr10000            null
);

comment on table pr_orders_actions_hist is
'Действия по заказу - hist';

/*==============================================================*/
/* Table: pr_package_orders                                     */
/*==============================================================*/
create table pr_package_orders (
   order_id             TIdCode              not null,
   order_status_id      TIdCode              not null,
   actual_date          TDateTime            not null,
   package_id           TIdCode              not null,
   batch_template_id    TIdCode              not null,
   bot_id               TIdCode              null,
   order_batches_amount TIdCode              not null,
   order_name           TStr100              null,
   success_batches_amount TIdCode              null,
   fail_batches_amount  TIdCode              null,
   exec_start_date      TDateTime            null,
   exec_finish_date     TDateTime            null,
   exec_last_date       TDateTime            null,
   order_note           TStr200              null,
   constraint PK_PR_PACKAGE_ORDERS primary key (order_id)
);

comment on table pr_package_orders is
'Задания на пакеты';

/*==============================================================*/
/* Table: pr_package_orders_hist                                */
/*==============================================================*/
create table pr_package_orders_hist (
   order_id             TIdCode              null,
   actual_date          TDateTime            null,
   package_id           TIdCode              null,
   batch_template_id    TIdCode              null,
   order_batches_amount TIdCode              null,
   fail_batches_amount  TIdCode              null,
   success_batches_amount TIdCode              null,
   order_status_id      TIdCode              null,
   bot_id               TIdCode              null,
   order_name           TStr100              null,
   exec_start_date      TDateTime            null,
   exec_finish_date     TDateTime            null,
   exec_last_date       TDateTime            null,
   order_note           TStr200              null
);

comment on table pr_package_orders_hist is
'Задания на пакеты - hist';

/*==============================================================*/
/* Table: pr_packages                                           */
/*==============================================================*/
create table pr_packages (
   package_id           TIdCode              not null,
   provider_id          TIdCode              not null,
   package_name         TStr100              not null,
   actual_date          TDateTime            not null,
   is_actual            TBoolean             not null,
   package_note         TStr200              null,
   constraint PK_PR_PACKAGES primary key (package_id)
);

comment on table pr_packages is
'Набор пакетов';

/*==============================================================*/
/* Table: pr_packages_hist                                      */
/*==============================================================*/
create table pr_packages_hist (
   package_id           TIdCode              null,
   package_name         TStr100              null,
   actual_date          TDateTime            null,
   is_actual            TBoolean             null,
   package_note         TStr200              null,
   provider_id          TIdCode              not null
);

comment on table pr_packages_hist is
'Набор пакетов - hist';

/*==============================================================*/
/* Table: pr_providers_ref                                      */
/*==============================================================*/
create table pr_providers_ref (
   provider_id          TIdCode              not null,
   provider_name        TStr100              not null,
   constraint PK_PR_PROVIDERS_REF primary key (provider_id)
);

comment on table pr_providers_ref is
'Провайдеры приложений';

/*==============================================================*/
/* Table: pr_used_comments                                      */
/*==============================================================*/
create table pr_used_comments (
   comment_id           TIdCode              not null,
   action_id            TIdCode              not null,
   constraint PK_PR_USED_COMMENTS primary key (comment_id)
);

comment on table pr_used_comments is
'Использованные комментарии';

alter table pr_batches_setup
   add constraint FK_pr_bs_act_ref_id foreign key (act_ref_id)
      references pr_actions_ref (act_ref_id)
      on delete restrict on update restrict;

alter table pr_batches_setup
   add constraint FK_pr_bs_template_id foreign key (batch_template_id)
      references pr_batches_templates (batch_template_id)
      on delete restrict on update restrict;

alter table pr_batches_setup_hist
   add constraint FK_pr_bsh_setup_id foreign key (batch_setup_id)
      references pr_batches_setup (batch_setup_id)
      on delete restrict on update restrict;

alter table pr_batches_templates
   add constraint FK_pr_bt_batch_type foreign key (batch_type_id)
      references pr_batches_types_ref (batch_type_id)
      on delete restrict on update restrict;

alter table pr_batches_templates
   add constraint FK_pr_bt_provider_id foreign key (provider_id)
      references pr_providers_ref (provider_id)
      on delete restrict on update restrict;

alter table pr_batches_templates_hist
   add constraint FK_pr_bth_template_id foreign key (batch_template_id)
      references pr_batches_templates (batch_template_id)
      on delete restrict on update restrict;

alter table pr_bots
   add constraint FK_pr_bots_bot_status_id foreign key (bot_status_id)
      references pr_bot_statuses_ref (bot_status_id)
      on delete restrict on update restrict;

alter table pr_bots_hist
   add constraint FK_pr_bh_bot_id foreign key (bot_id)
      references pr_bots (bot_id)
      on delete restrict on update restrict;

alter table pr_orders_actions
   add constraint FK_pr_oa_order_id foreign key (order_id)
      references pr_package_orders (order_id)
      on delete restrict on update restrict;

alter table pr_orders_actions
   add constraint FK_pr_oa_result_id foreign key (action_result_id)
      references pr_actions_results_ref (action_result_id)
      on delete restrict on update restrict;

alter table pr_orders_actions
   add constraint FK_pr_oa_setup_id foreign key (batch_setup_id)
      references pr_batches_setup (batch_setup_id)
      on delete restrict on update restrict;

alter table pr_orders_actions_hist
   add constraint FK_pr_oah_action_id foreign key (action_id)
      references pr_orders_actions (action_id)
      on delete restrict on update restrict;

alter table pr_package_orders
   add constraint FK_pr_pkdg_bot_id foreign key (bot_id)
      references pr_bots (bot_id)
      on delete restrict on update restrict;

alter table pr_package_orders
   add constraint FK_pr_pkg_order_status_id foreign key (order_status_id)
      references pr_order_statuses_ref (order_status_id)
      on delete restrict on update restrict;

alter table pr_package_orders
   add constraint FK_pr_pkg_orders_pkgid foreign key (package_id)
      references pr_packages (package_id)
      on delete restrict on update restrict;

alter table pr_package_orders
   add constraint FK_pkg_orders_template_id foreign key (batch_template_id)
      references pr_batches_templates (batch_template_id)
      on delete restrict on update restrict;

alter table pr_package_orders_hist
   add constraint FK_pr_pkg_ordersh_order_id foreign key (order_id)
      references pr_package_orders (order_id)
      on delete restrict on update restrict;

alter table pr_packages
   add constraint FK_pr_pkg_provider_id foreign key (provider_id)
      references pr_providers_ref (provider_id)
      on delete restrict on update restrict;

alter table pr_packages_hist
   add constraint FK_pr_bsh_setup_id foreign key (package_id)
      references pr_packages (package_id)
      on delete restrict on update restrict;

alter table pr_used_comments
   add constraint FK_pr_oa_action_id foreign key (action_id)
      references pr_orders_actions (action_id)
      on delete restrict on update restrict;

alter table pr_used_comments
   add constraint FK_pr_uc_comment_id foreign key (comment_id)
      references pr_comments (comment_id)
      on delete restrict on update restrict;

