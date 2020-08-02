DROP FUNCTION core_save_EntityAttrs;
CREATE OR REPLACE FUNCTION core_save_EntityAttrs(p_entity_id tidbigcode, p_attr_id tidcode, p_bigdecimal_value tmoney, p_localdate_value tdate, p_integer_value tidcode, p_string2000_value tstr2000, p_string200_value tstr200, p_boolean_value tboolean, p_localdatetime_value tdatetime)
 RETURNS void
 LANGUAGE plpgsql
AS $function$

begin 
    
INSERT INTO core_entityattrs (entity_id, attr_id, bigdecimal_value, localdate_value, integer_value, string2000_value, string200_value, boolean_value, localdatetime_value) 
	VALUES (p_entity_id, p_attr_id, p_bigdecimal_value, p_localdate_value, p_integer_value, p_string2000_value, p_string200_value, p_boolean_value, p_localdatetime_value)
      ON CONFLICT (entity_id, attr_id) DO
    UPDATE
      SET bigdecimal_value = EXCLUDED.bigdecimal_value,
        localdate_value = EXCLUDED.localdate_value,
        integer_value = EXCLUDED.integer_value,
        string2000_value = EXCLUDED.string2000_value,
        string200_value = EXCLUDED.string200_value,
        boolean_value = EXCLUDED.boolean_value,
        localdatetime_value = EXCLUDED.localdatetime_value;
end

$function$
