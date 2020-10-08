/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     20.09.2020 8:43:13                           */
/*==============================================================*/


drop table DbObjects;

drop table DocAttrs;

drop table DocAttrsRef;

drop table DocStatusesRef;

drop table DocTemplateAttrsRef;

drop table DocTemplateGroupsRef;

drop table DocTemplateSetup;

drop table DocTemplatesRef;

drop table DocTypesRef;

drop table Documents;

drop table FinAccTypes;

drop table LiasActionTypesRef;

drop index iLiasActions_Status_LiasID;

drop index iLiasActions_LiasID_LDate;

drop index iLiasActions_LiasID_ODate;

drop index iLiasActions_Type_Status_LDate;

drop table LiasActions;

drop table LiasBaseAssetTypesRef;

drop table LiasDebtRests;

drop table LiasDebtStatesRef;

drop index DebtDates;

drop table LiasDebts;

drop table LiasFinOperCodesRef;

drop table LiasFinOperStatusesRef;

drop table LiasGroupsRef;

drop table LiasKindsRef;

drop table LiasRests;

drop table LiasTypesRef;

drop index iLias_DebtID_InactiveDate;

drop index iLias_DebtID_FinalDate;

drop table Liases;

drop table PaymentSystemDocTemplatesRef;

drop table PaymentSystemsRef;

drop table ReferencesVersions;

drop table TariffAccretionSchemeRef;

drop table TariffAccretionsHist;

drop table TariffCalcRecords;

drop table TariffCalcSum;

drop table TariffGroupsRef;

drop table TariffKindsRef;

drop table TariffPlan2ServId;

drop table TariffPlan2StdRates;

drop table TariffPlanAccretions;

drop table TariffPlans;

drop table TariffRates;

drop table TariffRates_1;

drop table TariffRates_2;

drop table TariffRates_3;

drop table TariffServsRef;

drop table TariffStdRates;

drop table TariffStdRatesGroupsRef;

drop table TariffStdRatesRef;

drop table chess_CertTypeRef;

drop table chess_Certificates;

drop table chess_CheckerboardActions;

drop table chess_CheckerboardsRef;

drop table chess_EnginesRef;

drop table chess_Federations;

drop table chess_FreeChallenges;

drop table chess_GameActions;

drop table chess_GameActionsEstimations;

drop table chess_GamesResultTypeRef;

drop table chess_GamesTypeRef;

drop table chess_InvitesTypesRef;

drop table chess_MovesNotationsRef;

drop table chess_PiecesRef;

drop table chess_Player2Federation;

drop table chess_Players;

drop table chess_PlayersRatings;

drop table chess_PrivateInvites;

drop table chess_RatingTypeRef;

drop table chess_SandBox;

drop table chess_TimerAddAlgRef;

drop table chess_TimerTypesRef;

drop table chess_Tournament;

drop table chess_TournamentGames;

drop table chess_TournamentInvites;

drop table chess_TournamentRounds;

drop table core_Action2Role;

drop table core_ActionCodesRef;

drop table core_Actions;

drop table core_AppFieldsCaptions;

drop table core_Application2Role;

drop table core_ApplicationsRef;

drop table core_ContractSubjectsRef;

drop index idx_counterparty_code;

drop table core_Counterparties;

drop table core_CountriesRef;

drop table core_CurrenciesGroupsRef;

drop table core_CurrenciesRatesTypesRef;

drop table core_CurrenciesRef;

drop table core_Currency2Group;

drop table core_CurrencyRates;

drop table core_EntAttrTemplatesRef;

drop table core_EntAttrsRef;

drop table core_Entities;

drop table core_EntityAttrs;

drop table core_EntityContracts;

drop table core_EntityKindsRef;

drop table core_EntityMarks;

drop table core_EntityStatusesRef;

drop table core_EntityTests;

drop table core_EntityTypesRef;

drop table core_Function2Role;

drop table core_FunctionsGroupsRef;

drop table core_FunctionsRef;

drop table core_GenderRef;

drop table core_LanguagesRef;

drop table core_MarksRef;

drop table core_MarksValuesRef;

drop table core_PmtScheduleAlgsRef;

drop table core_PmtScheduleLines;

drop table core_PmtScheduleTermsRef;

drop index schedule_contracts;

drop table core_PmtSchedules;

drop table core_Roles;

drop index idx_user;

drop table core_SessionTokens;

drop table core_SessionTokensHist;

drop table core_SessionTokensTypesRef;

drop table core_User2Role;

drop index idx_user3;

drop table core_UserActivationCodes;

drop table core_Users;

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

drop table pg_dual;

drop table rlc_LoanContracts;

drop table rlc_LoanSourcesRef;

drop domain TBanknotesAmt;

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

drop domain TIntCounter;

drop domain TItemType;

drop domain TMoney;

drop domain TPercRate;

drop domain TPercRateExt;

drop domain TReal;

drop domain TStr10;

drop domain TStr100;

drop domain TStr128;

drop domain TStr2;

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

drop domain TidByteCode;

drop sequence seq_TariffAccretionsHist;

drop sequence seq_TariffCalcRecords;

drop sequence seq_TariffPlanAccretions;

drop sequence seq_TariffRates;

drop sequence seq_action_id;

drop sequence seq_debt_id;

drop sequence seq_doc_id;

drop sequence seq_lias_action_id;

drop sequence seq_lias_id;

drop sequence seq_msn_Dialogs;

drop sequence seq_msn_UserPosts;

drop sequence seq_msn_UserPosts_Hist;

drop sequence seq_msn_UsersHist;

drop sequence seq_wc_CheckerboardActions;

drop sequence seq_wc_GameActions;

create sequence seq_TariffAccretionsHist
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_TariffCalcRecords
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_TariffPlanAccretions
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_TariffRates
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_action_id
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_debt_id
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_doc_id
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_lias_action_id
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_lias_id
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

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

create sequence seq_wc_CheckerboardActions
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

create sequence seq_wc_GameActions
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
/* Domain: TStr2                                                */
/*==============================================================*/
create domain TStr2 as CHAR(2);

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
/* Table: DbObjects                                             */
/*==============================================================*/
create table DbObjects (
   obj_name             TStr100              not null,
   obj_type             TStr50               not null,
   obj_version          TReal                not null,
   application          TStr80               not null,
   assign_date          TDateTime            not null,
   constraint PK_DBOBJECTS primary key (obj_name)
);

comment on table DbObjects is
'Инофрмация о версиях объектов БД';

/*==============================================================*/
/* Table: DocAttrs                                              */
/*==============================================================*/
create table DocAttrs (
   doc_id               TIdBigCode           not null,
   doc_attr_id          TIdCode              not null,
   doc_attr_value       TStr2000             not null,
   constraint PK_DOCATTRS primary key (doc_id, doc_attr_id)
);

comment on table DocAttrs is
'Атрибуты платежного документа';

/*==============================================================*/
/* Table: DocAttrsRef                                           */
/*==============================================================*/
create table DocAttrsRef (
   doc_attr_id          TIdCode              not null,
   doc_attr_code        TStr30               not null,
   doc_attr_name        TStr128              not null,
   constraint PK_DOCATTRSREF primary key (doc_attr_id),
   constraint AK_DOCATTRSREF unique (doc_attr_code)
);

comment on table DocAttrsRef is
'Справочник атрибутов документов';

/*==============================================================*/
/* Table: DocStatusesRef                                        */
/*==============================================================*/
create table DocStatusesRef (
   doc_status_id        TIdCode              not null,
   doc_status_name      TStr100              not null,
   constraint PK_DOCSTATUSESREF primary key (doc_status_id)
);

comment on table DocStatusesRef is
'Справочник статусов документов';

/*==============================================================*/
/* Table: DocTemplateAttrsRef                                   */
/*==============================================================*/
create table DocTemplateAttrsRef (
   doc_template_id      TIdCode              not null,
   doc_attr_id          TIdCode              not null,
   constraint PK_DOCTEMPLATEATTRSREF primary key (doc_template_id, doc_attr_id)
);

comment on table DocTemplateAttrsRef is
'Справочник шаблонов плажетных документов';

/*==============================================================*/
/* Table: DocTemplateGroupsRef                                  */
/*==============================================================*/
create table DocTemplateGroupsRef (
   doc_template_group_id TIdCode              not null,
   doc_template_group_name TStr100              not null,
   constraint PK_DOCTEMPLATEGROUPSREF primary key (doc_template_group_id)
);

comment on table DocTemplateGroupsRef is
'Справочник групп шаблонов';

/*==============================================================*/
/* Table: DocTemplateSetup                                      */
/*==============================================================*/
create table DocTemplateSetup (
   action_code          TIdCode              null,
   lias_fin_oper_code   TIdCode              null,
   doc_template_id      TIdCode              not null
);

comment on table DocTemplateSetup is
'Настройка шаблонов документов для операций';

/*==============================================================*/
/* Table: DocTemplatesRef                                       */
/*==============================================================*/
create table DocTemplatesRef (
   doc_template_id      TIdCode              not null,
   doc_template_group_id TIdCode              not null,
   doc_template_code    TStr30               not null,
   doc_template_name    TStr100              not null,
   pmt_sys_id           TIdCode              not null,
   doc_type_id          TIdCode              not null,
   constraint PK_DOCTEMPLATESREF primary key (doc_template_id),
   constraint AK_KEY_2_DOCTEMPL unique (doc_template_code)
);

comment on table DocTemplatesRef is
'Справочник шаблонов плажетных документов';

/*==============================================================*/
/* Table: DocTypesRef                                           */
/*==============================================================*/
create table DocTypesRef (
   doc_type_id          TIdCode              not null,
   doc_type_name        TStr100              not null,
   constraint PK_DOCTYPESREF primary key (doc_type_id)
);

comment on table DocTypesRef is
'Справочник типов документов';

/*==============================================================*/
/* Table: Documents                                             */
/*==============================================================*/
create table Documents (
   doc_id               TIdBigCode           not null,
   parent_doc_id        TIdBigCode           null,
   doc_template_id      TIdCode              not null,
   doc_status_id        TIdCode              not null,
   entity_id            TIdBigCode           not null,
   doc_date             TDate                not null,
   doc_server_date      TDateTime            not null,
   doc_close_date       TDate                null,
   user_id              TIdBigCode           not null,
   constraint PK_DOCUMENTS primary key (doc_id)
);

comment on table Documents is
'Платежные документы';

/*==============================================================*/
/* Table: FinAccTypes                                           */
/*==============================================================*/
create table FinAccTypes (
   FinAccTypeId         TIdCode              not null,
   FinAccTypeName       TStr200              not null,
   constraint PK_FINACCTYPES primary key (FinAccTypeId)
);

comment on table FinAccTypes is
'Справочник типов финансовых счетов';

/*==============================================================*/
/* Table: LiasActionTypesRef                                    */
/*==============================================================*/
create table LiasActionTypesRef (
   action_type_id       TIdCode              not null,
   change_rest_tag      TBoolean             not null,
   action_type_name     TStr100              not null,
   constraint PK_LIASACTIONTYPES primary key (action_type_id)
);

/*==============================================================*/
/* Table: LiasActions                                           */
/*==============================================================*/
create table LiasActions (
   lias_action_id       TIdBigCode           not null,
   action_type_id       TIdCode              not null,
   doc_id               TIdBigCode           not null,
   lias_id              TIdCode              not null,
   fin_oper_code        TIdCode              not null,
   lias_date            TDate                not null,
   oper_date            TDate                null,
   server_date          TDateTime            not null,
   fin_oper_status_id   TIdCode              null,
   lias_sum             TMoney               null,
   constraint PK_LIASACTIONS primary key (lias_action_id)
);

/*==============================================================*/
/* Index: iLiasActions_Type_Status_LDate                        */
/*==============================================================*/
create  index iLiasActions_Type_Status_LDate on LiasActions (
action_type_id,
fin_oper_status_id,
lias_date
);

/*==============================================================*/
/* Index: iLiasActions_LiasID_ODate                             */
/*==============================================================*/
create  index iLiasActions_LiasID_ODate on LiasActions (
lias_id,
oper_date
);

/*==============================================================*/
/* Index: iLiasActions_LiasID_LDate                             */
/*==============================================================*/
create  index iLiasActions_LiasID_LDate on LiasActions (
lias_id,
lias_date
);

/*==============================================================*/
/* Index: iLiasActions_Status_LiasID                            */
/*==============================================================*/
create  index iLiasActions_Status_LiasID on LiasActions (
fin_oper_status_id,
lias_id
);

/*==============================================================*/
/* Table: LiasBaseAssetTypesRef                                 */
/*==============================================================*/
create table LiasBaseAssetTypesRef (
   base_asset_type_id   TIdCode              not null,
   base_asset_type_name TStr100              not null,
   constraint PK_LIASBASEASSETTYPES primary key (base_asset_type_id)
);

/*==============================================================*/
/* Table: LiasDebtRests                                         */
/*==============================================================*/
create table LiasDebtRests (
   debt_id              TIdCode              not null,
   rest_type            TIdCode              not null,
   rest_date            TDate                not null,
   rest                 TMoney               not null,
   constraint PK_LIASDEBTRESTS primary key (rest_type, debt_id, rest_date),
   constraint CKT_DebtRests_Rest_Negative check (not(Rest<0))
);

/*==============================================================*/
/* Table: LiasDebtStatesRef                                     */
/*==============================================================*/
create table LiasDebtStatesRef (
   debt_state_id        TIdCode              not null,
   debt_state_name      TStr100              not null,
   constraint PK_LIASDEBTSTATES primary key (debt_state_id)
);

/*==============================================================*/
/* Table: LiasDebts                                             */
/*==============================================================*/
create table LiasDebts (
   debt_id              TIdCode              not null,
   counterparty_id      TIdBigCode           not null,
   currency_id          TIdCode              not null,
   contract_id          TIdCode              not null,
   debt_state_id        TIdCode              not null,
   lias_kind_id         TIdCode              not null,
   lias_type_id         TIdCode              not null,
   base_asset_type_id   TIdCode              not null,
   debt_start_date      TDate                null,
   debt_final_date      TDate                null,
   constraint PK_LIASDEBTS primary key (debt_id)
);

/*==============================================================*/
/* Index: DebtDates                                             */
/*==============================================================*/
create  index DebtDates on LiasDebts (
debt_start_date,
debt_final_date
);

/*==============================================================*/
/* Table: LiasFinOperCodesRef                                   */
/*==============================================================*/
create table LiasFinOperCodesRef (
   fin_oper_code        TIdCode              not null,
   fin_oper_name        TStr80               not null,
   constraint PK_LIASFINOPERCODES primary key (fin_oper_code)
);

/*==============================================================*/
/* Table: LiasFinOperStatusesRef                                */
/*==============================================================*/
create table LiasFinOperStatusesRef (
   fin_oper_status_id   TIdCode              not null,
   fin_oper_status_name TStr80               not null,
   constraint PK_LIASFINOPERSTATUSESREF primary key (fin_oper_status_id)
);

comment on table LiasFinOperStatusesRef is
'Статусы финансовых операций';

/*==============================================================*/
/* Table: LiasGroupsRef                                         */
/*==============================================================*/
create table LiasGroupsRef (
   lias_group_id        TIdCode              not null,
   lias_group_name      TStr100              not null,
   constraint PK_LIASGROUPS primary key (lias_group_id)
);

/*==============================================================*/
/* Table: LiasKindsRef                                          */
/*==============================================================*/
create table LiasKindsRef (
   lias_kind_id         TIdCode              not null,
   Is_claim             TBoolean             not null,
   lias_group_id        TIdCode              not null,
   lias_kind_name       TStr100              not null,
   constraint PK_LIASKINDS primary key (lias_kind_id)
);

/*==============================================================*/
/* Table: LiasRests                                             */
/*==============================================================*/
create table LiasRests (
   lias_id              TIdCode              not null,
   rest_type            TIdCode              not null,
   rest_date            TDate                not null,
   rest                 TMoney               not null,
   constraint PK_LIASRESTS primary key (rest_type, lias_id, rest_date),
   constraint CKT_LiasRests_Rest_Negative check (not(Rest<0))
);

/*==============================================================*/
/* Table: LiasTypesRef                                          */
/*==============================================================*/
create table LiasTypesRef (
   lias_type_id         TIdCode              not null,
   lias_type_name       TStr100              not null,
   constraint PK_LIASTYPES primary key (lias_type_id)
);

/*==============================================================*/
/* Table: Liases                                                */
/*==============================================================*/
create table Liases (
   lias_id              TIdCode              not null,
   debt_id              TIdCode              not null,
   start_date           TDate                not null,
   allow_date           TDate                null,
   final_date           TDate                null,
   legal_date           TDate                null,
   server_date          TDateTime            not null,
   inactive_date        TDate                null,
   is_canceled          TBoolean             null,
   constraint PK_LIAS primary key (lias_id)
);

/*==============================================================*/
/* Index: iLias_DebtID_FinalDate                                */
/*==============================================================*/
create  index iLias_DebtID_FinalDate on Liases (
debt_id,
final_date
);

/*==============================================================*/
/* Index: iLias_DebtID_InactiveDate                             */
/*==============================================================*/
create  index iLias_DebtID_InactiveDate on Liases (
debt_id,
inactive_date
);

/*==============================================================*/
/* Table: PaymentSystemDocTemplatesRef                          */
/*==============================================================*/
create table PaymentSystemDocTemplatesRef (
   pmt_sys_id           TIdCode              not null,
   fin_oper_code        TIdCode              not null,
   doc_template_id      TIdCode              not null,
   actual_date          TDate                not null,
   close_date           TDate                null,
   constraint PK_PAYMENTSYSTEMDOCTEMPLATESRE primary key (pmt_sys_id, fin_oper_code, doc_template_id)
);

comment on table PaymentSystemDocTemplatesRef is
'Связь финоперации с шаблоном документа';

/*==============================================================*/
/* Table: PaymentSystemsRef                                     */
/*==============================================================*/
create table PaymentSystemsRef (
   pmt_sys_id           TIdCode              not null,
   pmt_sys_code         TStr20               not null,
   pmt_sys_name         TStr100              not null,
   pmt_open_date        TDate                not null,
   pmt_close_date       TDate                null,
   constraint PK_PAYMENTSYSTEMSREF primary key (pmt_sys_id)
);

/*==============================================================*/
/* Table: ReferencesVersions                                    */
/*==============================================================*/
create table ReferencesVersions (
   ref_name             TStr100              not null,
   ref_hashcode         TIdBigCode           not null,
   assign_date          TDateTime            not null,
   constraint PK_REFERENCESVERSIONS primary key (ref_name)
);

comment on table ReferencesVersions is
'Инофрмация о версии справочников';

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
/* Table: TariffAccretionsHist                                  */
/*==============================================================*/
create table TariffAccretionsHist (
   id                   TIdCode              not null,
   accretion_date       TDate                not null,
   tariff_serv_id       TIdCode              not null,
   tariff_kind_id       TIdCode              not null,
   contract_id          TIdBigCode           not null,
   lias_action_id       TIdBigCode           not null,
   constraint PK_TARIFFACCRETIONSHIST primary key (id),
   constraint AK_TARIFFACCRETIONSHIST unique (accretion_date, tariff_serv_id, tariff_kind_id, contract_id)
);

comment on table TariffAccretionsHist is
'История начислений тарифицирумых сумм';

/*==============================================================*/
/* Table: TariffCalcRecords                                     */
/*==============================================================*/
create table TariffCalcRecords (
   tariff_calc_id       TIdCode              not null,
   rate_id              TIdCode              not null,
   entity_id            TIdBigCode           not null,
   constraint PK_TARIFFCALCRECORDS primary key (tariff_calc_id)
);

comment on table TariffCalcRecords is
'Ключ тарифа';

/*==============================================================*/
/* Table: TariffCalcSum                                         */
/*==============================================================*/
create table TariffCalcSum (
   tariff_calc_id       TIdCode              not null,
   tariff_calc_date     TDate                not null,
   tariff_summ          TMoney               not null,
   constraint PK_TARIFFCALCSUM primary key (tariff_calc_id, tariff_calc_date)
);

comment on table TariffCalcSum is
'Суммы рассчитанные тарифами';

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
   tariff_plan_id       TIdBigCode           not null,
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
   std_rate_name        TStr100              not null,
   std_rate_formula     TStr200              not null,
   constraint PK_TARIFFPLAN2STDRATES primary key (tariff_std_rate_id)
);

comment on table TariffPlan2StdRates is
'Ставки стандартных тарифных величин';

/*==============================================================*/
/* Table: TariffPlanAccretions                                  */
/*==============================================================*/
create table TariffPlanAccretions (
   id                   TIdCode              not null,
   tariff_plan_id       TIdCode              null,
   accretion_plan_date  TDate                not null,
   notes                TStr200              null,
   constraint PK_TARIFFPLANACCRETIONS primary key (id)
);

comment on table TariffPlanAccretions is
'Плановые даты начисления';

/*==============================================================*/
/* Table: TariffPlans                                           */
/*==============================================================*/
create table TariffPlans (
   tariff_plan_id       TIdBigCode           not null,
   tariff_plan_kind_id  TIdCode              not null,
   tariff_plan_code     TStr100              not null,
   tariff_plan_name     TStr100              not null,
   actual_date          TDate                not null,
   finish_date          TDate                null,
   constraint PK_TARIFFPLANS primary key (tariff_plan_id)
);

comment on table TariffPlans is
'Картотека тарифных планов';

/*==============================================================*/
/* Table: TariffRates                                           */
/*==============================================================*/
create table TariffRates (
   rate_id              TIdCode              not null,
   tariff_plan_id       TIdBigCode           not null,
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
   rate_date            TDate                not null,
   currency_id          TIdCode              not null,
   rate_value           TPercRate            null,
   constraint PK_TARIFFRATES_1 primary key (rate_id, rate_date, currency_id)
);

comment on table TariffRates_1 is
'Значения ставок (вариант 1)';

/*==============================================================*/
/* Table: TariffRates_2                                         */
/*==============================================================*/
create table TariffRates_2 (
   rate_id              TIdCode              not null,
   rate_date            TDate                not null,
   currency_id          TIdCode              not null,
   min_sum              TMoney               not null,
   max_sum              TMoney               not null,
   rate_value           TPercRate            not null,
   constraint PK_TARIFFRATES_2 primary key (rate_id, rate_date, currency_id, min_sum)
);

comment on table TariffRates_2 is
'Значения ставок (вариант 2)';

/*==============================================================*/
/* Table: TariffRates_3                                         */
/*==============================================================*/
create table TariffRates_3 (
   rate_id              TIdCode              not null,
   rate_date            TDate                not null,
   currency_id          TIdCode              not null,
   rate_value           TPercRate            not null,
   fix_sum              TMoney               not null,
   constraint PK_TARIFFRATES_3 primary key (rate_id, currency_id, rate_date)
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

/*==============================================================*/
/* Table: chess_CertTypeRef                                     */
/*==============================================================*/
create table chess_CertTypeRef (
   cert_type_id         TIdCode              not null,
   cert_type_name       TStr200              not null,
   constraint PK_CHESS_CERTTYPEREF primary key (cert_type_id)
);

comment on table chess_CertTypeRef is
'Справочник видов сертификатов участия';

/*==============================================================*/
/* Table: chess_Certificates                                    */
/*==============================================================*/
create table chess_Certificates (
   certificate_id       TIdBigCode           not null,
   cert_type_id         TIdCode              not null,
   player_id            TIdBigCode           not null,
   cert_issue_date      TDateTime            not null,
   game_date            TIdCode              not null,
   cert_info            TStr2000             not null,
   constraint PK_CHESS_CERTIFICATES primary key (certificate_id)
);

comment on table chess_Certificates is
'Картотека сертификатов';

/*==============================================================*/
/* Table: chess_CheckerboardActions                             */
/*==============================================================*/
create table chess_CheckerboardActions (
   move_action_id       TIdBigCode           not null,
   game_action_id       TIdBigCode           not null,
   piece_code           TStr3                not null,
   checkerboard_code    TStr2                not null,
   move_direction       TBoolean             not null,
   is_white             TBoolean             not null,
   constraint PK_CHESS_CHECKERBOARDACTIONS primary key (move_action_id)
);

comment on table chess_CheckerboardActions is
'Действие над шахматной клеткой (прибытие и убытие фигур)';

/*==============================================================*/
/* Table: chess_CheckerboardsRef                                */
/*==============================================================*/
create table chess_CheckerboardsRef (
   checkerboard_code    TStr2                not null,
   checkerboard_name    TStr100              not null,
   constraint PK_CHESS_CHECKERBOARDSREF primary key (checkerboard_code)
);

comment on table chess_CheckerboardsRef is
'Справочник адресов шахматных клеток';

/*==============================================================*/
/* Table: chess_EnginesRef                                      */
/*==============================================================*/
create table chess_EnginesRef (
   engine_id            TIdCode              not null,
   engine_code          TStr10               not null,
   engine_name          TStr200              not null,
   constraint PK_CHESS_ENGINESREF primary key (engine_id),
   constraint AK_ENGINE_CODE unique (engine_code)
);

comment on table chess_EnginesRef is
'Справочник шахматных движкой';

/*==============================================================*/
/* Table: chess_Federations                                     */
/*==============================================================*/
create table chess_Federations (
   federation_id        TIdBigCode           not null,
   federation_name      TStr128              not null,
   federation_code      TStr10               not null,
   parent_federation_id TIdBigCode           null,
   federation_country_id TIdCode              not null,
   is_member_fide       TBoolean             not null,
   constraint PK_CHESS_FEDERATIONS primary key (federation_id)
);

comment on table chess_Federations is
'Картотека федераций';

/*==============================================================*/
/* Table: chess_FreeChallenges                                  */
/*==============================================================*/
create table chess_FreeChallenges (
   challenge_id         TIdBigCode           not null,
   player_id            TIdBigCode           not null,
   training_elo         TIdCode              not null,
   white_is_desired     TBoolean             not null,
   partner_player_id    TIdBigCode           null,
   partner_training_elo TIdCode              null,
   constraint PK_CHESS_FREECHALLENGES primary key (challenge_id)
);

comment on table chess_FreeChallenges is
'Приглашение поиграть';

/*==============================================================*/
/* Table: chess_GameActions                                     */
/*==============================================================*/
create table chess_GameActions (
   game_action_id       TIdBigCode           not null,
   chess_game_id        TIdBigCode           not null,
   parent_game_action_id TIdBigCode           null,
   notice_code          TStr3                null,
   constraint PK_CHESS_GAMEACTIONS primary key (game_action_id)
);

comment on table chess_GameActions is
'Шахматные ходы';

/*==============================================================*/
/* Table: chess_GameActionsEstimations                          */
/*==============================================================*/
create table chess_GameActionsEstimations (
   game_action_id       TIdBigCode           not null,
   engine_id            TIdCode              not null,
   estimation           TPercRate            not null,
   constraint PK_CHESS_GAMEACTIONSESTIMATION primary key (game_action_id, engine_id)
);

comment on table chess_GameActionsEstimations is
'Оценки шахматных ходов';

/*==============================================================*/
/* Table: chess_GamesResultTypeRef                              */
/*==============================================================*/
create table chess_GamesResultTypeRef (
   game_result_type_id  TIdCode              not null,
   game_result_type_name TStr100              not null,
   constraint PK_CHESS_GAMESRESULTTYPEREF primary key (game_result_type_id)
);

comment on table chess_GamesResultTypeRef is
'Результаты окончания партий';

/*==============================================================*/
/* Table: chess_GamesTypeRef                                    */
/*==============================================================*/
create table chess_GamesTypeRef (
   game_type_id         TIdCode              not null,
   game_type_name       TStr100              not null,
   constraint PK_CHESS_GAMESTYPEREF primary key (game_type_id)
);

comment on table chess_GamesTypeRef is
'Тип игры';

/*==============================================================*/
/* Table: chess_InvitesTypesRef                                 */
/*==============================================================*/
create table chess_InvitesTypesRef (
   invite_type_id       TIdCode              not null,
   invite_type_name     TStr100              not null,
   constraint PK_CHESS_INVITESTYPESREF primary key (invite_type_id)
);

comment on table chess_InvitesTypesRef is
'Виды приглашений';

/*==============================================================*/
/* Table: chess_MovesNotationsRef                               */
/*==============================================================*/
create table chess_MovesNotationsRef (
   notice_code          TStr3                not null,
   notice_name          TStr100              not null,
   constraint PK_CHESS_MOVESNOTATIONSREF primary key (notice_code)
);

comment on table chess_MovesNotationsRef is
'Справочник нотаций к ходу';

/*==============================================================*/
/* Table: chess_PiecesRef                                       */
/*==============================================================*/
create table chess_PiecesRef (
   piece_code           TStr3                not null,
   piece_name           TStr100              not null,
   constraint PK_CHESS_PIECESREF primary key (piece_code)
);

comment on table chess_PiecesRef is
'Справочник шахматных фигур';

/*==============================================================*/
/* Table: chess_Player2Federation                               */
/*==============================================================*/
create table chess_Player2Federation (
   invite_id            TIdBigCode           not null,
   player_id            TIdBigCode           not null,
   federation_id        TIdBigCode           not null,
   membership_date      TDateTime            not null,
   constraint PK_CHESS_PLAYER2FEDERATION primary key (invite_id)
);

comment on table chess_Player2Federation is
'Членство игрока в федерации';

/*==============================================================*/
/* Table: chess_Players                                         */
/*==============================================================*/
create table chess_Players (
   player_id            TIdBigCode           not null,
   last_name            TStr100              not null,
   middle_name          TStr100              null,
   first_name           TStr100              null,
   birth_date           TDate                null,
   gender_id            TIdCode              null,
   country_id           TIdCode              null,
   language_id          TIdCode              null,
   avatar               TStr200              null,
   email                TStr100              null,
   is_verified          TBoolean             null,
   verify_date          TDate                null,
   constraint PK_CHESS_PLAYERS primary key (player_id)
);

comment on table chess_Players is
'Перечень игроков';

/*==============================================================*/
/* Table: chess_PlayersRatings                                  */
/*==============================================================*/
create table chess_PlayersRatings (
   rate_id              TIdBigCode           not null,
   player_id            TIdBigCode           not null,
   rating_type_id       TIdCode              not null,
   rating_value         TMoney               not null,
   rating_actual_date   TDateTime            not null,
   assign_note          TStr2000             not null,
   constraint PK_CHESS_PLAYERSRATINGS primary key (rate_id)
);

comment on table chess_PlayersRatings is
'Рейтинги игроков';

/*==============================================================*/
/* Table: chess_PrivateInvites                                  */
/*==============================================================*/
create table chess_PrivateInvites (
   invite_id            TIdBigCode           not null,
   player_id            TIdBigCode           not null,
   target_player_id     TIdBigCode           not null,
   invate_date          TDateTime            not null,
   game_date            TDate                not null,
   constraint PK_CHESS_PRIVATEINVITES primary key (invite_id)
);

comment on table chess_PrivateInvites is
'Перечень частных приглашений';

/*==============================================================*/
/* Table: chess_RatingTypeRef                                   */
/*==============================================================*/
create table chess_RatingTypeRef (
   rating_type_id       TIdCode              not null,
   rating_code_iso      TStr10               not null,
   rating_type_name     TStr100              not null,
   constraint PK_CHESS_RATINGTYPEREF primary key (rating_type_id),
   constraint AK_RTR_CODE_ISO unique (rating_code_iso)
);

comment on table chess_RatingTypeRef is
'Тип рейтинга';

/*==============================================================*/
/* Table: chess_SandBox                                         */
/*==============================================================*/
create table chess_SandBox (
   challenge_id         TIdBigCode           not null,
   white_player_id      TIdBigCode           not null,
   white_training_elo   TIdCode              not null,
   black_player_id      TIdBigCode           not null,
   black_training_elo   TIdCode              not null,
   white_points         TChessGameScore      not null,
   black_points         TChessGameScore      not null,
   white_rating_change  TidByteCode          null,
   black_rating_change  TidByteCode          null,
   pgn_moves            TStr200              not null,
   constraint PK_CHESS_SANDBOX primary key (challenge_id)
);

comment on table chess_SandBox is
'Шахматная песочница';

/*==============================================================*/
/* Table: chess_TimerAddAlgRef                                  */
/*==============================================================*/
create table chess_TimerAddAlgRef (
   timer_add_alg_id     TIdCode              not null,
   timer_add_alg_name   TStr200              not null,
   constraint PK_CHESS_TIMERADDALGREF primary key (timer_add_alg_id)
);

comment on table chess_TimerAddAlgRef is
'Код алгоритма добавления времени к ходу';

/*==============================================================*/
/* Table: chess_TimerTypesRef                                   */
/*==============================================================*/
create table chess_TimerTypesRef (
   timer_type_id        TIdCode              not null,
   timer_add_alg_id     TIdCode              not null,
   timer_type_name      TStr200              not null,
   constraint PK_CHESS_TIMERTYPESREF primary key (timer_type_id)
);

comment on table chess_TimerTypesRef is
'Виды временного учета шахматной партии';

/*==============================================================*/
/* Table: chess_Tournament                                      */
/*==============================================================*/
create table chess_Tournament (
   tournament_id        TIdBigCode           not null,
   federation_id        TIdBigCode           not null,
   tournament_name      TStr200              not null,
   tournament_date      TDate                not null,
   tournament_finish_date TDate                not null,
   constraint PK_CHESS_TOURNAMENT primary key (tournament_id)
);

comment on table chess_Tournament is
'Шахматные турниры';

/*==============================================================*/
/* Table: chess_TournamentGames                                 */
/*==============================================================*/
create table chess_TournamentGames (
   chess_game_id        TIdBigCode           not null,
   timer_type_id        TIdCode              not null,
   game_result_type_id  TIdCode              not null,
   white_player_id      TIdBigCode           not null,
   black_player_id      TIdBigCode           not null,
   game_start_date      TDateTime            not null,
   game_finish_date     TDateTime            null,
   white_player_points  TChessGameScore      null,
   black_player_points  TChessGameScore      null,
   tournament_id        TIdBigCode           not null,
   round_number         TIdCode              not null,
   constraint PK_CHESS_TOURNAMENTGAMES primary key (chess_game_id),
   constraint AK_WC_GAMES_WHITE unique (chess_game_id, white_player_id),
   constraint AK_WC_GAMES_BLACK unique (chess_game_id, black_player_id)
);

comment on table chess_TournamentGames is
'Шахматные партии';

/*==============================================================*/
/* Table: chess_TournamentInvites                               */
/*==============================================================*/
create table chess_TournamentInvites (
   invite_id            TIdBigCode           not null,
   player_id            TIdBigCode           not null,
   invoice_type_id      TIdCode              not null,
   invate_date          TDateTime            not null,
   turnament_date       TDate                not null,
   tournament_name      TStr2000             not null,
   tournament_start_date TDate                not null,
   tournament_finish_date TDate                not null,
   prize                TMoney               not null,
   constraint PK_CHESS_TOURNAMENTINVITES primary key (invite_id)
);

comment on table chess_TournamentInvites is
'Перечень приглашений';

/*==============================================================*/
/* Table: chess_TournamentRounds                                */
/*==============================================================*/
create table chess_TournamentRounds (
   tournament_id        TIdBigCode           not null,
   round_number         TIdCode              not null,
   constraint PK_CHESS_TOURNAMENTROUNDS primary key (tournament_id, round_number)
);

comment on table chess_TournamentRounds is
'Раунды шахматных турниров';

/*==============================================================*/
/* Table: core_Action2Role                                      */
/*==============================================================*/
create table core_Action2Role (
   action_code          TIdCode              not null,
   role_id              TIdBigCode           not null,
   assign_date          TDate                not null,
   constraint PK_CORE_ACTION2ROLE primary key (action_code, role_id)
);

comment on table core_Action2Role is
'Действия присвоенные роли';

/*==============================================================*/
/* Table: core_ActionCodesRef                                   */
/*==============================================================*/
create table core_ActionCodesRef (
   action_code          TIdCode              not null,
   action_name          TStr80               null,
   app_name             TStr100              not null,
   is_closed            TBoolean             not null,
   constraint PK_CORE_ACTIONCODESREF primary key (action_code)
);

comment on table core_ActionCodesRef is
'Справочник зарегистрированных регистрированных действий над терминалом';

/*==============================================================*/
/* Table: core_Actions                                          */
/*==============================================================*/
create table core_Actions (
   action_id            TIdBigCode           not null,
   entity_id            TIdBigCode           not null,
   action_code          TIdCode              not null,
   user_id              TIdBigCode           not null,
   execute_date         TDateTime            not null,
   action_address       TStr40               not null,
   err_msg              TText                null,
   action_duration      TTime                null,
   notes                TText                null,
   constraint PK_CORE_ACTIONS primary key (action_id)
);

comment on table core_Actions is
'Картотека выполненный действий пользователя 
';

/*==============================================================*/
/* Table: core_AppFieldsCaptions                                */
/*==============================================================*/
create table core_AppFieldsCaptions (
   user_id              TIdBigCode           not null,
   app_id               TIdCode              not null,
   field_name           TStr100              not null,
   field_caption        TStr100              not null,
   field_tooltip        TStr100              null,
   constraint PK_CORE_APPFIELDSCAPTIONS primary key (user_id, app_id, field_name)
);

comment on table core_AppFieldsCaptions is
'Картотека наименования полей сущностей
';

/*==============================================================*/
/* Table: core_Application2Role                                 */
/*==============================================================*/
create table core_Application2Role (
   app_id               TIdCode              not null,
   role_id              TIdBigCode           not null,
   assign_date          TDate                not null,
   constraint PK_CORE_APPLICATION2ROLE primary key (app_id, role_id)
);

comment on table core_Application2Role is
'Приложения присвоенные роли';

/*==============================================================*/
/* Table: core_ApplicationsRef                                  */
/*==============================================================*/
create table core_ApplicationsRef (
   app_id               TIdUser              not null,
   app_code             TStr30               not null,
   app_name             TStr50               not null,
   app_url              TStr100              not null,
   constraint PK_CORE_APPLICATIONSREF primary key (app_id)
);

comment on table core_ApplicationsRef is
'Приложения пользователей';

/*==============================================================*/
/* Table: core_ContractSubjectsRef                              */
/*==============================================================*/
create table core_ContractSubjectsRef (
   contract_subject_id  TIdCode              not null,
   contract_subject_name TStr80               not null,
   constraint PK_CORE_CONTRACTSUBJECTSREF primary key (contract_subject_id)
);

comment on table core_ContractSubjectsRef is
'Справочник предметов договоров';

/*==============================================================*/
/* Table: core_Counterparties                                   */
/*==============================================================*/
create table core_Counterparties (
   counterparty_id      TIdBigCode           not null,
   counterparty_code    TStr20               not null,
   short_name           TStr80               null,
   full_name            TStr200              not null,
   constraint PK_CORE_COUNTERPARTIES primary key (counterparty_id)
);

comment on table core_Counterparties is
'Клиенты';

/*==============================================================*/
/* Index: idx_counterparty_code                                 */
/*==============================================================*/
create unique index idx_counterparty_code on core_Counterparties (
counterparty_code
);

/*==============================================================*/
/* Table: core_CountriesRef                                     */
/*==============================================================*/
create table core_CountriesRef (
   country_id           TIdCode              not null,
   country_iso          TStr3                not null,
   country_name         TStr200              not null,
   constraint PK_CORE_COUNTRIESREF primary key (country_id)
);

comment on table core_CountriesRef is
'Справочник стран';

/*==============================================================*/
/* Table: core_CurrenciesGroupsRef                              */
/*==============================================================*/
create table core_CurrenciesGroupsRef (
   currency_group_id    TIdCode              not null,
   currency_group_name  TStr100              not null,
   constraint PK_CORE_CURRENCIESGROUPSREF primary key (currency_group_id)
);

comment on table core_CurrenciesGroupsRef is
'Справочник групп валют';

/*==============================================================*/
/* Table: core_CurrenciesRatesTypesRef                          */
/*==============================================================*/
create table core_CurrenciesRatesTypesRef (
   currency_rate_type_id TIdCode              not null,
   currency_rate_type_code TStr10               not null,
   currency_rate_type_name TStr100              not null,
   constraint PK_CORE_CURRENCIESRATESTYPESRE primary key (currency_rate_type_id)
);

comment on table core_CurrenciesRatesTypesRef is
'Справочник типов курсов валют';

/*==============================================================*/
/* Table: core_CurrenciesRef                                    */
/*==============================================================*/
create table core_CurrenciesRef (
   currency_id          TIdCode              not null,
   currency_iso         TStr3                not null,
   currency_name        TStr100              not null,
   constraint PK_CORE_CURRENCIESREF primary key (currency_id)
);

comment on table core_CurrenciesRef is
'Справочник валют';

/*==============================================================*/
/* Table: core_Currency2Group                                   */
/*==============================================================*/
create table core_Currency2Group (
   currency_id          TIdCode              not null,
   currency_group_id    TIdCode              not null,
   actual_date          TDate                not null,
   constraint PK_CORE_CURRENCY2GROUP primary key (currency_id, currency_group_id, actual_date)
);

comment on table core_Currency2Group is
'Валюты в группе валют';

/*==============================================================*/
/* Table: core_CurrencyRates                                    */
/*==============================================================*/
create table core_CurrencyRates (
   currency_rate_type_id TIdCode              not null,
   quoted_currency_id   TIdCode              not null,
   quoted_value         TPercRateExt         not null,
   rated_currency_id    TIdCode              not null,
   rated_value          TPercRateExt         not null,
   rate_date            TDate                not null,
   constraint PK_CORE_CURRENCYRATES primary key (currency_rate_type_id, quoted_currency_id, quoted_value, rated_currency_id, rated_value, rate_date)
);

comment on table core_CurrencyRates is
'Курсы валют (ценных бумаг)';

/*==============================================================*/
/* Table: core_EntAttrTemplatesRef                              */
/*==============================================================*/
create table core_EntAttrTemplatesRef (
   attr_template_id     TIdCode              not null,
   attr_id              TIdCode              not null,
   constraint PK_CORE_ENTATTRTEMPLATESREF primary key (attr_template_id, attr_id)
);

comment on table core_EntAttrTemplatesRef is
'Справочник шаблонов атрибутов сущности';

/*==============================================================*/
/* Table: core_EntAttrsRef                                      */
/*==============================================================*/
create table core_EntAttrsRef (
   attr_id              TIdCode              not null,
   attr_code            TStr100              not null,
   attr_name            TStr200              not null,
   constraint PK_CORE_ENTATTRSREF primary key (attr_id),
   constraint AK_CORE_ENTATTRSREF unique (attr_code)
);

comment on table core_EntAttrsRef is
'Справочник атрибутов';

/*==============================================================*/
/* Table: core_Entities                                         */
/*==============================================================*/
create table core_Entities (
   entity_id            TIdBigCode           not null,
   entity_type_id       TIdCode              not null,
   entity_status_id     TIdCode              not null,
   creation_date        TDate                not null,
   close_date           TDate                null,
   last_modify          TDateTime            null,
   constraint PK_CORE_ENTITIES primary key (entity_id)
);

comment on table core_Entities is
'Картотека сущностей';

/*==============================================================*/
/* Table: core_EntityAttrs                                      */
/*==============================================================*/
create table core_EntityAttrs (
   entity_id            TIdBigCode           not null,
   attr_id              TIdCode              not null,
   bigdecimal_value     TMoney               null,
   localdate_value      TDate                null,
   integer_value        TIdCode              null,
   string2000_value     TStr2000             null,
   string200_value      TStr200              null,
   boolean_value        TBoolean             null,
   localdatetime_value  TDateTime            null,
   constraint PK_CORE_ENTITYATTRS primary key (entity_id, attr_id)
);

comment on table core_EntityAttrs is
'Расширенные атрибуты сущности';

/*==============================================================*/
/* Table: core_EntityContracts                                  */
/*==============================================================*/
create table core_EntityContracts (
   contract_id          TIdBigCode           not null,
   contract_subject_id  TIdCode              not null,
   counterparty_id      TIdBigCode           not null,
   entity_kind_id       TIdCode              not null,
   currency_id          TIdCode              null,
   contract_num         TStr200              not null,
   contract_date        TDate                not null,
   begin_date           TDate                not null,
   end_date             TDate                not null,
   contract_summ        TMoney               null,
   tariff_plan_id       TIdBigCode           null,
   constraint PK_CORE_ENTITYCONTRACTS primary key (contract_id)
);

comment on table core_EntityContracts is
'Базовые сведения о контрактах';

/*==============================================================*/
/* Table: core_EntityKindsRef                                   */
/*==============================================================*/
create table core_EntityKindsRef (
   entity_kind_id       TIdCode              not null,
   entity_type_id       TIdCode              not null,
   entity_kind_name     TStr100              not null,
   constraint PK_CORE_ENTITYKINDSREF primary key (entity_kind_id)
);

comment on table core_EntityKindsRef is
'Cправочник видов сущностей';

/*==============================================================*/
/* Table: core_EntityMarks                                      */
/*==============================================================*/
create table core_EntityMarks (
   entity_id            TIdBigCode           not null,
   action_id            TIdBigCode           not null,
   mark_id              TIdCode              not null,
   mark_value_id        TIdCode              not null,
   mark_direction       TBoolean             not null,
   constraint PK_CORE_ENTITYMARKS primary key (entity_id, action_id)
);

comment on table core_EntityMarks is
'Отметки на сущности';

/*==============================================================*/
/* Table: core_EntityStatusesRef                                */
/*==============================================================*/
create table core_EntityStatusesRef (
   entity_status_id     TIdCode              not null,
   entity_type_id       TIdCode              not null,
   entity_status_name   TStr30               not null,
   constraint PK_CORE_ENTITYSTATUSESREF primary key (entity_status_id, entity_type_id)
);

comment on table core_EntityStatusesRef is
'Справочник статусов ';

/*==============================================================*/
/* Table: core_EntityTests                                      */
/*==============================================================*/
create table core_EntityTests (
   action_id            TIdBigCode           not null,
   log_body             TText                not null,
   constraint PK_CORE_ENTITYTESTS primary key (action_id)
);

comment on table core_EntityTests is
'Тестовые действия над сущностями';

/*==============================================================*/
/* Table: core_EntityTypesRef                                   */
/*==============================================================*/
create table core_EntityTypesRef (
   entity_type_id       TIdCode              not null,
   entity_type_name     TStr100              not null,
   entity_app_name      TStr100              not null,
   constraint PK_CORE_ENTITYTYPESREF primary key (entity_type_id)
);

comment on table core_EntityTypesRef is
'Cправочник сущностей';

/*==============================================================*/
/* Table: core_Function2Role                                    */
/*==============================================================*/
create table core_Function2Role (
   function_id          TIdCode              not null,
   role_id              TIdBigCode           not null,
   assign_date          TDate                not null,
   constraint PK_CORE_FUNCTION2ROLE primary key (function_id, role_id)
);

comment on table core_Function2Role is
'Функции присвоенные роли';

/*==============================================================*/
/* Table: core_FunctionsGroupsRef                               */
/*==============================================================*/
create table core_FunctionsGroupsRef (
   function_group_id    TIdUser              not null,
   function_group_code  TStr30               not null,
   function_group_name  TStr100              not null,
   constraint PK_CORE_FUNCTIONSGROUPSREF primary key (function_group_id)
);

comment on table core_FunctionsGroupsRef is
'Группы функций системы';

/*==============================================================*/
/* Table: core_FunctionsRef                                     */
/*==============================================================*/
create table core_FunctionsRef (
   function_id          TIdUser              not null,
   function_group_id    TIdCode              not null,
   function_code        TStr30               not null,
   function_name        TStr100              not null,
   constraint PK_CORE_FUNCTIONSREF primary key (function_id)
);

comment on table core_FunctionsRef is
'Функции пользователей';

/*==============================================================*/
/* Table: core_GenderRef                                        */
/*==============================================================*/
create table core_GenderRef (
   gender_id            TIdCode              not null,
   gender_code          TStr2                not null,
   gender_name          TStr100              not null,
   constraint PK_CORE_GENDERREF primary key (gender_id)
);

comment on table core_GenderRef is
'Справочник полов';

/*==============================================================*/
/* Table: core_LanguagesRef                                     */
/*==============================================================*/
create table core_LanguagesRef (
   language_id          TIdCode              not null,
   language_iso         TStr3                not null,
   language_name        TStr200              not null,
   constraint PK_CORE_LANGUAGESREF primary key (language_id)
);

comment on table core_LanguagesRef is
'Справочник языков';

/*==============================================================*/
/* Table: core_MarksRef                                         */
/*==============================================================*/
create table core_MarksRef (
   mark_id              TIdCode              not null,
   mark_name            TStr200              not null,
   mark_group           TStr100              not null,
   constraint PK_CORE_MARKSREF primary key (mark_id)
);

comment on table core_MarksRef is
'Справочник отметок на сущности';

/*==============================================================*/
/* Table: core_MarksValuesRef                                   */
/*==============================================================*/
create table core_MarksValuesRef (
   mark_id              TIdCode              not null,
   mark_value_id        TIdCode              not null,
   mark_value_name      TStr200              not null,
   constraint PK_CORE_MARKSVALUESREF primary key (mark_id, mark_value_id)
);

comment on table core_MarksValuesRef is
'Справочник значений отметок на сущности';

/*==============================================================*/
/* Table: core_PmtScheduleAlgsRef                               */
/*==============================================================*/
create table core_PmtScheduleAlgsRef (
   schedule_alg_id      TIdCode              not null,
   schedule_alg_name    TStr100              not null,
   is_actual            TBoolean             null,
   constraint PK_CORE_PMTSCHEDULEALGSREF primary key (schedule_alg_id)
);

comment on table core_PmtScheduleAlgsRef is
'Алгоритм построения графика';

/*==============================================================*/
/* Table: core_PmtScheduleLines                                 */
/*==============================================================*/
create table core_PmtScheduleLines (
   schedule_id          TIdBigCode           not null,
   actual_date          TDate                not null,
   from_date            TDate                not null,
   to_date              TDate                not null,
   appear_date          TDate                null,
   pay_sum              TMoney               null,
   calc_date            TDate                null,
   constraint PK_CORE_PMTSCHEDULELINES primary key (schedule_id, actual_date, from_date, to_date)
);

comment on table core_PmtScheduleLines is
'Строки графика обязательств по договору';

/*==============================================================*/
/* Table: core_PmtScheduleTermsRef                              */
/*==============================================================*/
create table core_PmtScheduleTermsRef (
   pmt_term_id          TIdCode              not null,
   pmt_term_name        TStr80               null,
   is_actual            TBoolean             null,
   constraint PK_CORE_PMTSCHEDULETERMSREF primary key (pmt_term_id)
);

comment on table core_PmtScheduleTermsRef is
'Виды периодичности платежей\сроков погашения';

/*==============================================================*/
/* Table: core_PmtSchedules                                     */
/*==============================================================*/
create table core_PmtSchedules (
   schedule_id          TIdBigCode           not null,
   contract_id          TIdBigCode           not null,
   entity_kind_id       TIdCode              not null,
   pmt_term_id          TIdCode              not null,
   schedule_alg_id      TIdCode              not null,
   from_date            TDate                null,
   last_date            TDate                null,
   constraint PK_CORE_PMTSCHEDULES primary key (schedule_id),
   constraint AK_PMTSCHEDULES unique (schedule_id, contract_id, entity_kind_id)
);

comment on table core_PmtSchedules is
'График обязательств по договору';

/*==============================================================*/
/* Index: schedule_contracts                                    */
/*==============================================================*/
create unique index schedule_contracts on core_PmtSchedules (
schedule_id,
contract_id,
entity_kind_id
);

/*==============================================================*/
/* Table: core_Roles                                            */
/*==============================================================*/
create table core_Roles (
   role_id              TIdBigCode           not null,
   role_code            TStr30               not null,
   role_name            TStr50               not null,
   constraint PK_CORE_ROLES primary key (role_id)
);

comment on table core_Roles is
'Роли пользователей';

/*==============================================================*/
/* Table: core_SessionTokens                                    */
/*==============================================================*/
create table core_SessionTokens (
   token_key            TStr200              not null,
   token_issue_date     TDateTime            not null,
   token_expiry         TDateTime            not null,
   token_type_id        TIdCode              not null,
   user_id              TIdBigCode           not null,
   constraint PK_CORE_SESSIONTOKENS primary key (token_key)
);

comment on table core_SessionTokens is
'Выданные токены';

/*==============================================================*/
/* Index: idx_user                                              */
/*==============================================================*/
create  index idx_user on core_SessionTokens (
user_id
);

/*==============================================================*/
/* Table: core_SessionTokensHist                                */
/*==============================================================*/
create table core_SessionTokensHist (
   token_key            TStr200              not null,
   token_issue_date     TDateTime            not null,
   token_expiry         TDateTime            not null,
   token_type_id        TIdCode              not null,
   user_id              TIdBigCode           not null,
   constraint PK_CORE_SESSIONTOKENSHIST primary key (token_key)
);

comment on table core_SessionTokensHist is
'Выданные токены - история';

/*==============================================================*/
/* Table: core_SessionTokensTypesRef                            */
/*==============================================================*/
create table core_SessionTokensTypesRef (
   token_type_id        TIdCode              not null,
   token_type_name      TStr200              not null,
   constraint PK_CORE_SESSIONTOKENSTYPESREF primary key (token_type_id)
);

comment on table core_SessionTokensTypesRef is
'Виды токенов сессионных';

/*==============================================================*/
/* Table: core_User2Role                                        */
/*==============================================================*/
create table core_User2Role (
   user_id              TIdBigCode           not null,
   role_id              TIdBigCode           not null,
   assign_date          TDate                not null,
   constraint PK_CORE_USER2ROLE primary key (user_id, role_id)
);

comment on table core_User2Role is
'Роли присвоенные пользователям';

/*==============================================================*/
/* Table: core_UserActivationCodes                              */
/*==============================================================*/
create table core_UserActivationCodes (
   user_id              TIdBigCode           not null,
   issue_date           TDateTime            not null,
   issue_expiry         TDateTime            not null,
   activation_key       TStr3                not null,
   constraint PK_CORE_USERACTIVATIONCODES primary key (user_id, issue_date)
);

comment on table core_UserActivationCodes is
'Коды активации пользователей';

/*==============================================================*/
/* Index: idx_user3                                             */
/*==============================================================*/
create  index idx_user3 on core_UserActivationCodes (
user_id
);

/*==============================================================*/
/* Table: core_Users                                            */
/*==============================================================*/
create table core_Users (
   user_id              TIdBigCode           not null,
   login                TStr20               not null,
   password             TStr100              not null,
   name                 TStr100              not null,
   phone                TStr30               null,
   mail                 TStr40               null,
   activation_date      TDateTime            null,
   constraint PK_CORE_USERS primary key (user_id)
);

comment on table core_Users is
'Пользователи';

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
   post_body            TStr2000             not null,
   create_date          TDateTime            not null,
   receive_date         TDateTime            null,
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

/*==============================================================*/
/* Table: pg_dual                                               */
/*==============================================================*/
create table pg_dual (
   dummy                TStr3                not null
);

comment on table pg_dual is
'Dual';

/*==============================================================*/
/* Table: rlc_LoanContracts                                     */
/*==============================================================*/
create table rlc_LoanContracts (
   contract_id          TIdBigCode           not null,
   loan_source_id       TIdCode              not null,
   pmt_term_id          TIdCode              not null,
   schedule_alg_id      TIdCode              null,
   constraint PK_RLC_LOANCONTRACTS primary key (contract_id)
);

comment on table rlc_LoanContracts is
'Базовые сведения о кредитном договоре';

/*==============================================================*/
/* Table: rlc_LoanSourcesRef                                    */
/*==============================================================*/
create table rlc_LoanSourcesRef (
   loan_source_id       TIdCode              not null,
   loan_source_name     TStr80               not null,
   constraint PK_RLC_LOANSOURCESREF primary key (loan_source_id)
);

comment on table rlc_LoanSourcesRef is
'Источники кредитования';

alter table DocAttrs
   add constraint FK_Doc2attrs_doc_attr_code foreign key (doc_attr_id)
      references DocAttrsRef (doc_attr_id)
      on delete restrict on update restrict;

alter table DocAttrs
   add constraint FK_Doc2attrs_doc_id foreign key (doc_id)
      references Documents (doc_id)
      on delete restrict on update restrict;

alter table DocTemplateAttrsRef
   add constraint FK_DTAR_doc_attr_code foreign key (doc_attr_id)
      references DocAttrsRef (doc_attr_id)
      on delete restrict on update restrict;

alter table DocTemplateAttrsRef
   add constraint FK_DTAR_doc_template_id foreign key (doc_template_id)
      references DocTemplatesRef (doc_template_id)
      on delete restrict on update restrict;

alter table DocTemplatesRef
   add constraint FK_DTAR_pmt_sys_id foreign key (pmt_sys_id)
      references PaymentSystemsRef (pmt_sys_id)
      on delete restrict on update restrict;

alter table DocTemplatesRef
   add constraint FK_DTR_doc_template_group_id foreign key (doc_template_group_id)
      references DocTemplateGroupsRef (doc_template_group_id)
      on delete restrict on update restrict;

alter table DocTemplatesRef
   add constraint FK_DTR_doc_type_id foreign key (doc_type_id)
      references DocTypesRef (doc_type_id)
      on delete restrict on update restrict;

alter table Documents
   add constraint FK_Document_user_id foreign key (user_id)
      references core_Users (user_id)
      on delete restrict on update restrict;

alter table Documents
   add constraint FK_Documents_doc_status_id foreign key (doc_status_id)
      references DocStatusesRef (doc_status_id)
      on delete restrict on update restrict;

alter table Documents
   add constraint FK_Documents_doc_template_id foreign key (doc_template_id)
      references DocTemplatesRef (doc_template_id)
      on delete restrict on update restrict;

alter table Documents
   add constraint FK_Documents_entity_id foreign key (entity_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;

alter table Documents
   add constraint FK_Documents_parent_doc_id foreign key (parent_doc_id)
      references Documents (doc_id)
      on delete restrict on update restrict;

alter table LiasActions
   add constraint FK_LiasActions_ActionTypeID foreign key (action_type_id)
      references LiasActionTypesRef (action_type_id)
      on delete restrict on update restrict;

alter table LiasActions
   add constraint FK_LiasActions_doc_id foreign key (doc_id)
      references Documents (doc_id)
      on delete restrict on update restrict;

alter table LiasActions
   add constraint FK_LiasActions_lias_action_id foreign key (lias_id)
      references Liases (lias_id)
      on delete restrict on update restrict;

alter table LiasActions
   add constraint FK_LiasActions_status_id foreign key (fin_oper_status_id)
      references LiasFinOperStatusesRef (fin_oper_status_id)
      on delete restrict on update restrict;

alter table LiasActions
   add constraint FK_la_fin_oper_code foreign key (fin_oper_code)
      references LiasFinOperCodesRef (fin_oper_code)
      on delete restrict on update restrict;

alter table LiasDebtRests
   add constraint FK_LiasDebtsRests_debt_id foreign key (debt_id)
      references LiasDebts (debt_id)
      on delete restrict on update restrict;

alter table LiasDebts
   add constraint FK_LiasDebts_counterpary_id foreign key (counterparty_id)
      references core_Counterparties (counterparty_id)
      on delete restrict on update restrict;

alter table LiasDebts
   add constraint FK_LiasDebts_currency_code foreign key (currency_id)
      references core_CurrenciesRef (currency_id)
      on delete restrict on update restrict;

alter table LiasDebts
   add constraint FK_liasDebts_base_asset_type_id foreign key (base_asset_type_id)
      references LiasBaseAssetTypesRef (base_asset_type_id)
      on delete restrict on update restrict;

alter table LiasDebts
   add constraint FK_liasDebts_contract_id foreign key (contract_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;

alter table LiasDebts
   add constraint FK_liasDebts_debt_state_id foreign key (debt_state_id)
      references LiasDebtStatesRef (debt_state_id)
      on delete restrict on update restrict;

alter table LiasDebts
   add constraint FK_liasDebts_lias_kind_id foreign key (lias_kind_id)
      references LiasKindsRef (lias_kind_id)
      on delete restrict on update restrict;

alter table LiasDebts
   add constraint FK_liasDebts_lias_type_id foreign key (lias_type_id)
      references LiasTypesRef (lias_type_id)
      on delete restrict on update restrict;

alter table LiasKindsRef
   add constraint FK_liasKinds_gri foreign key (lias_group_id)
      references LiasGroupsRef (lias_group_id)
      on delete restrict on update restrict;

alter table LiasRests
   add constraint FK_liasRests_lias_id foreign key (lias_id)
      references Liases (lias_id)
      on delete restrict on update restrict;

alter table Liases
   add constraint FK_liasDebts_debt_id foreign key (debt_id)
      references LiasDebts (debt_id)
      on delete restrict on update restrict;

alter table PaymentSystemDocTemplatesRef
   add constraint FK_PSA_pmt_sys_id foreign key (pmt_sys_id)
      references PaymentSystemsRef (pmt_sys_id)
      on delete restrict on update restrict;

alter table PaymentSystemDocTemplatesRef
   add constraint FK_PSDTR_doc_template_id foreign key (doc_template_id)
      references DocTemplatesRef (doc_template_id)
      on delete restrict on update restrict;

alter table PaymentSystemDocTemplatesRef
   add constraint FK_PSDTR_fin_oper_code foreign key (fin_oper_code)
      references LiasFinOperCodesRef (fin_oper_code)
      on delete restrict on update restrict;

alter table TariffAccretionsHist
   add constraint FK_TAH_contract_id foreign key (contract_id)
      references core_EntityContracts (contract_id)
      on delete restrict on update restrict;

alter table TariffAccretionsHist
   add constraint FK_TAH_lias_action_id foreign key (lias_action_id)
      references LiasActions (lias_action_id)
      on delete restrict on update restrict;

alter table TariffAccretionsHist
   add constraint FK_TAH_tariff_kind_id foreign key (tariff_kind_id)
      references TariffKindsRef (tariff_kind_id)
      on delete restrict on update restrict;

alter table TariffAccretionsHist
   add constraint FK_TAH_tariff_serv_id foreign key (tariff_serv_id)
      references TariffServsRef (tariff_serv_id)
      on delete restrict on update restrict;

alter table TariffCalcRecords
   add constraint FK_TCR_entity_id foreign key (entity_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;

alter table TariffCalcRecords
   add constraint FK_TKR_rate_id foreign key (rate_id)
      references TariffRates (rate_id)
      on delete restrict on update restrict;

alter table TariffCalcSum
   add constraint FK_TCS_tariff_calc_id foreign key (tariff_calc_id)
      references TariffCalcRecords (tariff_calc_id)
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

alter table TariffPlanAccretions
   add constraint FK_tariff_plan_id foreign key (tariff_plan_id)
      references TariffPlans (tariff_plan_id)
      on delete restrict on update restrict;

alter table TariffPlans
   add constraint FK_TariffPlans_kind_id foreign key (tariff_plan_kind_id)
      references core_EntityKindsRef (entity_kind_id)
      on delete restrict on update restrict;

alter table TariffPlans
   add constraint FK_TariffPlans_plan_id foreign key (tariff_plan_id)
      references core_Entities (entity_id)
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

alter table chess_Certificates
   add constraint FK_chess_cert_cert_id foreign key (certificate_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;

alter table chess_Certificates
   add constraint FK_chess_cert_player_id foreign key (player_id)
      references chess_Players (player_id)
      on delete restrict on update restrict;

alter table chess_Certificates
   add constraint FK_chess_cert_type_id foreign key (cert_type_id)
      references chess_CertTypeRef (cert_type_id)
      on delete restrict on update restrict;

alter table chess_CheckerboardActions
   add constraint FK_chess_cb_action_piece foreign key (piece_code)
      references chess_PiecesRef (piece_code)
      on delete restrict on update restrict;

alter table chess_CheckerboardActions
   add constraint FK_chess_cb_game_action foreign key (game_action_id)
      references chess_GameActions (game_action_id)
      on delete restrict on update restrict;

alter table chess_CheckerboardActions
   add constraint FK_chess_cp_checkboard_run foreign key (checkerboard_code)
      references chess_CheckerboardsRef (checkerboard_code)
      on delete restrict on update restrict;

alter table chess_Federations
   add constraint FK_chess_fed_pfed_id foreign key (parent_federation_id)
      references chess_Federations (federation_id)
      on delete restrict on update restrict;

alter table chess_FreeChallenges
   add constraint FK_chess_FC_challenge_id foreign key (challenge_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;

alter table chess_FreeChallenges
   add constraint FK_chess_fc_player_id foreign key (player_id)
      references chess_Players (player_id)
      on delete restrict on update restrict;

alter table chess_GameActions
   add constraint FK_chess_ga_moves_results foreign key (notice_code)
      references chess_MovesNotationsRef (notice_code)
      on delete restrict on update restrict;

alter table chess_GameActions
   add constraint FK_chess_ga_parent_move foreign key (parent_game_action_id)
      references chess_GameActions (game_action_id)
      on delete restrict on update restrict;

alter table chess_GameActions
   add constraint FK_chess_party_id foreign key (chess_game_id)
      references chess_TournamentGames (chess_game_id)
      on delete restrict on update restrict;

alter table chess_GameActionsEstimations
   add constraint FK_chess_ga_estimations foreign key (game_action_id)
      references chess_GameActions (game_action_id)
      on delete restrict on update restrict;

alter table chess_GameActionsEstimations
   add constraint FK_chess_me_engine_id foreign key (engine_id)
      references chess_EnginesRef (engine_id)
      on delete restrict on update restrict;

alter table chess_Player2Federation
   add constraint FK_chess_P2F_federation_id foreign key (federation_id)
      references chess_Federations (federation_id)
      on delete restrict on update restrict;

alter table chess_Players
   add constraint FK_chess_Players_langId foreign key (language_id)
      references core_LanguagesRef (language_id)
      on delete restrict on update restrict;

alter table chess_Players
   add constraint FK_chess_player_country_id foreign key (country_id)
      references core_CountriesRef (country_id)
      on delete restrict on update restrict;

alter table chess_Players
   add constraint FK_chess_players_player_id foreign key (player_id)
      references core_Users (user_id)
      on delete restrict on update restrict;

alter table chess_Players
   add constraint FK_core_players_gender_id foreign key (gender_id)
      references core_GenderRef (gender_id)
      on delete restrict on update restrict;

alter table chess_PlayersRatings
   add constraint FK_chess_pr_player_id foreign key (player_id)
      references chess_Players (player_id)
      on delete restrict on update restrict;

alter table chess_PlayersRatings
   add constraint FK_chess_pr_rate_id foreign key (rate_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;

alter table chess_PlayersRatings
   add constraint FK_chess_pr_rating_type_id foreign key (rating_type_id)
      references chess_RatingTypeRef (rating_type_id)
      on delete restrict on update restrict;

alter table chess_PrivateInvites
   add constraint FK_chess_PI_target_player_id foreign key (target_player_id)
      references chess_Players (player_id)
      on delete restrict on update restrict;

alter table chess_PrivateInvites
   add constraint FK_chess_pi_invite_id foreign key (invite_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;

alter table chess_PrivateInvites
   add constraint FK_chess_pi_player_id foreign key (player_id)
      references chess_Players (player_id)
      on delete restrict on update restrict;

alter table chess_SandBox
   add constraint FK_chess_SB_challenge_id foreign key (challenge_id)
      references chess_FreeChallenges (challenge_id)
      on delete restrict on update restrict;

alter table chess_TimerTypesRef
   add constraint FK_chess_ttr_add_alg_id foreign key (timer_add_alg_id)
      references chess_TimerAddAlgRef (timer_add_alg_id)
      on delete restrict on update restrict;

alter table chess_Tournament
   add constraint FK_chess_tournament_fed_id foreign key (federation_id)
      references chess_Federations (federation_id)
      on delete restrict on update restrict;

alter table chess_TournamentGames
   add constraint FK_chess_TG_round_number foreign key (tournament_id, round_number)
      references chess_TournamentRounds (tournament_id, round_number)
      on delete restrict on update restrict;

alter table chess_TournamentGames
   add constraint FK_chess_cg_black_player_id foreign key (black_player_id)
      references chess_Players (player_id)
      on delete restrict on update restrict;

alter table chess_TournamentGames
   add constraint FK_chess_Games_game_id foreign key (chess_game_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;

alter table chess_TournamentGames
   add constraint FK_chess_games_result_type_id foreign key (game_result_type_id)
      references chess_GamesResultTypeRef (game_result_type_id)
      on delete restrict on update restrict;

alter table chess_TournamentGames
   add constraint FK_chess_Games_timer_type_id foreign key (timer_type_id)
      references chess_TimerTypesRef (timer_type_id)
      on delete restrict on update restrict;

alter table chess_TournamentGames
   add constraint FK_chess_tg_white_player_id foreign key (white_player_id)
      references chess_Players (player_id)
      on delete restrict on update restrict;

alter table chess_TournamentInvites
   add constraint FK_chess_Invite_type_id foreign key (invoice_type_id)
      references chess_InvitesTypesRef (invite_type_id)
      on delete restrict on update restrict;

alter table chess_TournamentInvites
   add constraint FK_chess_Invites_Player_id foreign key (player_id)
      references chess_Players (player_id)
      on delete restrict on update restrict;

alter table chess_TournamentInvites
   add constraint FK_chess_Invites_id foreign key (invite_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;

alter table chess_TournamentRounds
   add constraint FK_chess_tr_tournament_id foreign key (tournament_id)
      references chess_Tournament (tournament_id)
      on delete restrict on update restrict;

alter table core_Action2Role
   add constraint FK_CORE_ACT_AR2R_ACTR_CORE_ACT foreign key (action_code)
      references core_ActionCodesRef (action_code)
      on delete restrict on update restrict;

alter table core_Action2Role
   add constraint FK_CORE_ACT_AR2R_ROLE_CORE_ROL foreign key (role_id)
      references core_Roles (role_id)
      on delete restrict on update restrict;

alter table core_Actions
   add constraint FK_Actions_user_id foreign key (user_id)
      references core_Users (user_id)
      on delete restrict on update restrict;

alter table core_Actions
   add constraint FK_TA_ACTREFID foreign key (action_code)
      references core_ActionCodesRef (action_code)
      on delete restrict on update restrict;

alter table core_Actions
   add constraint FK_actions_entity_id foreign key (entity_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;

alter table core_AppFieldsCaptions
   add constraint FK_AFC_APP_ID foreign key (app_id)
      references core_ApplicationsRef (app_id)
      on delete restrict on update restrict;

alter table core_AppFieldsCaptions
   add constraint FK_AFC_USER_ID foreign key (user_id)
      references core_Users (user_id)
      on delete restrict on update restrict;

alter table core_Application2Role
   add constraint FK_CORE_APP_A2R_APP_I_CORE_APP foreign key (app_id)
      references core_ApplicationsRef (app_id)
      on delete restrict on update restrict;

alter table core_Application2Role
   add constraint FK_CORE_APP_A2R_ROLE__CORE_ROL foreign key (role_id)
      references core_Roles (role_id)
      on delete restrict on update restrict;

alter table core_Counterparties
   add constraint FK_CP_COUNTERPARTY_ID foreign key (counterparty_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;

alter table core_Currency2Group
   add constraint FK_Currency2Group_currency_id foreign key (currency_id)
      references core_CurrenciesRef (currency_id)
      on delete restrict on update restrict;

alter table core_Currency2Group
   add constraint FK_Currency2Group_group_id foreign key (currency_group_id)
      references core_CurrenciesGroupsRef (currency_group_id)
      on delete restrict on update restrict;

alter table core_CurrencyRates
   add constraint FK_CR_rate_type_id foreign key (currency_rate_type_id)
      references core_CurrenciesRatesTypesRef (currency_rate_type_id)
      on delete restrict on update restrict;

alter table core_CurrencyRates
   add constraint FK_CRate_quoted_currency_id foreign key (quoted_currency_id)
      references core_CurrenciesRef (currency_id)
      on delete restrict on update restrict;

alter table core_CurrencyRates
   add constraint FK_CRate_rated_currency_id foreign key (rated_currency_id)
      references core_CurrenciesRef (currency_id)
      on delete restrict on update restrict;

alter table core_EntAttrTemplatesRef
   add constraint FK_core_EATR_attr_id foreign key (attr_id)
      references core_EntAttrsRef (attr_id)
      on delete restrict on update restrict;

alter table core_Entities
   add constraint FK_CORE_ENT_ENTITY_ST_CORE_ENT foreign key (entity_status_id, entity_type_id)
      references core_EntityStatusesRef (entity_status_id, entity_type_id)
      on delete restrict on update restrict;

alter table core_Entities
   add constraint FK_CORE_ENT_ENTITY_TY_CORE_ENT foreign key (entity_type_id)
      references core_EntityTypesRef (entity_type_id)
      on delete restrict on update restrict;

alter table core_EntityAttrs
   add constraint FK_core_EA_attr_id foreign key (attr_id)
      references core_EntAttrsRef (attr_id)
      on delete restrict on update restrict;

alter table core_EntityAttrs
   add constraint FK_core_EA_entity_id foreign key (entity_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;

alter table core_EntityContracts
   add constraint FK_core_EC_tariff_plan_id foreign key (tariff_plan_id)
      references TariffPlans (tariff_plan_id)
      on delete restrict on update restrict;

alter table core_EntityContracts
   add constraint FK_CORE_ENT_CS_ID_CORE_CON foreign key (contract_subject_id)
      references core_ContractSubjectsRef (contract_subject_id)
      on delete restrict on update restrict;

alter table core_EntityContracts
   add constraint FK_ec_contract_id foreign key (contract_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;

alter table core_EntityContracts
   add constraint FK_ec_counterparty_id foreign key (counterparty_id)
      references core_Counterparties (counterparty_id)
      on delete restrict on update restrict;

alter table core_EntityContracts
   add constraint FK_EC_CURRENCY_CODE foreign key (currency_id)
      references core_CurrenciesRef (currency_id)
      on delete restrict on update restrict;

alter table core_EntityContracts
   add constraint FK_EC_ENTITY_KIND_ID foreign key (entity_kind_id)
      references core_EntityKindsRef (entity_kind_id)
      on delete restrict on update restrict;

alter table core_EntityKindsRef
   add constraint FK_CORE_ENT_ENTITY_KI_CORE_ENT foreign key (entity_type_id)
      references core_EntityTypesRef (entity_type_id)
      on delete restrict on update restrict;

alter table core_EntityMarks
   add constraint FK_E2M_EntityId foreign key (entity_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;

alter table core_EntityMarks
   add constraint FK_E2M_action_id foreign key (action_id)
      references core_Actions (action_id)
      on delete restrict on update restrict;

alter table core_EntityMarks
   add constraint FK_EM_mark_value_id foreign key (mark_id, mark_value_id)
      references core_MarksValuesRef (mark_id, mark_value_id)
      on delete restrict on update restrict;

alter table core_EntityStatusesRef
   add constraint FK_CORE_ENT_ENTITY_TY_CORE_ENT foreign key (entity_type_id)
      references core_EntityTypesRef (entity_type_id)
      on delete restrict on update restrict;

alter table core_EntityTests
   add constraint FK_ET_action_id foreign key (action_id)
      references core_Actions (action_id)
      on delete restrict on update restrict;

alter table core_Function2Role
   add constraint FK_f2r_function_id foreign key (function_id)
      references core_FunctionsRef (function_id)
      on delete restrict on update restrict;

alter table core_Function2Role
   add constraint FK_CORE_FUN_F2R_ROLE__CORE_ROL foreign key (role_id)
      references core_Roles (role_id)
      on delete restrict on update restrict;

alter table core_FunctionsRef
   add constraint FK_FGR_function_group_id foreign key (function_group_id)
      references core_FunctionsGroupsRef (function_group_id)
      on delete restrict on update restrict;

alter table core_MarksValuesRef
   add constraint FK_MVR_mark_id foreign key (mark_id)
      references core_MarksRef (mark_id)
      on delete restrict on update restrict;

alter table core_PmtScheduleLines
   add constraint FK_CORE_PMT_SCHEDULES_CORE_PMT foreign key (schedule_id)
      references core_PmtSchedules (schedule_id)
      on delete restrict on update restrict;

alter table core_PmtSchedules
   add constraint FK_PMTSCHED_CONTRACT_ID foreign key (contract_id)
      references core_EntityContracts (contract_id)
      on delete restrict on update restrict;

alter table core_PmtSchedules
   add constraint FK_PMTSCHED_ENTITY_KIND_ID foreign key (entity_kind_id)
      references core_EntityKindsRef (entity_kind_id)
      on delete restrict on update restrict;

alter table core_PmtSchedules
   add constraint FK_PMTSCHED_PMT_TERM_ID foreign key (pmt_term_id)
      references core_PmtScheduleTermsRef (pmt_term_id)
      on delete restrict on update restrict;

alter table core_PmtSchedules
   add constraint FK_PMTSCHED_SCHEDULE_ALG_ID foreign key (schedule_alg_id)
      references core_PmtScheduleAlgsRef (schedule_alg_id)
      on delete restrict on update restrict;

alter table core_PmtSchedules
   add constraint FK_CORE_PMT_SCHEDULE__CORE_ENT foreign key (schedule_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;

alter table core_Roles
   add constraint FK_roles_entity_id foreign key (role_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;

alter table core_SessionTokens
   add constraint FK_ST_token_type_id foreign key (token_type_id)
      references core_SessionTokensTypesRef (token_type_id)
      on delete restrict on update restrict;

alter table core_SessionTokens
   add constraint FK_core_ST_user_id foreign key (user_id)
      references core_Users (user_id)
      on delete restrict on update restrict;

alter table core_User2Role
   add constraint FK_u2r_role_id foreign key (role_id)
      references core_Roles (role_id)
      on delete restrict on update restrict;

alter table core_User2Role
   add constraint FK_u2r_user_id foreign key (user_id)
      references core_Users (user_id)
      on delete restrict on update restrict;

alter table core_UserActivationCodes
   add constraint FK_core_UAC_user_id foreign key (user_id)
      references core_Users (user_id)
      on delete restrict on update restrict;

alter table core_Users
   add constraint FK_users_user_entity_id foreign key (user_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;

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

alter table rlc_LoanContracts
   add constraint FK_LoanContract_pmt_term_id foreign key (pmt_term_id)
      references core_PmtScheduleTermsRef (pmt_term_id)
      on delete restrict on update restrict;

alter table rlc_LoanContracts
   add constraint FK_LoanContracts_s_alg_id foreign key (schedule_alg_id)
      references core_PmtScheduleAlgsRef (schedule_alg_id)
      on delete restrict on update restrict;

alter table rlc_LoanContracts
   add constraint FK_LC_CONTRACT_ID foreign key (contract_id)
      references core_EntityContracts (contract_id)
      on delete restrict on update restrict;

alter table rlc_LoanContracts
   add constraint FK_LSR_SOURCE_ID foreign key (loan_source_id)
      references rlc_LoanSourcesRef (loan_source_id)
      on delete restrict on update restrict;

