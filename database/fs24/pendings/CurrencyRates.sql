/*==============================================================*/
/* Table: CurrenciesRatesTypesRef                               */
/*==============================================================*/
create table CurrenciesRatesTypesRef (
   currency_rate_type_id TIdCode              not null,
   currency_rate_type_code TStr10               not null,
   currency_rate_type_name TStr100              not null,
   constraint PK_CURRENCIESRATESTYPESREF primary key (currency_rate_type_id)
);

comment on table CurrenciesRatesTypesRef is
'Справочник типов курсов валют';


/*==============================================================*/
/* Table: CurrenciesGroupsRef                                   */
/*==============================================================*/
create table CurrenciesGroupsRef (
   currency_group_id    TIdCode              not null,
   currency_group_name  TStr100              not null,
   constraint PK_CURRENCIESGROUPSREF primary key (currency_group_id)
);

comment on table CurrenciesGroupsRef is
'Справочник групп валют';

/*==============================================================*/
/* Table: Currency2Group                                        */
/*==============================================================*/
create table Currency2Group (
   currency_id          TIdCode              not null,
   currency_group_id    TIdCode              not null,
   actual_date          TDate                not null,
   constraint PK_CURRENCY2GROUP primary key (currency_id, currency_group_id, actual_date)
);

comment on table Currency2Group is
'Валюты в группе валют';

alter table Currency2Group
   add constraint FK_Currency2Group_currency_id foreign key (currency_id)
      references CurrenciesRef (currency_id)
      on delete restrict on update restrict;

alter table Currency2Group
   add constraint FK_Currency2Group_group_id foreign key (currency_group_id)
      references CurrenciesGroupsRef (currency_group_id)
      on delete restrict on update restrict;


/*==============================================================*/
/* Table: CurrencyRates                                         */
/*==============================================================*/
create table CurrencyRates (
   currency_rate_type_id TIdCode              not null,
   quoted_currency_id   TIdCode              not null,
   quoted_value         TPercRateExt         not null,
   rated_currency_id    TIdCode              not null,
   rated_value          TPercRateExt         not null,
   rate_date            TDate                not null,
   constraint PK_CURRENCYRATES primary key (currency_rate_type_id, quoted_currency_id, quoted_value, rated_currency_id, rated_value, rate_date)
);

comment on table CurrencyRates is
'Курсы валют (ценных бумаг)';

alter table CurrencyRates
   add constraint FK_CR_rate_type_id foreign key (currency_rate_type_id)
      references CurrenciesRatesTypesRef (currency_rate_type_id)
      on delete restrict on update restrict;

alter table CurrencyRates
   add constraint FK_CRate_quoted_currency_id foreign key (quoted_currency_id)
      references CurrenciesRef (currency_id)
      on delete restrict on update restrict;

alter table CurrencyRates
   add constraint FK_CRate_rated_currency_id foreign key (rated_currency_id)
      references CurrenciesRef (currency_id)
      on delete restrict on update restrict;
