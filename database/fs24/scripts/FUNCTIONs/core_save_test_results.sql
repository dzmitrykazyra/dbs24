DROP FUNCTION ent_save_testresults;
CREATE OR REPLACE FUNCTION core_save_test_results(p_action_id TIdBigCode, p_log_body ttext)
 RETURNS void
 LANGUAGE plpgsql
AS $function$

begin 

INSERT INTO core_entityTests (action_id, log_body) 
	VALUES (p_action_id, p_log_body)
                     ON CONFLICT (action_id) DO UPDATE SET 
                       log_body = EXCLUDED.log_body; 
end;

$function$