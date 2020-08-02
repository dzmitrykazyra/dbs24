DROP FUNCTION ent_get_entity_marks;
CREATE OR REPLACE FUNCTION core_get_entity_marks(p_entity_id TIdBigCode, p_datefrom tdate, p_dateto tdate)
 RETURNS refcursor
 LANGUAGE plpgsql
AS $function$

 declare default_cursor refcursor;

begin open default_cursor for select
        em.mark_id,
        em.mark_value_id,
        em.mark_direction,
        a.*,
        acr.action_name,
        mr.mark_name,
        mvr.mark_value_name
from
	core_entityMarks em, core_actions a, core_actionCodesRef acr, core_marksRef mr, core_marksValuesRef mvr
where
	em.entity_id = p_entity_id
        and em.action_id = a.action_id
        and a.execute_date BETWEEN p_datefrom AND p_dateto
        and a.action_code = acr.action_code
        and em.mark_id = mr.mark_id
        and em.mark_id = mvr.mark_id
        and em.mark_value_id = mvr.mark_value_id
order by
	action_id desc;

return(default_cursor);
end;

$function$
