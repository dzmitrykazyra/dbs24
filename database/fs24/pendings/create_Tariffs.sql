/*==============================================================*/
/* Domain: TPercRate                                            */
/*==============================================================*/
create domain TPercRate as NUMERIC(12,6);

/*==============================================================*/
/* Domain: TPercRateExt                                         */
/*==============================================================*/
create domain TPercRateExt as NUMERIC(16,8);

/*==============================================================*/
/* Domain: TStr128                                              */
/*==============================================================*/
create domain TStr128 as VARCHAR(128);


/*==============================================================*/
/* Table: EntityContract2TariffPlans                            */
/*==============================================================*/
create table EntityContract2TariffPlans (
   tariff_plan_id       TIdCode              not null,
   contract_id          TIdCode              not null,
   constraint PK_ENTITYCONTRACT2TARIFFPLANS primary key (tariff_plan_id, contract_id)
);

comment on table EntityContract2TariffPlans is
'Тарифные планы на договорах';


/*==============================================================*/
/* Table: TariffAccretionSchemeRef                              */
/*==============================================================*/
create table TariffAccretionSchemeRef (
   tariff_scheme_id     TIdCode              not null,
   tariff_scheme_name   TStr100              not null,
   constraint PK_TARIFFACCRETIONSCHEMEREF primary key (tariff_scheme_id)
);

comment on table TariffAccretionSchemeRef is
'Справочник схем начисления';

/*==============================================================*/
/* Table: TariffGroupsRef                                       */
/*==============================================================*/
create table TariffGroupsRef (
   tariff_group_id      TIdCode              not null,
   tariff_group_name    TStr100              not null,
   constraint PK_TARIFFGROUPSREF primary key (tariff_group_id)
);

comment on table TariffGroupsRef is
'Справочник групп тарифицируемых услуг';

/*==============================================================*/
/* Table: TariffKindsRef                                        */
/*==============================================================*/
create table TariffKindsRef (
   tariff_kind_id       TIdCode              not null,
   tariff_serv_id       TIdCode              not null,
   tariff_kind_name     TStr200              not null,
   constraint PK_TARIFFKINDSREF primary key (tariff_kind_id)
);

comment on table TariffKindsRef is
'Справочник видов тарифицируемых услуг';

/*==============================================================*/
/* Table: TariffPlan2ServId                                     */
/*==============================================================*/
create table TariffPlan2ServId (
   tariff_plan_id       TIdCode              not null,
   tariff_serv_id       TIdCode              not null,
   tariff_kind_id       TIdCode              null,
   actual_date          TDate                not null,
   close_date           TDate                null,
   constraint PK_TARIFFPLAN2SERVID primary key (tariff_plan_id, tariff_serv_id)
);

comment on table TariffPlan2ServId is
'Услуги по тарифному плану';

/*==============================================================*/
/* Table: TariffPlan2StdRates                                   */
/*==============================================================*/
create table TariffPlan2StdRates (
   tariff_std_rate_id   TIdCode              not null,
   tariff_plan_id       TIdCode              not null,
   tariff_serv_id       TIdCode              not null,
   tariff_kind_id       TIdCode              not null,
   actual_date          TDate                not null,
   std_rate_name        TStr100              null,
   constraint PK_TARIFFPLAN2STDRATES primary key (tariff_std_rate_id)
);

comment on table TariffPlan2StdRates is
'Ставки стандартных тарифных величин';

/*==============================================================*/
/* Table: TariffPlans                                           */
/*==============================================================*/
create table TariffPlans (
   tariff_plan_id       TIdCode              not null,
   tariff_plan_name     TStr100              not null,
   entity_kind_id       TIdCode              not null,
   actual_date          TDate                not null,
   close_date           TDate                null,
   constraint PK_TARIFFPLANS primary key (tariff_plan_id)
);

comment on table TariffPlans is
'Картотека тарифных планов';

/*==============================================================*/
/* Table: TariffRates                                           */
/*==============================================================*/
create table TariffRates (
   rate_id              TIdCode              not null,
   tariff_plan_id       TIdCode              not null,
   tariff_serv_id       TIdCode              not null,
   tariff_kind_id       TIdCode              not null,
   tariff_scheme_id     TIdCode              not null,
   rate_name            TStr128              not null,
   actual_date          TDate                not null,
   close_date           TDate                null,
   constraint PK_TARIFFRATES primary key (rate_id)
);

comment on table TariffRates is
'Тарифные ставки';

/*==============================================================*/
/* Table: TariffRates_1                                         */
/*==============================================================*/
create table TariffRates_1 (
   rate_id              TIdCode              not null,
   actual_date          TDate                not null,
   close_date           TDate                null,
   rate_value           TPercRate            null,
   currency_code        TIdCode              not null,
   constraint PK_TARIFFRATES_1 primary key (rate_id)
);

comment on table TariffRates_1 is
'Значения ставок (вариант 1)';

/*==============================================================*/
/* Table: TariffRates_2                                         */
/*==============================================================*/
create table TariffRates_2 (
   rate_id              TIdCode              not null,
   max_sum              TMoney               not null,
   min_sum              TMoney               not null,
   actual_date          TDate                not null,
   close_date           TDate                null,
   rate_value           TPercRate            not null,
   currency_code        TIdCode              not null,
   constraint PK_TARIFFRATES_2 primary key (rate_id)
);

comment on table TariffRates_2 is
'Значения ставок (вариант 2)';

/*==============================================================*/
/* Table: TariffRates_3                                         */
/*==============================================================*/
create table TariffRates_3 (
   rate_id              TIdCode              not null,
   rate_value           TPercRate            not null,
   fix_sum              TMoney               not null,
   actual_date          TDate                not null,
   close_date           TDate                null,
   currency_code        TIdCode              not null,
   constraint PK_TARIFFRATES_3 primary key (rate_id)
);

comment on table TariffRates_3 is
'Значения ставок (вариант 3)';

/*==============================================================*/
/* Table: TariffServsRef                                        */
/*==============================================================*/
create table TariffServsRef (
   tariff_serv_id       TIdCode              not null,
   tariff_serv_name     TStr100              not null,
   tariff_group_id      TIdCode              not null,
   client_pay           TBoolean             not null,
   constraint PK_TARIFFSERVSREF primary key (tariff_serv_id)
);

comment on table TariffServsRef is
'Справочник тарифицируемых услуг';

/*==============================================================*/
/* Table: TariffStdRates                                        */
/*==============================================================*/
create table TariffStdRates (
   tariff_std_rate_id   TIdCode              not null,
   actual_date          TDate                not null,
   tariff_std_rate_value TPercRate            null,
   constraint PK_TARIFFSTDRATES primary key (tariff_std_rate_id)
);

comment on table TariffStdRates is
'Значение нормативных величин';

/*==============================================================*/
/* Table: TariffStdRatesGroupsRef                               */
/*==============================================================*/
create table TariffStdRatesGroupsRef (
   tariff_std_group_id  TIdCode              not null,
   tariff_std_group_name TStr100              not null,
   constraint PK_TARIFFSTDRATESGROUPSREF primary key (tariff_std_group_id)
);

comment on table TariffStdRatesGroupsRef is
'Справочник групп стандартных тарифных величин';

/*==============================================================*/
/* Table: TariffStdRatesRef                                     */
/*==============================================================*/
create table TariffStdRatesRef (
   tariff_std_rate_id   TIdCode              not null,
   tariff_std_rate_name TStr80               not null,
   tariff_std_group_id  TIdCode              not null,
   constraint PK_TARIFFSTDRATESREF primary key (tariff_std_rate_id)
);

comment on table TariffStdRatesRef is
'Справочник стандартных тарифных величин';


alter table EntityContract2TariffPlans
   add constraint FK_EC_TariffPlans_contract_id foreign key (tariff_plan_id)
      references TariffPlans (tariff_plan_id)
      on delete restrict on update restrict;

alter table EntityContract2TariffPlans
   add constraint FK_ENTITYCO_REFERENCE_ENTITYCO foreign key (contract_id)
      references EntityContracts (contract_id)
      on delete restrict on update restrict;

alter table TariffKindsRef
   add constraint FK_TariffKinds_serv_id foreign key (tariff_serv_id)
      references TariffServsRef (tariff_serv_id)
      on delete restrict on update restrict;

alter table TariffPlan2ServId
   add constraint FK_TariffPlan2ServId_plan_id foreign key (tariff_plan_id)
      references TariffPlans (tariff_plan_id)
      on delete restrict on update restrict;

alter table TariffPlan2ServId
   add constraint FK_TariffPlan2ServId_serv_id foreign key (tariff_serv_id)
      references TariffServsRef (tariff_serv_id)
      on delete restrict on update restrict;

alter table TariffPlan2ServId
   add constraint FK_TariffRates_kind_id foreign key (tariff_kind_id)
      references TariffKindsRef (tariff_kind_id)
      on delete restrict on update restrict;

alter table TariffPlan2StdRates
   add constraint FK_TariffPlan2ServId_serv_id foreign key (tariff_plan_id, tariff_serv_id)
      references TariffPlan2ServId (tariff_plan_id, tariff_serv_id)
      on delete restrict on update restrict;

alter table TariffPlan2StdRates
   add constraint FK_TARIFFPL_TARIFFPLA_TARIFFKI foreign key (tariff_kind_id)
      references TariffKindsRef (tariff_kind_id)
      on delete restrict on update restrict;

alter table TariffPlan2StdRates
   add constraint FK_TariffPlan2StdRate_rate_id foreign key (tariff_std_rate_id)
      references TariffStdRatesRef (tariff_std_rate_id)
      on delete restrict on update restrict;

alter table TariffRates
   add constraint FK_TariffASR_scheme_id foreign key (tariff_scheme_id)
      references TariffAccretionSchemeRef (tariff_scheme_id)
      on delete restrict on update restrict;

alter table TariffRates
   add constraint FK_TariffRates_kind_id foreign key (tariff_kind_id)
      references TariffKindsRef (tariff_kind_id)
      on delete restrict on update restrict;

alter table TariffRates
   add constraint FK_TariffRates_serv_id foreign key (tariff_plan_id, tariff_serv_id)
      references TariffPlan2ServId (tariff_plan_id, tariff_serv_id)
      on delete restrict on update restrict;

alter table TariffRates_1
   add constraint FK_TariffRates_1_rate_id foreign key (rate_id)
      references TariffRates (rate_id)
      on delete restrict on update restrict;

alter table TariffRates_2
   add constraint FK_TariffRates_2_rate_id foreign key (rate_id)
      references TariffRates (rate_id)
      on delete restrict on update restrict;

alter table TariffRates_3
   add constraint FK_TariffRate_3_rate_id foreign key (rate_id)
      references TariffRates (rate_id)
      on delete restrict on update restrict;

alter table TariffServsRef
   add constraint FK_TariffServs_group_id foreign key (tariff_group_id)
      references TariffGroupsRef (tariff_group_id)
      on delete restrict on update restrict;

alter table TariffStdRates
   add constraint FK_TariffStdRates_rate_id foreign key (tariff_std_rate_id)
      references TariffStdRatesRef (tariff_std_rate_id)
      on delete restrict on update restrict;

alter table TariffStdRatesRef
   add constraint FK_TariffStrRatesRef_groupId foreign key (tariff_std_group_id)
      references TariffStdRatesGroupsRef (tariff_std_group_id)
      on delete restrict on update restrict;

