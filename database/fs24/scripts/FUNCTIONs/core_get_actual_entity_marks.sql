DROP FUNCTION ent_get_actual_entity_marks;
CREATE OR REPLACE FUNCTION core_get_actual_entity_marks(p_entity_id TIdBigCode)
 RETURNS refcursor
 LANGUAGE plpgsql
 VOLATILE 
AS $function$ declare default_cursor refcursor;

begin open default_cursor for select
	m.mark_id,
	m.mark_value_id
from
	(
		select
			em.mark_id,
			em.mark_value_id,
			row_number() over(
				partition by mark_id
			order by
				action_id desc
			) rn
		from
			core_entityMarks em
		where
			em.entity_id = p_entity_id
	) m
where
	rn = 1;

return(default_cursor);
end;

$function$
