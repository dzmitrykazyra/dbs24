DROP FUNCTION ent_save_action_duration;
CREATE OR REPLACE FUNCTION core_save_action_duration(p_action_id TIdBigCode, p_action_duration ttime)
 RETURNS void
 LANGUAGE plpgsql
AS $function$

begin 

UPDATE core_actions 
  set action_duration = p_action_duration
  where action_id = p_action_id; 
end;

$function$
