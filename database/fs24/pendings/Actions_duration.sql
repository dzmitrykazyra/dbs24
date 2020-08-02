ALTER TABLE Actions
ADD COLUMN action_duration TTime;

UPDATE Actions 
  SET action_duration = time '00:00';

ALTER TABLE Actions
 ALTER COLUMN action_duration SET NOT NULL;


ALTER TABLE EntityTests
 DROP COLUMN test_duration;