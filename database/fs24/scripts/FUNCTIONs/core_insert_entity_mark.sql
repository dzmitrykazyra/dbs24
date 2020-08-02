DROP FUNCTION ent_insert_entity_mark;
CREATE OR REPLACE FUNCTION core_insert_entity_mark(p_entity_id TIdBigCode, p_action_id TIdBigCode, p_mark_id tidcode, p_mark_value_id tidcode)
 RETURNS void
 LANGUAGE plpgsql
AS $function$

begin 
    
INSERT INTO core_entitymarks (entity_id, action_id, mark_id, mark_value_id, mark_direction) 
	VALUES (p_entity_id, p_action_id, p_mark_id, p_mark_value_id, true);

end

$function$
