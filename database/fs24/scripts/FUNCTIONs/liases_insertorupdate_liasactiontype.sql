CREATE OR REPLACE FUNCTION liases_insertorupdate_liasactiontype(
	p_action_type_id tidcode,
	p_action_type_name tstr100,
	p_change_rest_tag tboolean)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

begin 
    
INSERT INTO liasactiontypesref (action_type_id, change_rest_tag, action_type_name) 
	VALUES (p_action_type_id, p_change_rest_tag, p_action_type_name)
      ON CONFLICT (action_type_id) DO
    UPDATE
      SET change_rest_tag = EXCLUDED.change_rest_tag,
        action_type_name = EXCLUDED.action_type_name;

end

$BODY$;