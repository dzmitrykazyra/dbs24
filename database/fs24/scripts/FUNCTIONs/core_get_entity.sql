drop function core_get_entity;
CREATE OR REPLACE FUNCTION core_get_entity(p_entity_id TIdBigCode)
 RETURNS refcursor
 LANGUAGE plpgsql
 VOLATILE 
AS $function$ declare default_cursor refcursor;

begin open default_cursor for 
SELECT e.* 
FROM  core_entities e 
WHERE e.entity_id= p_entity_id; 

return(default_cursor);
end;

$function$
