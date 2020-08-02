DROP FUNCTION ent_update_action_notes;
CREATE OR REPLACE FUNCTION core_update_entity_action_notes(p_action_id TIdBigCode, p_notes ttext)
 RETURNS void
 LANGUAGE plpgsql
AS $function$

begin 

UPDATE core_actions 
  set notes = p_notes
  where action_id = p_action_id; 
end;

$function$
