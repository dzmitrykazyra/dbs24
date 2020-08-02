drop table TariffCalculations;


drop table TariffCalcRecords;

/*==============================================================*/
/* Table: TariffCalcRecords                                     */
/*==============================================================*/
create table TariffCalcRecords (
   tariff_calc_id        TIdCode              not null,
   rate_id              TIdCode              not null,
   entity_id            TIdBigCode           not null,
   constraint PK_TARIFFCALCRECORDS primary key (tariff_calc_id)
);

comment on table TariffCalcRecords is
'Ключ тарифа';

alter table TariffCalcRecords
   add constraint FK_TCR_entity_id foreign key (entity_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;

alter table TariffCalcRecords
   add constraint FK_TKR_rate_id foreign key (rate_id)
      references TariffRates (rate_id)
      on delete restrict on update restrict;


drop table TariffCalcSum;

/*==============================================================*/
/* Table: TariffCalcSum                                         */
/*==============================================================*/
create table TariffCalcSum (
   tariff_calc_id        TIdCode              not null,
   tariff_calc_date     TDate                not null,
   tariff_summ          TMoney               not null,
   constraint PK_TARIFFCALCSUM primary key (tariff_calc_id, tariff_calc_date)
);

comment on table TariffCalcSum is
'Суммы рассчитанные тарифами';

alter table TariffCalcSum
   add constraint FK_TCS_tariff_calc_id foreign key (tariff_calc_id)
      references TariffCalcRecords (tariff_calc_id)
      on delete restrict on update restrict;



create sequence seq_TariffCalcRecords
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;

