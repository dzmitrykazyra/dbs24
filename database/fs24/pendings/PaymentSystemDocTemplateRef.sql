drop table PaymentSystemActionsRef;

--drop table PaymentSystemDocTemplateRef;

/*==============================================================*/
/* Table: PaymentSystemDocTemplateRef                           */
/*==============================================================*/
create table PaymentSystemDocTemplateRef (
   pmt_sys_id           TIdCode              not null,
   fin_oper_code        TIdCode              not null,
   doc_template_id      TIdCode              not null,
   actual_date          TDate                not null,
   close_date           TDate                null,
   constraint PK_PAYMENTSYSTEMDOCTEMPLATEREF primary key (pmt_sys_id, fin_oper_code, doc_template_id)
);

comment on table PaymentSystemDocTemplateRef is
'Связь финоперации с шаблоном документа';

alter table PaymentSystemDocTemplateRef
   add constraint FK_PSA_pmt_sys_id foreign key (pmt_sys_id)
      references PaymentSystemsRef (pmt_sys_id)
      on delete restrict on update restrict;

alter table PaymentSystemDocTemplateRef
   add constraint FK_PSDTR_doc_template_id foreign key (doc_template_id)
      references DocTemplatesRef (doc_template_id)
      on delete restrict on update restrict;

alter table PaymentSystemDocTemplateRef
   add constraint FK_PSDTR_fin_oper_code foreign key (fin_oper_code)
      references LiasFinOperCodesRef (fin_oper_code)
      on delete restrict on update restrict;
