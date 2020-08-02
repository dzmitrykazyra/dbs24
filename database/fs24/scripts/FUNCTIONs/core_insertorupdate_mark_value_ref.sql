drop FUNCTION ent_insertorupdate_marksvaluesref;

CREATE OR REPLACE FUNCTION core_insertorupdate_mark_value_ref(
	p_mark_id tidcode,
	p_mark_value_id tidcode,
	p_mark_value_name tstr200)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

begin 
    
    INSERT INTO core_marksValuesRef (mark_id, mark_value_id, mark_value_name) 
      VALUES (p_mark_id, p_mark_value_id, p_mark_value_name)
      ON CONFLICT (mark_id, mark_value_id) DO
    UPDATE
      SET mark_value_name = EXCLUDED.mark_value_name;

end

$BODY$;