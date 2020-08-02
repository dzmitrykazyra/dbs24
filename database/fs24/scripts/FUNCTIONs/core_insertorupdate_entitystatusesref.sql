DROP FUNCTION ent_insertorupdate_entitystatusesref;
CREATE OR REPLACE FUNCTION core_insertorupdate_entitystatusesref(
	p_entity_type_id tidcode,
	p_entity_status_id tidcode,
	p_entity_status_name tstr100)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

begin 
    
    INSERT INTO core_entitystatusesref (entity_status_id, entity_type_id, entity_status_name)  
      VALUES (p_entity_status_id, p_entity_type_id, p_entity_status_name)
      ON CONFLICT (entity_type_id, entity_status_id) DO
    UPDATE
      SET  entity_status_name = EXCLUDED.entity_status_name;

end

$BODY$;