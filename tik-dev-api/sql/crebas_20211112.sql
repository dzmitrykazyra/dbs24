/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     18.11.2021 16:43:43                          */
/*==============================================================*/


drop table tda_contract_status_ref;

drop table tda_contracts_use;

drop table tda_daily_endpoints_use;

drop table tda_developers;

drop table tda_developers_contracts;

drop table tda_developers_contracts_hist;

drop table tda_developers_hist;

drop table tda_device_statuses_ref;

drop index tda_ea_contract_id;

drop index tda_ea_tik_accounts;

drop table tda_endpoints_actions;

drop table tda_endpoints_ref;

drop table tda_endpoints_results_ref;

drop table tda_endpoints_scopes_ref;

drop table tda_monthly_endpoints_use;

drop table tda_ta_granted_scopes;

drop table tda_ta_granted_scopes_hist;

drop table tda_tariff_plan_status_ref;

drop table tda_tariff_plan_type_ref;

drop table tda_tariff_plans;

drop table tda_tariff_plans_hist;

drop table tda_tik_account_statuses_ref;

drop table tda_tik_accounts;

drop table tda_tik_accounts_hist;

drop table tda_tik_devices;

drop table tda_tik_devices_hist;

/*==============================================================*/
/* Table: tda_contract_status_ref                               */
/*==============================================================*/
create table tda_contract_status_ref (
   contract_status_id   TIdCode              not null,
   contract_status_name TStr100              not null,
   constraint PK_TDA_CONTRACT_STATUS_REF primary key (contract_status_id)
);

/*==============================================================*/
/* Table: tda_contracts_use                                     */
/*==============================================================*/
create table tda_contracts_use (
   contract_id          TIdBigCode           not null,
   used_oauth_users     TIdCode              not null,
   constraint PK_TDA_CONTRACTS_USE primary key (contract_id)
);

comment on table tda_contracts_use is
'Использование контракта';

/*==============================================================*/
/* Table: tda_daily_endpoints_use                               */
/*==============================================================*/
create table tda_daily_endpoints_use (
   contract_id          TIdBigCode           not null,
   actual_date          TDateTime            not null,
   used_endpoints_amt   TIdCode              not null,
   constraint PK_TDA_DAILY_ENDPOINTS_USE primary key (contract_id, actual_date)
);

comment on table tda_daily_endpoints_use is
'Дневное использование эндпоинтов';

/*==============================================================*/
/* Table: tda_developers                                        */
/*==============================================================*/
create table tda_developers (
   developer_id         TIdBigCode           not null,
   actual_date          TDateTime            not null,
   business_name        TStr200              not null,
   email                TStr200              not null,
   website              TStr200              not null,
   api_key              TStr400              not null,
   oauth_client_id      TStr400              not null,
   country_code         TStr2                not null,
   constraint PK_TDA_DEVELOPERS primary key (developer_id)
);

comment on table tda_developers is
'Разработчики-пользователи';

/*==============================================================*/
/* Table: tda_developers_contracts                              */
/*==============================================================*/
create table tda_developers_contracts (
   contract_id          TIdBigCode           not null,
   developer_id         TIdBigCode           not null,
   tariff_plan_id       TIdBigCode           not null,
   actual_date          TDateTime            not null,
   contract_status_id   TIdCode              not null,
   begin_date           TDateTime            not null,
   end_date             TDateTime            not null,
   cancel_date          TDateTime            null,
   constraint PK_TDA_DEVELOPERS_CONTRACTS primary key (contract_id)
);

comment on table tda_developers_contracts is
'Договора с разработчикам';

/*==============================================================*/
/* Table: tda_developers_contracts_hist                         */
/*==============================================================*/
create table tda_developers_contracts_hist (
   contract_id          TIdBigCode           null,
   developer_id         TIdBigCode           null,
   tariff_plan_id       TIdBigCode           null,
   actual_date          TDateTime            null,
   contract_status_id   TIdCode              null,
   begin_date           TDateTime            null,
   end_date             TDateTime            null,
   cancel_date          TDateTime            null
);

comment on table tda_developers_contracts_hist is
'Договора с разработчикам';

/*==============================================================*/
/* Table: tda_developers_hist                                   */
/*==============================================================*/
create table tda_developers_hist (
   developer_id         TIdBigCode           null,
   actual_date          TDateTime            not null,
   business_name        TStr200              null,
   email                TStr200              null,
   website              TStr200              null,
   api_key              TStr400              null,
   oauth_client_id      TStr400              null,
   country_code         TStr2                null
);

comment on table tda_developers_hist is
'Разработчики-пользователи';

/*==============================================================*/
/* Table: tda_device_statuses_ref                               */
/*==============================================================*/
create table tda_device_statuses_ref (
   device_status_id     TIdCode              not null,
   device_status_name   TStr100              not null,
   constraint PK_TDA_DEVICE_STATUSES_REF primary key (device_status_id)
);

comment on table tda_device_statuses_ref is
'Статусы телефона';

/*==============================================================*/
/* Table: tda_endpoints_actions                                 */
/*==============================================================*/
create table tda_endpoints_actions (
   endpoint_action_id   TIdBigCode           not null,
   endpoint_ref_id      TIdBigCode           not null,
   execution_date       TDateTime            not null,
   contract_id          TIdBigCode           not null,
   endpoint_result_id   TIdCode              not null,
   tik_account_id       TIdBigCode           not null,
   tik_device_id        TIdBigCode           not null,
   request_url          TStr200              not null,
   http_response_code   TIdCode              not null,
   proxy_country        TStr3                not null,
   proxy_exit_ip        TStr10               null,
   execution_time_millis TIdCode              not null,
   request_body         TStr10000            not null,
   bandwidth_bs         TIdCode              not null,
   headers              TStr2000             null,
   error_log            TStr10000            null,
   constraint PK_TDA_ENDPOINTS_ACTIONS primary key (endpoint_action_id)
);

comment on table tda_endpoints_actions is
'вызовы эндопоинтов';

/*==============================================================*/
/* Index: tda_ea_tik_accounts                                   */
/*==============================================================*/
create  index tda_ea_tik_accounts on tda_endpoints_actions (
tik_account_id
);

/*==============================================================*/
/* Index: tda_ea_contract_id                                    */
/*==============================================================*/
create  index tda_ea_contract_id on tda_endpoints_actions (
contract_id
);

/*==============================================================*/
/* Table: tda_endpoints_ref                                     */
/*==============================================================*/
create table tda_endpoints_ref (
   endpoint_ref_id      TIdCode              not null,
   endpoint_scope_id    TIdCode              not null,
   is_premium           TBoolean             not null,
   url_template         TStr200              not null,
   is_deprecated        TBoolean             not null,
   description          TStr400              null,
   constraint PK_TDA_ENDPOINTS_REF primary key (endpoint_ref_id)
);

comment on table tda_endpoints_ref is
'Рабочие эндпоинты';

/*==============================================================*/
/* Table: tda_endpoints_results_ref                             */
/*==============================================================*/
create table tda_endpoints_results_ref (
   endpoint_result_id   TIdCode              not null,
   endpoint_result_name TStr100              not null,
   constraint PK_TDA_ENDPOINTS_RESULTS_REF primary key (endpoint_result_id)
);

/*==============================================================*/
/* Table: tda_endpoints_scopes_ref                              */
/*==============================================================*/
create table tda_endpoints_scopes_ref (
   endpoint_scope_id    TIdCode              not null,
   endpoint_scope_name  TStr100              not null,
   constraint PK_TDA_ENDPOINTS_SCOPES_REF primary key (endpoint_scope_id)
);

comment on table tda_endpoints_scopes_ref is
'Endpoints Scope';

/*==============================================================*/
/* Table: tda_monthly_endpoints_use                             */
/*==============================================================*/
create table tda_monthly_endpoints_use (
   contract_id          TIdBigCode           not null,
   begin_date           TDateTime            not null,
   end_date             TDateTime            not null,
   used_bandwidth_mb    TIdCode              not null,
   constraint PK_TDA_MONTHLY_ENDPOINTS_USE primary key (contract_id, begin_date)
);

comment on table tda_monthly_endpoints_use is
'Месячное использование эндпоинтов';

/*==============================================================*/
/* Table: tda_ta_granted_scopes                                 */
/*==============================================================*/
create table tda_ta_granted_scopes (
   grant_id             TIdBigCode           not null,
   tik_account_id       TIdBigCode           not null,
   endpoint_scope_id    TIdCode              not null,
   is_granted           TBoolean             not null,
   actual_date          TDateTime            not null,
   constraint PK_TDA_TA_GRANTED_SCOPES primary key (grant_id),
   constraint AK_KEY_TDA_ACC_GRANTED_SCOPE unique (tik_account_id, endpoint_scope_id)
);

comment on table tda_ta_granted_scopes is
'Accounts scopes';

/*==============================================================*/
/* Table: tda_ta_granted_scopes_hist                            */
/*==============================================================*/
create table tda_ta_granted_scopes_hist (
   grant_id             TIdBigCode           null,
   tik_account_id       TIdBigCode           null,
   endpoint_scope_id    TIdCode              null,
   is_granted           TBoolean             null,
   actual_date          TDateTime            null
);

comment on table tda_ta_granted_scopes_hist is
'Accounts scopes - hists
';

/*==============================================================*/
/* Table: tda_tariff_plan_status_ref                            */
/*==============================================================*/
create table tda_tariff_plan_status_ref (
   tariff_plan_status_id TIdCode              not null,
   tariff_plan_status_name TStr100              not null,
   constraint PK_TDA_TARIFF_PLAN_STATUS_REF primary key (tariff_plan_status_id)
);

comment on table tda_tariff_plan_status_ref is
'Статус тарифного плана';

/*==============================================================*/
/* Table: tda_tariff_plan_type_ref                              */
/*==============================================================*/
create table tda_tariff_plan_type_ref (
   tariff_plan_type_id  TIdCode              not null,
   tariff_plan_type_name TStr100              not null,
   constraint PK_TDA_TARIFF_PLAN_TYPE_REF primary key (tariff_plan_type_id)
);

comment on table tda_tariff_plan_type_ref is
'Тип тарифного плана';

/*==============================================================*/
/* Table: tda_tariff_plans                                      */
/*==============================================================*/
create table tda_tariff_plans (
   tariff_plan_id       TIdBigCode           not null,
   tariff_plan_status_id TIdCode              not null,
   tariff_plan_type_id  TIdCode              not null,
   actual_date          TDateTime            not null,
   usd_tariff_price     TMoney               not null,
   trial_days           TIdCode              not null,
   daily_endpoints_limit TIdCode              not null,
   monthly_oauth_users_limit TIdCode              not null,
   monthly_bandwidth_mb_limit TIdCode              not null,
   use_premium_points   TBoolean             not null,
   tp_name              TStr100              not null,
   tp_note              TStr2000             null,
   constraint PK_TDA_TARIFF_PLANS primary key (tariff_plan_id)
);

comment on table tda_tariff_plans is
'Картотека тарифный планов';

/*==============================================================*/
/* Table: tda_tariff_plans_hist                                 */
/*==============================================================*/
create table tda_tariff_plans_hist (
   tariff_plan_id       TIdBigCode           null,
   tariff_plan_type_id  TIdCode              null,
   actual_date          TDateTime            null,
   usd_tariff_price     TMoney               null,
   trial_days           TIdCode              null,
   daily_endpoints_limit TIdCode              null,
   monthly_oauth_users_limit TIdCode              null,
   monthly_bandwidth_mb_limit TIdCode              null,
   use_premium_points   TBoolean             null,
   tp_name              TStr100              null,
   tp_note              TStr2000             null
);

comment on table tda_tariff_plans_hist is
'Картотека тарифный планов - hist';

/*==============================================================*/
/* Table: tda_tik_account_statuses_ref                          */
/*==============================================================*/
create table tda_tik_account_statuses_ref (
   tik_account_status_id TIdCode              not null,
   tik_account_status_name TStr100              not null,
   constraint PK_TDA_TIK_ACCOUNT_STATUSES primary key (tik_account_status_id)
);

comment on table tda_tik_account_statuses_ref is
'Статусы аккаунтов тик тока';

/*==============================================================*/
/* Table: tda_tik_accounts                                      */
/*==============================================================*/
create table tda_tik_accounts (
   tik_account_id       TIdBigCode           not null,
   tik_account_status_id TIdCode              not null,
   developer_id         TIdBigCode           not null,
   actual_date          TDateTime            not null,
   tik_username         TStr200              not null,
   tik_pass             TStr200              not null,
   tik_sec_user_id      TStr100              not null,
   tik_email            TStr200              not null,
   tik_account_auth_key TStr200              not null,
   tik_attributes       TStr40               null,
   constraint PK_TDA_TIK_ACCOUNTS primary key (tik_account_id)
);

comment on table tda_tik_accounts is
'Аккаунты тиктока';

/*==============================================================*/
/* Table: tda_tik_accounts_hist                                 */
/*==============================================================*/
create table tda_tik_accounts_hist (
   tik_account_id       TIdBigCode           null,
   tik_account_status_id TIdCode              null,
   developer_id         TIdBigCode           null,
   actual_date          TDateTime            null,
   tik_username         TStr200              null,
   tik_pass             TStr200              null,
   tik_sec_user_id      TStr100              null,
   tik_email            TStr200              null,
   tik_account_auth_key TStr200              null,
   tik_attributes       TStr40               null
);

comment on table tda_tik_accounts_hist is
'Аккаунты тиктока - hist';

/*==============================================================*/
/* Table: tda_tik_devices                                       */
/*==============================================================*/
create table tda_tik_devices (
   tik_device_id        TIdBigCode           not null,
   actual_date          TDateTime            not null,
   device_id            TStr100              not null,
   device_status_id     TIdCode              not null,
   install_id           TStr100              not null,
   apk_attributes       TBinary              null,
   apk_hash_id          TStr400              null,
   constraint PK_TDA_TIK_DEVICES primary key (tik_device_id),
   constraint AK_KEY_TIK_PHONE unique (device_id, install_id)
);

comment on table tda_tik_devices is
'Телефоне тик ток';

/*==============================================================*/
/* Table: tda_tik_devices_hist                                  */
/*==============================================================*/
create table tda_tik_devices_hist (
   tik_device_id        TIdBigCode           null,
   actual_date          TDateTime            null,
   device_id            TStr100              null,
   device_status_id     TIdCode              null,
   install_id           TStr100              null,
   apk_attributes       TBinary              null,
   apk_hash_id          TStr400              null
);

comment on table tda_tik_devices_hist is
'Телефоне тик ток';

alter table tda_contracts_use
   add constraint FK_tda_cu_contract_id foreign key (contract_id)
      references tda_developers_contracts (contract_id)
      on delete restrict on update restrict;

alter table tda_daily_endpoints_use
   add constraint FK_tda_deu_contract_id foreign key (contract_id)
      references tda_contracts_use (contract_id)
      on delete restrict on update restrict;

alter table tda_developers_contracts
   add constraint FK_tda_contract_dev_id foreign key (developer_id)
      references tda_developers (developer_id)
      on delete restrict on update restrict;

alter table tda_developers_contracts
   add constraint FK_tda_dc_status_id foreign key (contract_status_id)
      references tda_contract_status_ref (contract_status_id)
      on delete restrict on update restrict;

alter table tda_developers_contracts
   add constraint FK_tda_cu_limit_id foreign key (tariff_plan_id)
      references tda_tariff_plans (tariff_plan_id)
      on delete restrict on update restrict;

alter table tda_developers_contracts_hist
   add constraint FK_tda_dch_contract_id foreign key (contract_id)
      references tda_developers_contracts (contract_id)
      on delete restrict on update restrict;

alter table tda_developers_hist
   add constraint FK_tda_dh_dev_id foreign key (developer_id)
      references tda_developers (developer_id)
      on delete restrict on update restrict;

alter table tda_endpoints_actions
   add constraint FK_tda_ep_actions_result_id foreign key (endpoint_result_id)
      references tda_endpoints_results_ref (endpoint_result_id)
      on delete restrict on update restrict;

alter table tda_endpoints_actions
   add constraint FK_tda_ep_device_id foreign key (tik_device_id)
      references tda_tik_devices (tik_device_id)
      on delete restrict on update restrict;

alter table tda_endpoints_actions
   add constraint FK_tda_epa_contract_id foreign key (contract_id)
      references tda_developers_contracts (contract_id)
      on delete restrict on update restrict;

alter table tda_endpoints_actions
   add constraint FK_tda_epa_ep_id foreign key (endpoint_ref_id)
      references tda_endpoints_ref (endpoint_ref_id)
      on delete restrict on update restrict;

alter table tda_endpoints_actions
   add constraint FK_tda_tik_account_id foreign key (tik_account_id)
      references tda_tik_accounts (tik_account_id)
      on delete restrict on update restrict;

alter table tda_endpoints_ref
   add constraint FK_tda_ep_scope_id foreign key (endpoint_scope_id)
      references tda_endpoints_scopes_ref (endpoint_scope_id)
      on delete restrict on update restrict;

alter table tda_monthly_endpoints_use
   add constraint FK_tda_meu_contract_id foreign key (contract_id)
      references tda_contracts_use (contract_id)
      on delete restrict on update restrict;

alter table tda_ta_granted_scopes
   add constraint FK_tda_ta_account_id foreign key (tik_account_id)
      references tda_tik_accounts (tik_account_id)
      on delete restrict on update restrict;

alter table tda_ta_granted_scopes
   add constraint FK_tda_ta_gs_scope_id foreign key (endpoint_scope_id)
      references tda_endpoints_scopes_ref (endpoint_scope_id)
      on delete restrict on update restrict;

alter table tda_ta_granted_scopes_hist
   add constraint FK_tda_ta_gsh_gran_id foreign key (grant_id)
      references tda_ta_granted_scopes (grant_id)
      on delete restrict on update restrict;

alter table tda_tariff_plans
   add constraint FK_tda_tp_status_id foreign key (tariff_plan_status_id)
      references tda_tariff_plan_status_ref (tariff_plan_status_id)
      on delete restrict on update restrict;

alter table tda_tariff_plans
   add constraint FK_tda_tp_type_id foreign key (tariff_plan_type_id)
      references tda_tariff_plan_type_ref (tariff_plan_type_id)
      on delete restrict on update restrict;

alter table tda_tariff_plans_hist
   add constraint FK_tda_tph_plan_id foreign key (tariff_plan_id)
      references tda_tariff_plans (tariff_plan_id)
      on delete restrict on update restrict;

alter table tda_tik_accounts
   add constraint FK_tda_ta_dev_id foreign key (developer_id)
      references tda_developers (developer_id)
      on delete restrict on update restrict;

alter table tda_tik_accounts
   add constraint FK_tda_tik_account_id foreign key (tik_account_status_id)
      references tda_tik_account_statuses_ref (tik_account_status_id)
      on delete restrict on update restrict;

alter table tda_tik_accounts_hist
   add constraint FK_tda_tah_account_id foreign key (tik_account_id)
      references tda_tik_accounts (tik_account_id)
      on delete restrict on update restrict;

alter table tda_tik_devices
   add constraint FK_tda_device_status_id foreign key (device_status_id)
      references tda_device_statuses_ref (device_status_id)
      on delete restrict on update restrict;

alter table tda_tik_devices_hist
   add constraint FK_TDA_TIK__TDA_DH_DE_TDA_TIK_ foreign key (tik_device_id)
      references tda_tik_devices (tik_device_id)
      on delete restrict on update restrict;

