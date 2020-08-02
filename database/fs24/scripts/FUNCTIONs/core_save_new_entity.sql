DROP FUNCTION core_save_new_entity;
CREATE OR REPLACE FUNCTION core_save_new_entity(p_entity_type_id tidcode, p_entity_status_id tidcode, p_close_date tdate)
 RETURNS tidbigcode
 LANGUAGE plpgsql
AS $function$
 declare v_new_entity_id TIdBigCode;

begin 

v_new_entity_id =  nextval('seq_action_id');

INSERT INTO core_entities(entity_id, entity_type_id, entity_status_id, creation_date, close_date, last_modify)
                        VALUES(v_new_entity_id,
                               p_entity_type_id,
                               p_entity_status_id,
                               current_timestamp,
                               p_close_date,
                               current_timestamp);

  return v_new_entity_id;

end

$function$