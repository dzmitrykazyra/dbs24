CREATE OR REPLACE FUNCTION core_insertorupdate_EntAttrTemplatesRef(p_attr_template_id tidcode, p_attr_id tidcode)
 RETURNS void
 LANGUAGE plpgsql
AS $function$

begin 
    
INSERT INTO core_EntAttrTemplatesRef (attr_template_id, attr_id) 
	VALUES (p_attr_template_id, p_attr_id)
      ON CONFLICT (attr_template_id, attr_id) DO
    UPDATE
      SET attr_id = EXCLUDED.attr_id;

end

$function$
