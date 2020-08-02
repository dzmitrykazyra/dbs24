DROP FUNCTION ent_insertorupdate_entitykindsref;
CREATE OR REPLACE FUNCTION core_insertorupdate_entitykindsref(p_entity_type_id tidcode, p_entity_kind_id tidcode, p_entity_kind_name tstr100)
 RETURNS void
 LANGUAGE plpgsql
AS $function$

begin 
    
    INSERT INTO core_entityKindsRef (entity_type_id, entity_kind_id, entity_kind_name) 
      VALUES (p_entity_type_id, p_entity_kind_id, p_entity_kind_name)
      ON CONFLICT (entity_kind_id) DO
    UPDATE
      SET entity_type_id = EXCLUDED.entity_type_id,
        entity_kind_name = EXCLUDED.entity_kind_name;

end

$function$
