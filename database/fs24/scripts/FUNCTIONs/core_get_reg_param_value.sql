DROP FUNCTION reg_get_param_value;
CREATE OR REPLACE FUNCTION core_get_reg_param_value(p_param_name tstr100)
 RETURNS refcursor
 LANGUAGE plpgsql
AS $function$ declare default_cursor refcursor;

begin open default_cursor for 
SELECT param_value FROM core_registryParams WHERE param_name=p_param_name; 

return(default_cursor);
end;

$function$
