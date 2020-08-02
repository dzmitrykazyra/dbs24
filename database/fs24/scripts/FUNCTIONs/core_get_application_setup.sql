--DROP FUNCTION core_get_application_setup;
CREATE OR REPLACE FUNCTION core_get_application_setup()
 RETURNS refcursor
 LANGUAGE plpgsql
AS $function$ declare default_cursor refcursor;

begin open default_cursor for 
SELECT apps.* FROM core_ApplicationSetup apps;

return(default_cursor);
end;

$function$ 