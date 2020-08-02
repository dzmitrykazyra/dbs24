DROP FUNCTION ent_get_entity_history;
CREATE OR REPLACE FUNCTION core_get_entity_history(p_entity_id TIdBigCode, p_datefrom tdate, p_dateto tdate)
 RETURNS refcursor
 LANGUAGE plpgsql
AS $function$

 declare default_cursor refcursor;

begin open default_cursor for select
	a.*,
	ac.action_name
from
	core_actions a,
	core_actionCodesRef ac
where
	a.entity_id = p_entity_id
	and a.action_code = ac.action_code
        and a.execute_date BETWEEN p_datefrom AND p_dateto
order by
	action_id desc;

return(default_cursor);
end;

$function$
