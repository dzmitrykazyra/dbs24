drop FUNCTION ent_insertorupdate_entitytypesref;

CREATE OR REPLACE FUNCTION core_insertorupdate_entitytypesref(
	p_entity_type_id tidcode,
	p_entity_type_name tstr100,
	p_entity_app_name tstr100)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

begin 
    
    INSERT INTO core_entitytypesref (entity_type_id, entity_type_name, entity_app_name) 
      VALUES (p_entity_type_id, p_entity_type_name, p_entity_app_name)
      ON CONFLICT (entity_type_id) DO
    UPDATE
      SET entity_type_name = EXCLUDED.entity_type_name,
        entity_app_name = EXCLUDED.entity_app_name;

end

$BODY$;