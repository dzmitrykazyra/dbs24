CREATE OR REPLACE FUNCTION core_insertorupdate_functionsgroup(p_function_group_id tidcode, p_function_group_code tstr30, p_function_group_name tstr100)
 RETURNS void
 LANGUAGE plpgsql
AS $function$

begin 
    
INSERT INTO core_functionsgroupsref (function_group_id, function_group_code, function_group_name) 
	VALUES (p_function_group_id, p_function_group_code, p_function_group_name)
      ON CONFLICT (function_group_id) DO
    UPDATE
      SET function_group_code = EXCLUDED.function_group_code,
        function_group_name = EXCLUDED.function_group_name;

end

$function$
