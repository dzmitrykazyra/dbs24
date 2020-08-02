--drop table PmtScheduleTermsRef;

/*==============================================================*/
/* Table: PmtScheduleTermsRef                                   */
/*==============================================================*/
create table PmtScheduleTermsRef (
   pmt_term_id          TIdCode              not null,
   pmt_term_name        TStr80               null,
   is_actual            TBoolean             null,
   constraint PK_PMTSCHEDULETERMSREF primary key (pmt_term_id)
);

comment on table PmtScheduleTermsRef is
'Виды периодичности платежей\сроков погашения';

--drop table PmtScheduleAlgsRef;

/*==============================================================*/
/* Table: PmtScheduleAlgsRef                                    */
/*==============================================================*/
create table PmtScheduleAlgsRef (
   schedule_alg_id      TIdCode              not null,
   schedule_alg_name    TStr100              not null,
   is_actual            TBoolean             null,
   constraint PK_PMTSCHEDULEALGSREF primary key (schedule_alg_id)
);

comment on table PmtScheduleAlgsRef is
'Алгоритм построения графика';

--drop index schedule_contracts;

--drop table PmtSchedules;

/*==============================================================*/
/* Table: PmtSchedules                                          */
/*==============================================================*/
create table PmtSchedules (
   schedule_id          TIdCode              not null,
   contract_id          TIdCode              not null,
   entity_kind_id       TIdCode              not null,
   pmt_term_id          TIdCode              not null,
   schedule_alg_id      TIdCode              null,
   from_date            TDate                null,
   last_date            TDate                null,
   constraint PK_PMTSCHEDULES primary key (schedule_id)
);

comment on table PmtSchedules is
'График обязательств по договору';

/*==============================================================*/
/* Index: schedule_contracts                                    */
/*==============================================================*/
create unique index schedule_contracts on PmtSchedules (
schedule_id,
contract_id,
entity_kind_id
);

alter table PmtSchedules
   add constraint FK_PMTSCHED_CONTRACT_ID foreign key (contract_id)
      references EntityContracts (contract_id)
      on delete restrict on update restrict;

alter table PmtSchedules
   add constraint FK_PMTSCHED_ENTITY_KIND_ID foreign key (entity_kind_id)
      references EntityKindsRef (entity_kind_id)
      on delete restrict on update restrict;

alter table PmtSchedules
   add constraint FK_PMTSCHED_PMT_TERM_ID foreign key (pmt_term_id)
      references PmtScheduleTermsRef (pmt_term_id)
      on delete restrict on update restrict;

alter table PmtSchedules
   add constraint FK_PMTSCHED_SCHEDULE_ALG_ID foreign key (schedule_alg_id)
      references PmtScheduleAlgsRef (schedule_alg_id)
      on delete restrict on update restrict;

alter table PmtSchedules
   add constraint FK_PMTSCHED_SCHEDULE__ENTITIES foreign key (schedule_id)
      references Entities (entity_id)
      on delete restrict on update restrict;


--drop table PmtScheduleLines;

/*==============================================================*/
/* Table: PmtScheduleLines                                      */
/*==============================================================*/
create table PmtScheduleLines (
   schedule_id          TIdCode              not null,
   actual_date          TDate                not null,
   from_date            TDate                not null,
   to_date              TDate                not null,
   appear_date          TDate                null,
   pay_sum              TMoney               null,
   calc_date            TDate                null,
   constraint PK_PMTSCHEDULELINES primary key (schedule_id, actual_date, from_date, to_date)
);

comment on table PmtScheduleLines is
'Строки графика обязательств по договору';

alter table PmtScheduleLines
   add constraint FK_PMTSCHED_SCHEDULES_PMTSCHED foreign key (schedule_id)
      references PmtSchedules (schedule_id)
      on delete restrict on update restrict;
