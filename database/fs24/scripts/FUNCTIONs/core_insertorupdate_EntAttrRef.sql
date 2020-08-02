DROP FUNCTION core_insertorupdate_EntAttrRef;
CREATE OR REPLACE FUNCTION core_insertorupdate_EntAttrRef(p_attr_id tidcode, p_attr_code tstr100, p_attr_name tstr200)
 RETURNS void
 LANGUAGE plpgsql
AS $function$

begin 
    
INSERT INTO core_entattrsref (attr_id, attr_code, attr_name) 
	VALUES (p_attr_id, p_attr_code, p_attr_name)
      ON CONFLICT (attr_id) DO
    UPDATE SET 
        attr_code = EXCLUDED.attr_code,
        attr_name = EXCLUDED.attr_name;

end

$function$
