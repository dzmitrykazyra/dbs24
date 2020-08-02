create table LiasGroupsRef (
   lias_group_id        TIdCode              not null,
   lias_group_name      TStr100              not null,
   constraint PK_LIASGROUPS primary key (lias_group_id)
);

create table LiasKindsRef (
   lias_kind_id         TIdCode              not null,
   Is_claim             TBoolean             not null,
   lias_group_id        TIdCode              not null,
   lias_kind_name       TStr100              not null,
   constraint PK_LIASKINDS primary key (lias_kind_id)
);

alter table LiasKindsRef
   add constraint FK_liasKinds_gri foreign key (lias_group_id)
      references LiasGroupsRef (lias_group_id)
      on delete restrict on update restrict;

create table LiasBaseAssetTypesRef (
   base_asset_type_id   TIdCode              not null,
   base_asset_type_name  TStr100              not null,
   constraint PK_LIASBASEASSETTYPES primary key (base_asset_type_id)
);

create table LiasDebtStatesRef (
   debt_state_id        TIdCode              not null,
   debt_state_name      TStr100              not null,
   constraint PK_LIASDEBTSTATES primary key (debt_state_id)
);

create table LiasTypesRef (
   lias_type_id         TIdCode              not null,
   lias_type_name       TStr100              not null,
   constraint PK_LIASTYPES primary key (lias_type_id)
);


create table LiasActionTypesRef (
   action_type_id       TIdCode              not null,
   change_rest_tag      TBoolean             not null,
   action_type_name     TStr100              not null,
   constraint PK_LIASACTIONTYPES primary key (action_type_id)
);

create table LiasFinOperCodesRef (
   fin_oper_code        TIdCode              not null,
   fin_oper_name        TStr80               not null,
   constraint PK_LIASFINOPERCODES primary key (fin_oper_code)
);


/*==============================================================*/
/* Table: LiasDebts                                             */
/*==============================================================*/
create table LiasDebts (
   debt_id              TIdCode              not null,
   counterparty_id      TIdCode              not null,
   currency_code        TIdCode              not null,
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

alter table LiasDebts
   add constraint FK_LiasDebts_counterpary_id foreign key (counterparty_id)
      references Counterparties (counterparty_id)
      on delete restrict on update restrict;

alter table LiasDebts
   add constraint FK_LiasDebts_currency_code foreign key (currency_code)
      references CurrenciesRef (currency_code)
      on delete restrict on update restrict;

alter table LiasDebts
   add constraint FK_liasDebts_base_asset_type_id foreign key (base_asset_type_id)
      references LiasBaseAssetTypesRef (base_asset_type_id)
      on delete restrict on update restrict;

alter table LiasDebts
   add constraint FK_liasDebts_contract_id foreign key (contract_id)
      references Entities (entity_id)
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


create table LiasDebtRests (
   debt_id              TIdCode              not null,
   rest_type            TIdCode              not null,
   rest_date            TDate                not null,
   rest                 TMoney               not null,
   constraint PK_LIASDEBTRESTS primary key (rest_type, debt_id, rest_date),
   constraint CKT_DebtRests_Rest_Negative check (not(Rest<0))
);

alter table LiasDebtRests
   add constraint FK_LiasDebtsRests_debt_id foreign key (debt_id)
      references LiasDebts (debt_id)
      on delete restrict on update restrict;


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
   server_date          TDate                not null,
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

alter table Liases
   add constraint FK_liasDebts_debt_id foreign key (debt_id)
      references LiasDebts (debt_id)
      on delete restrict on update restrict;


create table LiasRests (
   lias_id              TIdCode              not null,
   rest_type            TIdCode              not null,
   rest_date            TDate                not null,
   rest                 TMoney               not null,
   constraint PK_LIASRESTS primary key (rest_type, lias_id, rest_date),
   constraint CKT_LiasRests_Rest_Negative check (not(Rest<0))
);

alter table LiasRests
   add constraint FK_liasRests_lias_id foreign key (lias_id)
      references Liases (lias_id)
      on delete restrict on update restrict;

/*==============================================================*/
/* Table: LiasActions                                           */
/*==============================================================*/
create table LiasActions (
   lias_action_id       TIdCode              not null,
   action_type_id       TIdCode              not null,
   lias_id              TIdCode              not null,
   lias_date            TDate                not null,
   oper_date            TDate                null,
   server_date          TDate                not null,
   status               TIdCode              null,
   lias_sum             TMoney               null,
   fin_oper_code        TIdCode              not null,
   constraint PK_LIASACTIONS primary key (lias_action_id)
);

/*==============================================================*/
/* Index: iLiasActions_Type_Status_LDate                        */
/*==============================================================*/
create  index iLiasActions_Type_Status_LDate on LiasActions (
action_type_id,
status,
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
status,
lias_id
);

alter table LiasActions
   add constraint FK_LiasActions_ActionTypeID foreign key (action_type_id)
      references LiasActionTypesRef (action_type_id)
      on delete restrict on update restrict;

alter table LiasActions
   add constraint FK_LiasActions_lias_action_id foreign key (lias_id)
      references Liases (lias_id)
      on delete restrict on update restrict;

alter table LiasActions
   add constraint FK_la_fin_oper_code foreign key (fin_oper_code)
      references LiasFinOperCodesRef (fin_oper_code)
      on delete restrict on update restrict;
