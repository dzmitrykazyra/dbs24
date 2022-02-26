create sequence seq_wa_Agent_messages
increment 1
minvalue 1
maxvalue 9223372036854775807
start 1;


/*==============================================================*/
/* Table: wa_agent_messages                                     */
/*==============================================================*/
create table wa_agent_messages (
                                   message_id           TIdCode              not null,
                                   actual_date          TDateTime            not null,
                                   create_date          TDateTime            not null,
                                   agent_id             TIdCode              not null,
                                   phone_num            TStr20               not null,
                                   is_tracking_allowed  TBoolean             not null,
                                   message_note         TStr2000             null,
                                   constraint PK_WA_AGENT_MESSAGES primary key (message_id),
                                   constraint AK_KEY_WA_AGENT_MESSAGES unique (agent_id, phone_num, create_date)
);

comment on table wa_agent_messages is
'Отметки об сообщениях';

alter table wa_agent_messages
    add constraint FK_wa_am_agent_id foreign key (agent_id)
        references wa_agents (agent_id)
        on delete restrict on update restrict;




/*==============================================================*/
/* Table: wa_agent_messages_hist                                */
/*==============================================================*/
create table wa_agent_messages_hist (
                                        message_id           TIdCode              null,
                                        create_date          TDateTime            null,
                                        actual_date          TDateTime            null,
                                        agent_id             TIdCode              null,
                                        phone_num            TStr20               null,
                                        is_tracking_allowed  TBoolean             null,
                                        message_note         TStr2000             null
);

comment on table wa_agent_messages_hist is
'Отметки об сообщениях';

alter table wa_agent_messages_hist
    add constraint FK_wa_agent_msg_id foreign key (message_id)
        references wa_agent_messages (message_id)
        on delete restrict on update restrict;