
CREATE OR REPLACE FUNCTION core_insert_log_note(p_entity_id TIdBigCode, p_notes ttext)
 RETURNS void
 LANGUAGE plpgsql
AS $function$

begin 

INSERT INTO core_entitylog (entity_id, server_date, notes) 
	VALUES (p_entity_id, current_timestamp, p_notes);
end;

$function$
