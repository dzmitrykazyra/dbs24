
/*==============================================================*/
/* Table: DocTemplateGroupsRef                                  */
/*==============================================================*/
drop table DocTemplateGroupsRef;

create table DocTemplateGroupsRef (
   doc_template_group_id TIdCode              not null,
   doc_template_group_name TStr100              not null,
   constraint PK_DOCTEMPLATEGROUPSREF primary key (doc_template_group_id)
);

comment on table DocTemplateGroupsRef is
'Справочник групп шаблонов';

drop table Doc2attrs;
drop table Documents;
drop table DocTemplateAttrsRef;
drop table DocTemplateRef;

/*==============================================================*/
/* Table: DocTemplateRef                                        */
/*==============================================================*/
create table DocTemplateRef (
   doc_template_id      TIdCode              not null,
   doc_template_group_id TIdCode              not null,
   doc_template_code    TStr30               not null,
   doc_template_name    TStr100              not null,
   pmt_sys_id           TIdCode              not null,
   doc_type_id          TIdCode              not null,
   constraint PK_DOCTEMPLATEREF primary key (doc_template_id),
   constraint AK_KEY_2_DOCTEMPL unique (doc_template_code)
);

comment on table DocTemplateRef is
'Справочник шаблонов плажетных документов';

alter table DocTemplateRef
   add constraint FK_DTAR_pmt_sys_id foreign key (pmt_sys_id)
      references PaymentSystemsRef (pmt_sys_id)
      on delete restrict on update restrict;

alter table DocTemplateRef
   add constraint FK_DTR_doc_template_group_id foreign key (doc_template_group_id)
      references DocTemplateGroupsRef (doc_template_group_id)
      on delete restrict on update restrict;

alter table DocTemplateRef
   add constraint FK_DTR_doc_type_id foreign key (doc_type_id)
      references DocTypesRef (doc_type_id)
      on delete restrict on update restrict;


create table DocTemplateAttrsRef (
   doc_template_id      TIdCode              not null,
   doc_attr_code        TStr30               not null,
   is_mandatory         TBoolean             not null,
   constraint PK_DOCTEMPLATEATTRSREF primary key (doc_template_id, doc_attr_code)
);

comment on table DocTemplateAttrsRef is
'Справочник шаблонов плажетных документов';

alter table DocTemplateAttrsRef
   add constraint FK_DTAR_doc_attr_code foreign key (doc_attr_code)
      references DocAttrsRef (doc_attr_code)
      on delete restrict on update restrict;

alter table DocTemplateAttrsRef
   add constraint FK_DTAR_doc_template_id foreign key (doc_template_id)
      references DocTemplateRef (doc_template_id)
      on delete restrict on update restrict;

/*==============================================================*/
/* Table: Documents                                             */
/*==============================================================*/
create table Documents (
   doc_id               TIdBigCode           not null,
   parent_doc_id        TIdBigCode           null,
   doc_template_id      TIdCode              not null,
   doc_status_id        TIdCode              not null,
   entity_id            TIdBigCode           null,
   doc_date             TDate                not null,
   doc_server_date      TDateTime            not null,
   doc_close_date       TDate                null,
   user_id              TIdBigCode           not null,
   constraint PK_DOCUMENTS primary key (doc_id)
);

comment on table Documents is
'Платежные документы';

alter table Documents
   add constraint FK_Document_user_id foreign key (user_id)
      references Users (user_id)
      on delete restrict on update restrict;

alter table Documents
   add constraint FK_Documents_doc_status_id foreign key (doc_status_id)
      references DocStatusesRef (doc_status_id)
      on delete restrict on update restrict;

alter table Documents
   add constraint FK_Documents_doc_template_id foreign key (doc_template_id)
      references DocTemplateRef (doc_template_id)
      on delete restrict on update restrict;

alter table Documents
   add constraint FK_Documents_entity_id foreign key (entity_id)
      references Entities (entity_id)
      on delete restrict on update restrict;

alter table Documents
   add constraint FK_Documents_parent_doc_id foreign key (parent_doc_id)
      references Documents (doc_id)
      on delete restrict on update restrict;

/*==============================================================*/
/* Table: Doc2attrs                                             */
/*==============================================================*/
create table Doc2attrs (
   doc_id               TIdBigCode           not null,
   doc_attr_code        TStr30               not null,
   doc_attr_value       TStr2000             not null,
   constraint PK_DOC2ATTRS primary key (doc_id, doc_attr_code)
);

comment on table Doc2attrs is
'Атрибуты платежного документа';

alter table Doc2attrs
   add constraint FK_Doc2attrs_doc_attr_code foreign key (doc_attr_code)
      references DocAttrsRef (doc_attr_code)
      on delete restrict on update restrict;

alter table Doc2attrs
   add constraint FK_Doc2attrs_doc_id foreign key (doc_id)
      references Documents (doc_id)
      on delete restrict on update restrict;


