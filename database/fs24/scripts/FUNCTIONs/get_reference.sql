CREATE OR REPLACE FUNCTION get_reference(p_table_name tstr50)
 RETURNS refcursor
 LANGUAGE plpgsql
AS $function$ declare default_cursor refcursor;

begin open default_cursor for select
	t.*
from
	format('%s', quote_ident(p_table_name)) t;

return(default_cursor);
end;

$function$
