create table TariffPlanAccretions (
   id                   TIdBigCode           not null,
   tariff_plan_id       TIdCode              null,
   accretion_plan_date  TDate                not null,
   notes                TStr200              null,
   constraint PK_TARIFFPLANACCRETIONS primary key (id)
);

comment on table TariffPlanAccretions is
'Плановые даты начисления';

alter table TariffPlanAccretions
   add constraint FK_tariff_plan_id foreign key (tariff_plan_id)
      references TariffPlans (tariff_plan_id)
      on delete restrict on update restrict;


create sequence seq_TariffPlanAccretions
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;