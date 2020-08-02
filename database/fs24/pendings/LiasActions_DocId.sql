ALTER TABLE LiasActions
ADD COLUMN doc_id TIdBigCode;


alter table LiasActions
   add constraint FK_LiasActions_doc_id foreign key (doc_id)
      references Documents (doc_id)
      on delete restrict on update restrict;

update LiasActions
  SET doc_id = 999;


ALTER TABLE LiasActions
ALTER COLUMN doc_id SET NOT NULL;
