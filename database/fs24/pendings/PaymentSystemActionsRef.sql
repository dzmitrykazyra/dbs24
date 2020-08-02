drop table PaymentSystemActionsRef;

/*==============================================================*/
/* Table: PaymentSystemActionsRef                               */
/*==============================================================*/
create table PaymentSystemActionsRef (
   pmt_sys_id           TIdCode              not null,
   action_code          TIdCode              not null,
   actual_date          TDate                not null,
   close_date           TDate                null,
   constraint PK_PAYMENTSYSTEMACTIONSREF primary key (pmt_sys_id, action_code)
);

comment on table PaymentSystemActionsRef is
'Связь действия с платежной системой';

alter table PaymentSystemActionsRef
   add constraint FK_PSA_actionCode foreign key (action_code)
      references ActionCodesRef (action_code)
      on delete restrict on update restrict;

alter table PaymentSystemActionsRef
   add constraint FK_PSA_pmt_sys_id foreign key (pmt_sys_id)
      references PaymentSystemsRef (pmt_sys_id)
      on delete restrict on update restrict;
