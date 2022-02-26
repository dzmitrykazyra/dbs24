-- alter

drop table wa_tariffs;
drop table wa_users_tokens;
alter table wa_agent_statusesRef rename to wa_agent_statuses_ref;
alter table wa_contract_statusesref rename to wa_contract_statuses_ref;
alter table wa_contract_typesref rename to wa_contract_types_ref;
alter table wa_countriesref rename to wa_countries_ref;
alter table wa_currenciesref rename to wa_currencies_ref;
alter table wa_device_typesref rename to wa_device_types_ref;
alter table wa_payment_servicesref rename to wa_payment_services_ref;
alter table wa_payment_statusesref rename to wa_payment_statuses_ref;
alter table wa_subscription_statusesref rename to wa_subscription_statuses_ref;
--==========================================================================
-- unused
drop table wa_contract_payments;
drop table wa_payment_services_ref;
drop table wa_payment_statuses_ref;
drop table wa_currencies_ref;

alter table wa_agents_hist drop constraint  pk_wa_agents_hist;
alter table wa_users_contracts_hist drop constraint  pk_wa_users_contracts_hist;
alter table wa_users_devices_android_hist drop constraint  pk_wa_users_devices_android_hi;
alter table wa_users_devices_hist drop constraint  pk_wa_users_devices_hist;
alter table wa_users_devices_ios_hist drop constraint pk_wa_users_devices_ios_hist;

--=========================================================================
ALTER TABLE wa_contract_types_ref RENAME contrract_type_name TO contract_type_name;

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

-----------------------------------------------------------------
create sequence seq_wa_TariffPlans
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

/* Table: wa_tariffs_plans                                      */
/*==============================================================*/
create table wa_tariffs_plans (
   tariff_plan_id       TIdCode              not null,
   actual_date          TDateTime            not null,
   contract_type_id     TIdCode              not null,
   tariff_plan_status_id TIdCode             not null,
   device_type_id       TIdCode              null,
   sku                  TStr100              null,
   duration_hours        TIdCode             not null,
   subscriptions_amount  TIdCode              not null,
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
   duration_days        TIdCode              null,
   subs_amount          TIdCode              null,
   device_type_id       TIdCode              null,
   tariff_plan_status_id TIdCode              null
);

comment on table wa_tariffs_plans_hist is
'Тариффные планы - hist';


/*==============================================================*/
/* Table: wa_manual_updates_reasons_ref                         */
/*==============================================================*/
create table wa_manual_updates_reasons_ref (
   manual_update_reason_id TIdCode              not null,
   manual_update_reason_name TStr200              not null,
   constraint PK_WA_MANUAL_UPDATES_REASONS_R primary key (manual_update_reason_id)
);

comment on table wa_manual_updates_reasons_ref is
'Коды причин ручного редактирования';


alter table wa_users_contracts_hist add column  manual_update_reason_id TIdCode null,
   add column note TStr2000 null;
);