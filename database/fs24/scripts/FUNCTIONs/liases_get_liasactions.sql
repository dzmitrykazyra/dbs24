CREATE OR REPLACE FUNCTION liases_get_liasactions(p_lias_id tidcode)
 RETURNS refcursor
 LANGUAGE plpgsql
AS $function$ declare default_cursor refcursor;

begin open default_cursor for 
SELECT la.* 
  FROM LiasActions la 
  WHERE la.lias_id = p_lias_id;

return(default_cursor);
end;

$function$
