CREATE OR REPLACE FUNCTION core_insertorupdate_Function(p_function_id tidcode, p_function_group_id tidcode, p_function_code tstr30, p_function_name tstr100)
 RETURNS void
 LANGUAGE plpgsql
AS $function$

begin 
    
INSERT INTO core_functionsref (function_id, function_group_id, function_code, function_name) 
	VALUES (p_function_id, p_function_group_id, p_function_code, p_function_name)
      ON CONFLICT (function_id) DO
    UPDATE
      SET function_code = EXCLUDED.function_code,
        function_group_id = EXCLUDED.function_group_id,
        function_name = EXCLUDED.function_name;

end

$function$
