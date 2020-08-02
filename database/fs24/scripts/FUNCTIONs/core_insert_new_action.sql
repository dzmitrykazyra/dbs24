DROP FUNCTION core_insert_new_action;
CREATE OR REPLACE FUNCTION core_insert_new_action(p_entity_id tidbigcode, p_action_code tidcode, p_user_id tidbigcode, p_action_address tstr40)
 RETURNS tidbigcode
 LANGUAGE plpgsql
AS $function$
 declare v_new_action_id TIdBigCode;

begin 

  v_new_action_id = nextval('seq_action_id');

  INSERT INTO core_Actions(action_id,entity_id,action_code,user_id,execute_date,action_address,action_duration)
      VALUES (v_new_action_id, p_entity_id,p_action_code,p_user_id,current_timestamp,p_action_address,time '00:00');

  return v_new_action_id;

end

$function$
