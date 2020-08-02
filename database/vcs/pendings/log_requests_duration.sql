ALTER TABLE log_requests
ADD COLUMN duration TIntCounter;

update log_requests
  SET duration = 0;

ALTER TABLE log_requests
ALTER COLUMN duration SET NOT NULL;