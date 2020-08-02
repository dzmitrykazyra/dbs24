DROP FUNCTION user_get_user;
CREATE OR REPLACE FUNCTION core_get_user(p_login tstr50)
 RETURNS refcursor
 LANGUAGE plpgsql
AS $function$ declare default_cursor refcursor;

begin open default_cursor for 
SELECT u.*, e.entity_status_id 
FROM core_users u, core_entities e 
WHERE u.login=p_login and u.user_id=e.entity_id;

return(default_cursor);
end;

$function$
