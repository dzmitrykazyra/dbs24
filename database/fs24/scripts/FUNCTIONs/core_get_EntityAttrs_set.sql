--DROP FUNCTION core_get_EntityAttrs_set;
CREATE OR REPLACE FUNCTION core_get_EntityAttrs_set(p_attr_template_id tidcode)
 RETURNS refcursor
 LANGUAGE plpgsql
 VOLATILE 
AS $function$ declare default_cursor refcursor;

begin open default_cursor for 
SELECT ear.attr_code, ear.attr_id
  FROM core_EntAttrTemplatesRef eatr, core_EntAttrsRef ear
  WHERE eatr.attr_template_id=p_attr_template_id
    AND eatr.attr_id=ear.attr_id;

return(default_cursor);
end;

$function$ 