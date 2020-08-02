ALTER TABLE LoanContracts
ADD COLUMN pmt_term_id TIdCode;

UPDATE LoanContracts 
  SET pmt_term_id = 0;

ALTER TABLE LoanContracts
 ALTER COLUMN pmt_term_id SET NOT NULL;

alter table LoanContracts
   add constraint FK_LoanContract_pmt_term_id foreign key (pmt_term_id)
      references PmtScheduleTermsRef (pmt_term_id)
      on delete restrict on update restrict;


ALTER TABLE LoanContracts
ADD COLUMN schedule_alg_id TIdCode;

UPDATE LoanContracts 
  SET schedule_alg_id = 0;

ALTER TABLE LoanContracts
 ALTER COLUMN schedule_alg_id SET NOT NULL;

alter table LoanContracts
   add constraint FK_LoanContracts_schedule_alg_id foreign key (schedule_alg_id)
      references PmtScheduleAlgsRef (schedule_alg_id)
      on delete restrict on update restrict;