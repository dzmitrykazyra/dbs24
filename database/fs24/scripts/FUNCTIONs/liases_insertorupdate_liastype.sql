CREATE OR REPLACE FUNCTION liases_insertorupdate_liastype(
	p_lias_type_id tidcode,
	p_lias_type_name tstr100)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

begin 
    
INSERT INTO liastypesref (lias_type_id, lias_type_name) 
	VALUES (p_lias_type_id, p_lias_type_name)
      ON CONFLICT (lias_type_id) DO
    UPDATE
      SET lias_type_name = EXCLUDED.lias_type_name;

end

$BODY$;