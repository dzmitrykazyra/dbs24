ALTER TABLE referencesVersions
ADD COLUMN ref_hashcode TIdCode;

ALTER TABLE referencesVersions
DROP COLUMN ref_version;
