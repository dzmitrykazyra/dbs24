DROP FUNCTION core_get_EntityAttr_list;
CREATE OR REPLACE FUNCTION core_get_EntityAttr_list(p_entity_id tidbigcode)
 RETURNS refcursor
 LANGUAGE plpgsql
 VOLATILE 
AS $function$ declare default_cursor refcursor;

begin open default_cursor for 
SELECT ea.*, ar.attr_code 
  FROM core_EntityAttrs ea, core_EntAttrsRef ar
  WHERE ea.entity_id=p_entity_id
    AND ea.attr_id=ar.attr_id;

return(default_cursor);
end;

$function$ 