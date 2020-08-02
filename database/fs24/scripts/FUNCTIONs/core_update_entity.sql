DROP FUNCTION ent_update_entity;
CREATE OR REPLACE FUNCTION core_update_entity(p_entity_id tidbigcode)
 RETURNS void
 LANGUAGE plpgsql
AS $function$ begin update
		core_entities
	set
		Last_Modify = current_timestamp
	where
		Entity_ID = p_entity_id;
end $function$
