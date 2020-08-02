DROP FUNCTION core_get_EntityTemplateAttr_list;
CREATE OR REPLACE FUNCTION core_get_EntityTemplateAttr_list(p_attr_template_id tidcode)
 RETURNS refcursor
 LANGUAGE plpgsql
 VOLATILE 
AS $function$ declare default_cursor refcursor;

begin open default_cursor for 
SELECT ea.* FROM core_EntityAttrs ea WHERE ea.entity_id=p_entity_id;

return(default_cursor);
end;

$function$ 