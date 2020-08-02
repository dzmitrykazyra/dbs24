ALTER TABLE ActionCodesRef
ADD COLUMN is_closed TBoolean;


update ActionCodesRef
  SET is_closed = false;


ALTER TABLE ActionCodesRef
ALTER COLUMN is_closed SET NOT NULL;