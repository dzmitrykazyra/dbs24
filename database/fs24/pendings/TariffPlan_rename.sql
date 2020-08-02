ALTER TABLE TariffPlans
RENAME COLUMN close_date TO finish_date;

ALTER TABLE TariffPlans
RENAME COLUMN entity_kind_id TO tariff_kind_id;

ALTER TABLE TariffPlans
ADD COLUMN tariff_plan_code TStr100;

ALTER TABLE TariffPlans
ALTER COLUMN tariff_plan_code SET NOT NULL;

comment on table TariffPlans is
'Картотека тарифных планов';

alter table TariffPlans
   add constraint FK_TariffPlans_kind_id foreign key (tariff_kind_id)
      references core_EntityKindsRef (entity_kind_id)
      on delete restrict on update restrict;

alter table TariffPlans
   add constraint FK_TariffPlans_plan_id foreign key (tariff_plan_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;