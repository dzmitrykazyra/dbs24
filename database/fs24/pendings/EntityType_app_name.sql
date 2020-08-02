ALTER TABLE EntityTypesRef
ADD COLUMN entity_app_name TStr100;

UPDATE EntityTypesRef 
  SET entity_app_name = 'isEmpty entity_app_name';

ALTER TABLE EntityTypesRef
 ALTER COLUMN entity_app_name SET NOT NULL;