--drop table core_EntityLog;

/*==============================================================*/
/* Table: core_EntityLog                                        */
/*==============================================================*/
create table core_EntityLog (
   entity_id            TIdBigCode           not null,
   server_date          TDateTime            not null,
   notes                TText                not null
);

comment on table core_EntityLog is
'Лог действий над сущностью';

alter table core_EntityLog
   add constraint FK_EntityLog_entity_id foreign key (entity_id)
      references core_Entities (entity_id)
      on delete restrict on update restrict;
