ALTER TABLE core_EntityContracts
ADD COLUMN tariff_plan_id TIdBigCode;


alter table core_EntityContracts
   add constraint FK_core_EC_tariff_plan_id foreign key (tariff_plan_id)
      references TariffPlans (tariff_plan_id)
      on delete restrict on update restrict;

drop table TariffPlan2EntityContract;
