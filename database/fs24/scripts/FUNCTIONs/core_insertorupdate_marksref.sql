-- FUNCTION: ee_dev.ent_insertorupdate_marksref(tidcode, tstr200, tstr100)

DROP FUNCTION ent_insertorupdate_marksref;

CREATE OR REPLACE FUNCTION core_insertorupdate_marksref(
	p_mark_id tidcode,
	p_mark_name tstr200,
	p_mark_group tstr100)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

begin 
    
    INSERT INTO core_marksRef (mark_id, mark_name, mark_group) 
      VALUES (p_mark_id, p_mark_name, p_mark_group)
      ON CONFLICT (mark_id) DO
    UPDATE
      SET mark_name = EXCLUDED.mark_name,
        mark_group = EXCLUDED.mark_group;

end

$BODY$;

