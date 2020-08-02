CREATE OR REPLACE FUNCTION liases_get_lias(p_debt_id tidcode)
 RETURNS refcursor
 LANGUAGE plpgsql
AS $function$ declare default_cursor refcursor;

begin open default_cursor for 
SELECT l.* 
FROM  Liases l 
WHERE l.debt_id = p_debt_id; 

return(default_cursor);
end;

$function$
