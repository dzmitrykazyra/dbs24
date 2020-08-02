ALTER TABLE LiasActions
RENAME COLUMN status TO fin_oper_status_id;

/*==============================================================*/
/* Table: LiasFinOperStatusesRef                                */
/*==============================================================*/
create table LiasFinOperStatusesRef (
   fin_oper_status_id   TIdCode              not null,
   fin_oper_status_name TStr80               not null,
   constraint PK_LIASFINOPERSTATUSESREF primary key (fin_oper_status_id)
);

comment on table LiasFinOperStatusesRef is
'Статусы финансовых операций';

insert into LiasFinOperStatusesRef(fin_oper_status_id, fin_oper_status_name)
  values(1, 'Исполнена');

insert into LiasFinOperStatusesRef(fin_oper_status_id, fin_oper_status_name)
  values(-1, 'Аннулирована');

alter table LiasActions
   add constraint FK_LiasActions_status_id foreign key (fin_oper_status_id)
      references LiasFinOperStatusesRef (fin_oper_status_id)
      on delete restrict on update restrict;
