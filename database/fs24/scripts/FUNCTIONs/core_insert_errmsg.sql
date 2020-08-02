DROP FUNCTION inserterrmsg;
CREATE OR REPLACE FUNCTION core_insert_errmsg(p_action_id TIdBigCode, p_err_msg ttext)
 RETURNS void
 LANGUAGE plpgsql
AS $function$

begin 

UPDATE core_Actions
              SET err_msg = p_err_msg
              WHERE action_id= p_action_id;
end;

$function$
