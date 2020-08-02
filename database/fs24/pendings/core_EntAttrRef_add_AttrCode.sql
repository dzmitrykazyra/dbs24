ALTER TABLE core_EntAttrsRef
ADD COLUMN attr_code TStr100;

Update core_EntAttrsRef
  set attr_code = 'n\d';

ALTER TABLE core_EntAttrsRef
ALTER COLUMN attr_code SET NOT NULL;

commit;
