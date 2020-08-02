drop table DocAttrs;
drop table DocTemplateAttrsRef;
drop table DocAttrsRef;


/*==============================================================*/
/* Table: DocAttrsRef                                           */
/*==============================================================*/
create table DocAttrsRef (
   doc_attr_id          TIdCode              not null,
   doc_attr_code        TStr30               not null,
   doc_attr_name        TStr128              not null,
   constraint PK_DOCATTRSREF primary key (doc_attr_id),
   constraint AK_DOCATTRSREF unique (doc_attr_code)
);

comment on table DocAttrsRef is
'Справочник атрибутов документов';

comment on table DocAttrsRef is
'Справочник атрибутов документов';

/*==============================================================*/
/* Table: DocTemplateAttrsRef                                   */
/*==============================================================*/
create table DocTemplateAttrsRef (
   doc_template_id      TIdCode              not null,
   doc_attr_id          TIdCode              not null,
   is_mandatory         TBoolean             not null,
   constraint PK_DOCTEMPLATEATTRSREF primary key (doc_template_id, doc_attr_id)
);

comment on table DocTemplateAttrsRef is
'Справочник шаблонов плажетных документов';

alter table DocTemplateAttrsRef
   add constraint FK_DTAR_doc_attr_code foreign key (doc_attr_id)
      references DocAttrsRef (doc_attr_id)
      on delete restrict on update restrict;

alter table DocTemplateAttrsRef
   add constraint FK_DTAR_doc_template_id foreign key (doc_template_id)
      references DocTemplatesRef (doc_template_id)
      on delete restrict on update restrict;

/*==============================================================*/
/* Table: DocAttrs                                              */
/*==============================================================*/
create table DocAttrs (
   doc_id               TIdBigCode           not null,
   doc_attr_id          TIdCode              not null,
   doc_attr_value       TStr2000             not null,
   constraint PK_DOCATTRS primary key (doc_id, doc_attr_id)
);

comment on table DocAttrs is
'Атрибуты платежного документа';

alter table DocAttrs
   add constraint FK_Doc2attrs_doc_attr_code foreign key (doc_attr_id)
      references DocAttrsRef (doc_attr_id)
      on delete restrict on update restrict;

alter table DocAttrs
   add constraint FK_Doc2attrs_doc_id foreign key (doc_id)
      references Documents (doc_id)
      on delete restrict on update restrict;
