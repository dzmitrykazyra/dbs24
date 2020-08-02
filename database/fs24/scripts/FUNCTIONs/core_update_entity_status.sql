DROP FUNCTION ent_update_entity_status;
CREATE OR REPLACE FUNCTION core_update_entity_status(
	p_entity_id TIdBigCode,
	p_entity_status_id tidcode)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

 

begin 

UPDATE core_entities
SET entity_status_id = p_entity_status_id
WHERE Entity_ID=p_entity_id;
end

$BODY$;