ALTER TABLE log_requests
ADD COLUMN req_answer TStr128;

UPDATE log_requests
  SET req_answer = 'answer';

ALTER TABLE log_requests
ALTER COLUMN req_answer SET NOT NULL;

