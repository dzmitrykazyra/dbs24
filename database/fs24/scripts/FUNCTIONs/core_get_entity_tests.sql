DROP FUNCTION ent_get_entity_tests;
CREATE OR REPLACE FUNCTION core_get_entity_tests(p_entity_id TIdBigCode, p_execute_date1 tdate, p_execute_date2 tdate)
 RETURNS refcursor
 LANGUAGE plpgsql
AS $function$

 declare default_cursor refcursor;

begin open default_cursor for select
	a.*,
		t.log_body,
	ac.action_name,
        ac.app_name
from
	core_actions a,
        core_entityTests t,
	core_actionCodesRef ac
where
	a.entity_id = p_entity_id
        and a.action_id = t.action_id
	and a.action_code = ac.action_code
        and a.execute_date BETWEEN p_execute_date1 and p_execute_date2 
order by
	a.action_id desc;

return(default_cursor);
end;

$function$
