DROP FUNCTION app_get_application_list;
CREATE OR REPLACE FUNCTION core_get_application_list(p_pkg_name tstr100)
 RETURNS refcursor
 LANGUAGE plpgsql
AS $function$ declare default_cursor refcursor;

begin open default_cursor for 
SELECT ar.* FROM core_applicationsref ar WHERE ar.app_url LIKE format('%s/%%', p_pkg_name) ORDER BY app_id;

return(default_cursor);
end;

$function$
