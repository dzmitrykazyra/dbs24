DROP FUNCTION ent_reset_appfieldscaptions;
CREATE OR REPLACE FUNCTION core_reset_appfieldscaptions(p_user_id TIdBigCode, p_app_id tidcode)
 RETURNS void
 LANGUAGE plpgsql
AS $function$

begin 

DELETE FROM core_appfieldscaptions 
  WHERE user_id = p_user_id AND app_id = p_app_id; 
end;

$function$
