CREATE OR REPLACE FUNCTION liases_insertorupdate_liasgroup(
	p_lias_group_id tidcode,
	p_lias_group_name tstr100)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

begin 
    
INSERT INTO liasgroupsref (lias_group_id, lias_group_name) 
	VALUES (p_lias_group_id, p_lias_group_name)
      ON CONFLICT (lias_group_id) DO
    UPDATE
      SET lias_group_name = EXCLUDED.lias_group_name;

end

$BODY$;