create domain TIdBigCode as BIGINT;

ALTER TABLE entities
 ALTER COLUMN entity_id SET DATA TYPE TIdBigCode;

ALTER TABLE actions
 ALTER COLUMN action_id SET DATA TYPE TIdBigCode;

ALTER TABLE actions
 ALTER COLUMN entity_id SET DATA TYPE TIdBigCode;

ALTER TABLE actions
 ALTER COLUMN user_id SET DATA TYPE TIdBigCode;

ALTER TABLE entitycontracts
 ALTER COLUMN contract_id SET DATA TYPE TIdBigCode;

ALTER TABLE loancontracts
 ALTER COLUMN contract_id SET DATA TYPE TIdBigCode;

ALTER TABLE action2role
 ALTER COLUMN role_id SET DATA TYPE TIdBigCode;

ALTER TABLE appfieldscaptions
 ALTER COLUMN user_id SET DATA TYPE TIdBigCode;

ALTER TABLE application2role
 ALTER COLUMN role_id SET DATA TYPE TIdBigCode;

ALTER TABLE entitycontract2tariffplans
 ALTER COLUMN contract_id SET DATA TYPE TIdBigCode;

ALTER TABLE entitymarks
 ALTER COLUMN entity_id SET DATA TYPE TIdBigCode;

ALTER TABLE entitymarks
 ALTER COLUMN action_id SET DATA TYPE TIdBigCode;

ALTER TABLE entitytests
 ALTER COLUMN action_id SET DATA TYPE TIdBigCode;

ALTER TABLE function2role
 ALTER COLUMN role_id SET DATA TYPE TIdBigCode;

ALTER TABLE liasactions
 ALTER COLUMN lias_action_id SET DATA TYPE TIdBigCode;

ALTER TABLE pmtschedules
 ALTER COLUMN contract_id SET DATA TYPE TIdBigCode;

ALTER TABLE roles
 ALTER COLUMN role_id SET DATA TYPE TIdBigCode;

ALTER TABLE user2role
 ALTER COLUMN role_id SET DATA TYPE TIdBigCode;

ALTER TABLE user2role
 ALTER COLUMN user_id SET DATA TYPE TIdBigCode;

ALTER TABLE users
 ALTER COLUMN user_id SET DATA TYPE TIdBigCode;

ALTER TABLE pmtschedules
 ALTER COLUMN schedule_id SET DATA TYPE TIdBigCode;

ALTER TABLE pmtschedulelines
 ALTER COLUMN schedule_id SET DATA TYPE TIdBigCode;
