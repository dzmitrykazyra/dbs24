DROP FUNCTION update_ref_version;
CREATE OR REPLACE FUNCTION update_ref_version(
	p_ref_name tstr100,
	p_ref_hashcode tidBigCode)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

begin 

INSERT INTO referencesVersions (ref_name, ref_hashcode, assign_date) 
	VALUES (p_ref_name, p_ref_hashcode, CURRENT_TIMESTAMP)
                     ON CONFLICT (ref_name) DO UPDATE 
SET 
                       ref_hashcode = EXCLUDED.ref_hashcode,
                       assign_date = CURRENT_TIMESTAMP; 
end;

$BODY$;