
/*==============================================================*/
/* Table: wa_agent_os_types_ref                                 */
/*==============================================================*/
create table wa_agent_os_types_ref (
   agent_os_type_id     TidByteCode          not null,
   agent_os_type_name   TStr100              not null,
   constraint PK_WA_AGENT_OS_TYPES_REF primary key (agent_os_type_id)
);

comment on table wa_agent_os_types_ref is
'Статусы  агентов';

insert into wa_agent_os_types_ref(agent_os_type_id, agent_os_type_name) values (1, 'Basic');
insert into wa_agent_os_types_ref(agent_os_type_id, agent_os_type_name) values (2, 'Business');


alter table wa_agents  add column agent_os_type_id  TIdCode;
alter table wa_agents_hist  add column agent_os_type_id  TIdCode;

update wa_agents set agent_os_type_id = 1;
update wa_agents_hist set agent_os_type_id = 1;


alter table wa_agents
   add constraint FK_wa_agents_os_type_id foreign key (agent_os_type_id)
      references wa_agent_os_types_ref (agent_os_type_id)
      on delete restrict on update restrict;

-- not null
ALTER TABLE wa_agents ALTER COLUMN agent_os_type_id SET NOT NULL;
ALTER TABLE wa_agents_hist ALTER COLUMN agent_os_type_id SET NOT NULL;

commit;