DROP FUNCTION liases_get_debts;
CREATE OR REPLACE FUNCTION liases_get_debts(p_entity_id TIdBigCode)
 RETURNS refcursor
 LANGUAGE plpgsql
AS $function$ declare default_cursor refcursor;

begin open default_cursor for 
SELECT ld.* 
FROM  LiasDebts ld 
WHERE ld.contract_id= p_entity_id; 

return(default_cursor);
end;

$function$
