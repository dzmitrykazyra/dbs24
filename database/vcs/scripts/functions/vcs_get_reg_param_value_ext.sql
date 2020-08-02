DROP FUNCTION vcs_get_reg_param_value_ext;
CREATE OR REPLACE FUNCTION vcs_get_reg_param_value_ext(p_param_name tstr100)
 RETURNS ttext
 LANGUAGE plpgsql
AS $function$
 declare v_param_value ttext;

begin 

  SELECT param_value INTO v_param_value
    FROM vcs_registryParams WHERE param_name=p_param_name; 

 
  if not found or(v_param_value is null) then 
     v_param_value = '';
  end if;

  return v_param_value;

end

$function$
