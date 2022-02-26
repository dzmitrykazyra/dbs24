/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     9/22/2021 1:41:55 PM                         */
/*==============================================================*/


drop table wa_agent_statuses_ref;

drop index idx_wa_agents_phoneNum;

drop table wa_agents;

drop table wa_agents_hist;

drop table wa_app_settings;

drop table wa_app_settings_hist;

drop table wa_contract_statuses_ref;

drop table wa_contract_types_ref;

drop table wa_countries_ref;

drop table wa_device_sessions;

drop table wa_device_types_ref;

drop table wa_firebase_apps;

drop table wa_modify_reasons_ref;

drop table wa_package_details;

drop table wa_subscription_statuses_ref;

drop table wa_tariffs_plan_statuses_ref;

drop table wa_tariffs_plans;

drop table wa_tariffs_plans_hist;

drop table wa_users;

drop table wa_users_contracts;

drop table wa_users_contracts_hist;

drop table wa_users_devices;

drop table wa_users_devices_android;

drop table wa_users_devices_android_hist;

drop table wa_users_devices_hist;

drop table wa_users_devices_ios;

drop table wa_users_devices_ios_hist;

drop table wa_users_hist;

drop index wa_idx_actual_date;

drop table wa_users_subscriptions;

drop index wa_users_ss_hist;

drop table wa_users_subscriptions_hist;

drop index wa_ussa_ss_id_ad;

drop table wa_uss_activities;

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

drop sequence seq_wa_Activities;

drop sequence seq_wa_Agents;

drop sequence seq_wa_DeviceSessions;

drop sequence seq_wa_FirebaseApps;

drop sequence seq_wa_Payments;

drop sequence seq_wa_Subscriptions;

drop sequence seq_wa_Tariff;

drop sequence seq_wa_TariffPlans;

drop sequence seq_wa_Tokens;

drop sequence seq_wa_UserContracts;

drop sequence seq_wa_UserDevices;

drop sequence seq_wa_Users;

drop sequence seq_wa_settings;

create sequence seq_wa_Activities
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_wa_Agents
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_wa_DeviceSessions
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_wa_FirebaseApps
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_wa_Payments
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_wa_Subscriptions
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_wa_Tariff
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_wa_TariffPlans
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_wa_Tokens
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_wa_UserContracts
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_wa_UserDevices
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_wa_Users
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_wa_settings
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
/* Table: wa_agent_statuses_ref                                 */
/*==============================================================*/
create table wa_agent_statuses_ref (
   agent_status_id      TidByteCode          not null,
   agent_status_name    TStr100              not null,
   constraint PK_WA_AGENT_STATUSES_REF primary key (agent_status_id)
);

comment on table wa_agent_statuses_ref is
'Статусы  агентов';

/*==============================================================*/
/* Table: wa_agents                                             */
/*==============================================================*/
create table wa_agents (
   agent_id             TIdCode              not null,
   agent_status_id      TIdCode              not null,
   phone_num            TStr20               not null,
   payload              TStr2000             null,
   created              TDateTime            not null,
   actual_date          TDateTime            not null,
   agent_note           TStr200              null,
   constraint PK_WA_AGENTS primary key (agent_id)
);

comment on table wa_agents is
'Агенты мониторинга';

/*==============================================================*/
/* Index: idx_wa_agents_phoneNum                                */
/*==============================================================*/
create unique index idx_wa_agents_phoneNum on wa_agents (
phone_num
);

/*==============================================================*/
/* Table: wa_agents_hist                                        */
/*==============================================================*/
create table wa_agents_hist (
   agent_id             TIdCode              not null,
   actual_date          TDateTime            not null,
   phone_num            TStr20               not null,
   payload              TStr2000             null,
   created              TDateTime            not null,
   agent_status_id      TidByteCode          not null,
   agent_note           TStr200              null
);

comment on table wa_agents_hist is
'Агенты мониторинга история';

/*==============================================================*/
/* Table: wa_app_settings                                       */
/*==============================================================*/
create table wa_app_settings (
   app_setting_id       TIdCode              not null,
   actual_date          TDateTime            not null,
   package_name         TStr100              not null,
   email                TStr200              not null,
   min_version_code     TStr100              not null,
   min_version          TStr100              not null,
   constraint PK_WA_APP_SETTINGS primary key (app_setting_id)
);

comment on table wa_app_settings is
'WA App Settings';

/*==============================================================*/
/* Table: wa_app_settings_hist                                  */
/*==============================================================*/
create table wa_app_settings_hist (
   app_setting_id       TIdCode              not null,
   actual_date          TDateTime            null,
   package_name         TStr100              null,
   email                TStr200              null,
   min_version_code     TStr100              null,
   min_version          TStr100              null
);

comment on table wa_app_settings_hist is
'WA App Settings - hist';

/*==============================================================*/
/* Table: wa_contract_statuses_ref                              */
/*==============================================================*/
create table wa_contract_statuses_ref (
   contract_status_id   TidByteCode          not null,
   contract_status_name TStr100              not null,
   constraint PK_WA_CONTRACT_STATUSES_REF primary key (contract_status_id)
);

comment on table wa_contract_statuses_ref is
'Статусы контракта';

/*==============================================================*/
/* Table: wa_contract_types_ref                                 */
/*==============================================================*/
create table wa_contract_types_ref (
   contract_type_id     TIdCode              not null,
   contract_type_name   TStr100              not null,
   constraint PK_WA_CONTRACT_TYPES_REF primary key (contract_type_id)
);

comment on table wa_contract_types_ref is
'Типы контрактов';

/*==============================================================*/
/* Table: wa_countries_ref                                      */
/*==============================================================*/
create table wa_countries_ref (
   country_id           TIdCode              not null,
   country_iso          TStr10               not null,
   country_name         TStr100              not null,
   constraint PK_WA_COUNTRIES_REF primary key (country_id),
   constraint AK_KEY_2_COUNTRY_ISO unique (country_iso)
);

comment on table wa_countries_ref is
'Коды стран';

/*==============================================================*/
/* Table: wa_device_sessions                                    */
/*==============================================================*/
create table wa_device_sessions (
   session_id           TIdBigCode           not null,
   device_id            TIdCode              not null,
   actual_date          TDateTime            not null,
   ip_address           TStr50               null,
   note                 TStr200              null,
   constraint PK_WA_DEVICE_SESSIONS primary key (session_id)
);

comment on table wa_device_sessions is
'Сессии пользовательских устройств';

/*==============================================================*/
/* Table: wa_device_types_ref                                   */
/*==============================================================*/
create table wa_device_types_ref (
   device_type_id       TidByteCode          not null,
   device_type_name     TStr100              not null,
   constraint PK_WA_DEVICE_TYPES_REF primary key (device_type_id)
);

comment on table wa_device_types_ref is
'Типы устройств';

/*==============================================================*/
/* Table: wa_firebase_apps                                      */
/*==============================================================*/
create table wa_firebase_apps (
   firebase_app_id      TIdCode              not null,
   actual_date          TDateTime            not null,
   admin_sdk            TStr10000            not null,
   package_name         TStr200              not null,
   name                 TStr200              not null,
   db_url               TStr200              not null,
   is_actual            TBoolean             not null,
   constraint PK_WA_FIREBASE_APPS primary key (firebase_app_id)
);

comment on table wa_firebase_apps is
'FireBase';

/*==============================================================*/
/* Table: wa_modify_reasons_ref                                 */
/*==============================================================*/
create table wa_modify_reasons_ref (
   modify_reason_id     TIdCode              not null,
   modify_reason_name   TStr200              not null,
   constraint PK_WA_MODIFY_REASONS_REF primary key (modify_reason_id)
);

comment on table wa_modify_reasons_ref is
'Коды причин ручного редактирования';

/*==============================================================*/
/* Table: wa_package_details                                    */
/*==============================================================*/
create table wa_package_details (
   package_name         TStr100              not null,
   actual_date          TDateTime            not null,
   company_name         TStr100              not null,
   app_name             TStr100              not null,
   contact_info         TStr100              not null,
   constraint PK_WA_PACKAGE_DETAILS primary key (package_name)
);

comment on table wa_package_details is
'PackageDetails';

/*==============================================================*/
/* Table: wa_subscription_statuses_ref                          */
/*==============================================================*/
create table wa_subscription_statuses_ref (
   subscription_status_id TidByteCode          not null,
   subscription_status_name TStr100              not null,
   constraint PK_WA_SUBSCRIPTION_STATUSES_RE primary key (subscription_status_id)
);

comment on table wa_subscription_statuses_ref is
'Статусы подписок';

/*==============================================================*/
/* Table: wa_tariffs_plan_statuses_ref                          */
/*==============================================================*/
create table wa_tariffs_plan_statuses_ref (
   tariff_plan_status_id TIdCode              not null,
   tariff_plan_status_name TStr100              not null,
   constraint PK_WA_TARIFFS_PLAN_STATUSES_RE primary key (tariff_plan_status_id)
);

comment on table wa_tariffs_plan_statuses_ref is
'Статусы тарифных планов';

/*==============================================================*/
/* Table: wa_tariffs_plans                                      */
/*==============================================================*/
create table wa_tariffs_plans (
   tariff_plan_id       TIdCode              not null,
   actual_date          TDateTime            not null,
   contract_type_id     TIdCode              not null,
   tariff_plan_status_id TIdCode              not null,
   device_type_id       TIdCode              null,
   sku                  TStr100              null,
   duration_hours       TIdCode              not null,
   subscriptions_amount TIdCode              not null,
   constraint PK_WA_TARIFFS_PLANS primary key (tariff_plan_id)
);

comment on table wa_tariffs_plans is
'Тариффные планы';

/*==============================================================*/
/* Table: wa_tariffs_plans_hist                                 */
/*==============================================================*/
create table wa_tariffs_plans_hist (
   tariff_plan_id       TIdCode              null,
   actual_date          TDateTime            null,
   contract_type_id     TIdCode              null,
   sku                  TStr100              null,
   duration_hours       TIdCode              null,
   subscriptions_amount TIdCode              null,
   device_type_id       TIdCode              null,
   tariff_plan_status_id TIdCode              null
);

comment on table wa_tariffs_plans_hist is
'Тариффные планы - hist';

/*==============================================================*/
/* Table: wa_users                                              */
/*==============================================================*/
create table wa_users (
   user_id              TIdCode              not null,
   actual_date          TDateTime            not null,
   country_id           TIdCode              not null,
   login_token          TStr50               not null,
   user_phone_num       TStr50               null,
   user_name            TStr128              null,
   email                TStr200              null,
   ora_user_id          TIdCode              null,
   constraint PK_WA_USERS primary key (user_id),
   constraint AK_WA_USERS_LOGIN_TOKEN unique (login_token)
);

comment on table wa_users is
'Пользователи системы';

/*==============================================================*/
/* Table: wa_users_contracts                                    */
/*==============================================================*/
create table wa_users_contracts (
   contract_id          TIdCode              not null,
   contract_status_id   TidByteCode          not null,
   actual_date          TDateTime            not null,
   user_id              TIdCode              not null,
   contract_type_id     TIdCode              not null,
   begin_date           TDateTime            not null,
   end_date             TDateTime            null,
   cancel_date          TDateTime            null,
   subscriptions_amount TIdCode              not null,
   modify_reason_id     TIdCode              null,
   edit_note            TStr2000             null,
   constraint PK_WA_USERS_CONTRACTS primary key (contract_id)
);

comment on table wa_users_contracts is
'Договора пользователей';

/*==============================================================*/
/* Table: wa_users_contracts_hist                               */
/*==============================================================*/
create table wa_users_contracts_hist (
   contract_id          TIdCode              not null,
   actual_date          TDateTime            not null,
   contract_type_id     TIdCode              null,
   user_id              TIdCode              null,
   begin_date           TDateTime            null,
   end_date             TDateTime            null,
   cancel_date          TDateTime            null,
   contract_status_id   TidByteCode          null,
   subscriptions_amount TIdCode              null,
   modify_reason_id     TIdCode              null,
   edit_note            TStr2000             null
);

comment on table wa_users_contracts_hist is
'Договора пользователей hist';

/*==============================================================*/
/* Table: wa_users_devices                                      */
/*==============================================================*/
create table wa_users_devices (
   device_id            TIdCode              not null,
   actual_date          TDateTime            not null,
   device_type_id       TidByteCode          not null,
   user_id              TIdUser              not null,
   app_name             TStr100              null,
   app_version          TStr100              null,
   mac_addr             TStr100              null,
   constraint PK_WA_USERS_DEVICES primary key (device_id)
);

comment on table wa_users_devices is
'Устройства пользователя';

/*==============================================================*/
/* Table: wa_users_devices_android                              */
/*==============================================================*/
create table wa_users_devices_android (
   device_id            TIdCode              not null,
   gsf_id               TStr200              not null,
   actual_date          TDateTime            not null,
   fcm_token            TStr400              null,
   secure_id            TStr100              null,
   device_fingerprint   TStr200              null,
   version_sdk_int      TStr100              null,
   version_release      TStr100              null,
   board                TStr100              null,
   manufacter           TStr100              null,
   supported_Abis       TStr100              null,
   constraint PK_WA_USERS_DEVICES_ANDROID primary key (device_id),
   constraint AK_WA_USERS_DEVICES_ANDROID unique (gsf_id)
);

comment on table wa_users_devices_android is
'Атрибуты устройства Android';

/*==============================================================*/
/* Table: wa_users_devices_android_hist                         */
/*==============================================================*/
create table wa_users_devices_android_hist (
   device_id            TIdCode              not null,
   actual_date          TDateTime            not null,
   gsf_id               TStr200              null,
   fcm_token            TStr400              null,
   secure_id            TStr100              null,
   device_fingerprint   TStr200              null,
   version_sdk_int      TStr100              null,
   version_release      TStr100              null,
   board                TStr100              null,
   manufacter           TStr100              null,
   supported_Abis       TStr100              null
);

comment on table wa_users_devices_android_hist is
'Атрибуты устройства Android';

/*==============================================================*/
/* Table: wa_users_devices_hist                                 */
/*==============================================================*/
create table wa_users_devices_hist (
   device_id            TIdCode              not null,
   actual_date          TDateTime            not null,
   device_type_id       TidByteCode          null,
   user_id              TIdUser              null,
   app_name             TStr100              null,
   app_version          TStr100              null,
   mac_addr             TStr100              null
);

comment on table wa_users_devices_hist is
'Устройства пользователя';

/*==============================================================*/
/* Table: wa_users_devices_ios                                  */
/*==============================================================*/
create table wa_users_devices_ios (
   device_id            TIdCode              not null,
   identifier_for_vendor TStr200              not null,
   actual_date          TDateTime            not null,
   system_version       TStr100              null,
   model                TStr100              null,
   ustname_release      TStr100              null,
   ustname_version      TStr100              null,
   ustname_machine      TStr200              null,
   icm_token            TStr400              null,
   constraint PK_WA_USERS_DEVICES_IOS primary key (device_id),
   constraint AK_WA_USERS_DEVICES_IOS unique (identifier_for_vendor)
);

comment on table wa_users_devices_ios is
'Атрибуты устройства ios
';

/*==============================================================*/
/* Table: wa_users_devices_ios_hist                             */
/*==============================================================*/
create table wa_users_devices_ios_hist (
   device_id            TIdCode              not null,
   actual_date          TDateTime            not null,
   identifier_for_vendor TStr200              null,
   system_version       TStr100              null,
   model                TStr100              null,
   ustname_release      TStr100              null,
   ustname_version      TStr30               null,
   ustname_machine      TStr200              null,
   icm_token            TStr400              null
);

comment on table wa_users_devices_ios_hist is
'Атрибуты устройства ios
';

/*==============================================================*/
/* Table: wa_users_hist                                         */
/*==============================================================*/
create table wa_users_hist (
   user_id              TIdCode              not null,
   actual_date          TDateTime            not null,
   country_id           TIdCode              not null,
   login_token          TStr50               not null,
   user_phone_num       TStr50               null,
   user_name            TStr128              null,
   email                TStr200              null,
   ora_user_id          TIdCode              null
);

comment on table wa_users_hist is
'Пользователи системы hist';

/*==============================================================*/
/* Table: wa_users_subscriptions                                */
/*==============================================================*/
create table wa_users_subscriptions (
   subscription_id      TIdCode              not null,
   actual_date          TDateTime            not null,
   contract_id          TIdCode              not null,
   agent_id             TIdCode              not null,
   user_id              TIdCode              not null,
   subscription_status_id TidByteCode          not null,
   subscription_name    TStr100              null,
   phone_num            TStr20               not null,
   online_notify        TBoolean             not null,
   avatar_img           TImage               null,
   avatar_id            TIdBigCode           null,
   tmp_ora_id           TIdCode              null,
   constraint PK_WA_USERS_SUBSCRIPTIONS primary key (subscription_id)
);

comment on table wa_users_subscriptions is
'Подписки пользователей';

/*==============================================================*/
/* Index: wa_idx_actual_date                                    */
/*==============================================================*/
create  index wa_idx_actual_date on wa_users_subscriptions (
actual_date
);

/*==============================================================*/
/* Table: wa_users_subscriptions_hist                           */
/*==============================================================*/
create table wa_users_subscriptions_hist (
   subscription_id      TIdCode              not null,
   actual_date          TDateTime            not null,
   subscription_name    TStr100              null,
   phone_num            TStr20               not null,
   contract_id          TIdCode              not null,
   user_id              TIdCode              not null,
   agent_id             TIdCode              not null,
   subscription_status_id TidByteCode          not null,
   online_notify        TBoolean             not null,
   avatar_img           TImage               null,
   avatar_id            TIdBigCode           null
);

comment on table wa_users_subscriptions_hist is
'Подписки пользователей hist';

/*==============================================================*/
/* Index: wa_users_ss_hist                                      */
/*==============================================================*/
create  index wa_users_ss_hist on wa_users_subscriptions_hist (
subscription_id,
actual_date
);

/*==============================================================*/
/* Table: wa_uss_activities                                     */
/*==============================================================*/
create table wa_uss_activities (
   activity_id          TIdBigCode           not null,
   subscription_id      TIdCode              not null,
   actual_date          TDateTime            not null,
   is_online            TBoolean             not null,
   constraint PK_WA_USS_ACTIVITIES primary key (activity_id)
);

comment on table wa_uss_activities is
'Активность пользователей';

/*==============================================================*/
/* Index: wa_ussa_ss_id_ad                                      */
/*==============================================================*/
create  index wa_ussa_ss_id_ad on wa_uss_activities (
subscription_id,
actual_date
);

alter table wa_agents
   add constraint FK_wa_agents_status foreign key (agent_status_id)
      references wa_agent_statuses_ref (agent_status_id)
      on delete restrict on update restrict;

alter table wa_agents_hist
   add constraint FK_wa_ah_agent_id foreign key (agent_id)
      references wa_agents (agent_id)
      on delete restrict on update restrict;

alter table wa_app_settings_hist
   add constraint FK_wa_apps_hist_id foreign key (app_setting_id)
      references wa_app_settings (app_setting_id)
      on delete restrict on update restrict;

alter table wa_device_sessions
   add constraint FK_wa_ud_device_type_id foreign key (device_id)
      references wa_users_devices (device_id)
      on delete restrict on update restrict;

alter table wa_tariffs_plans
   add constraint FK_wa_tp_contract_type_id foreign key (contract_type_id)
      references wa_contract_types_ref (contract_type_id)
      on delete restrict on update restrict;

alter table wa_tariffs_plans
   add constraint FK_wa_tp_device_type_id foreign key (device_type_id)
      references wa_device_types_ref (device_type_id)
      on delete restrict on update restrict;

alter table wa_tariffs_plans
   add constraint FK_wa_tp_status_id foreign key (tariff_plan_status_id)
      references wa_tariffs_plan_statuses_ref (tariff_plan_status_id)
      on delete restrict on update restrict;

alter table wa_tariffs_plans_hist
   add constraint FK_wa_tp_hist_tp_id foreign key (tariff_plan_id)
      references wa_tariffs_plans (tariff_plan_id)
      on delete restrict on update restrict;

alter table wa_users
   add constraint FK_wa_users_country_id foreign key (country_id)
      references wa_countries_ref (country_id)
      on delete restrict on update restrict;

alter table wa_users_contracts
   add constraint FK_wa_uc_manual_r_id foreign key (modify_reason_id)
      references wa_modify_reasons_ref (modify_reason_id)
      on delete restrict on update restrict;

alter table wa_users_contracts
   add constraint FK_wa_uc_status_id foreign key (contract_status_id)
      references wa_contract_statuses_ref (contract_status_id)
      on delete restrict on update restrict;

alter table wa_users_contracts
   add constraint FK_wa_uc_type_id foreign key (contract_type_id)
      references wa_contract_types_ref (contract_type_id)
      on delete restrict on update restrict;

alter table wa_users_contracts
   add constraint FK_wa_uch_contract_id foreign key (user_id)
      references wa_users (user_id)
      on delete restrict on update restrict;

alter table wa_users_contracts_hist
   add constraint FK_wa_uch_contract_id foreign key (contract_id)
      references wa_users_contracts (contract_id)
      on delete restrict on update restrict;

alter table wa_users_devices
   add constraint FK_wa_ud_device_type_id foreign key (device_type_id)
      references wa_device_types_ref (device_type_id)
      on delete restrict on update restrict;

alter table wa_users_devices
   add constraint FK_wa_ud_user_id foreign key (user_id)
      references wa_users (user_id)
      on delete restrict on update restrict;

alter table wa_users_devices_android
   add constraint FK_wa_uda_device_id foreign key (device_id)
      references wa_users_devices (device_id)
      on delete restrict on update restrict;

alter table wa_users_devices_android_hist
   add constraint FK_wa_uda_hist_device_id foreign key (device_id)
      references wa_users_devices_android (device_id)
      on delete restrict on update restrict;

alter table wa_users_devices_hist
   add constraint FK_wa_udh_device_id foreign key (device_id)
      references wa_users_devices (device_id)
      on delete restrict on update restrict;

alter table wa_users_devices_ios
   add constraint FK_wa_uda_device_id foreign key (device_id)
      references wa_users_devices (device_id)
      on delete restrict on update restrict;

alter table wa_users_devices_ios_hist
   add constraint FK_wa_udih_device_id foreign key (device_id)
      references wa_users_devices_ios (device_id)
      on delete restrict on update restrict;

alter table wa_users_hist
   add constraint FK_wa_users_hist foreign key (user_id)
      references wa_users (user_id)
      on delete restrict on update restrict;

alter table wa_users_subscriptions
   add constraint FK_wa_subscriptipons_status_id foreign key (subscription_status_id)
      references wa_subscription_statuses_ref (subscription_status_id)
      on delete restrict on update restrict;

alter table wa_users_subscriptions
   add constraint FK_wa_uss_agent_id foreign key (agent_id)
      references wa_agents (agent_id)
      on delete restrict on update restrict;

alter table wa_users_subscriptions
   add constraint FK_wa_uss_contract_id foreign key (contract_id)
      references wa_users_contracts (contract_id)
      on delete restrict on update restrict;

alter table wa_users_subscriptions
   add constraint FK_wa_uss_user_id foreign key (user_id)
      references wa_users (user_id)
      on delete restrict on update restrict;

alter table wa_users_subscriptions_hist
   add constraint FK_wa_ussh_user_id foreign key (subscription_id)
      references wa_users_subscriptions (subscription_id)
      on delete restrict on update restrict;

alter table wa_uss_activities
   add constraint FK_wa_uss_ss_id foreign key (subscription_id)
      references wa_users_subscriptions (subscription_id)
      on delete restrict on update restrict;

