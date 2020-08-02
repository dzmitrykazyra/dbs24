ALTER TABLE core_Counterparties
 ALTER COLUMN counterparty_id SET DATA TYPE TIdBigCode;

ALTER TABLE core_EntityContracts
 ALTER COLUMN counterparty_id SET DATA TYPE TIdBigCode;

ALTER TABLE liasdebts
 ALTER COLUMN counterparty_id SET DATA TYPE TIdBigCode;

