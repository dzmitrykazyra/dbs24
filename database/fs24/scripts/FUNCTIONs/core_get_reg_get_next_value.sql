DROP FUNCTION core_get_reg_get_next_value;
CREATE OR REPLACE FUNCTION core_get_reg_get_next_value(p_seq_name tstr100)
 RETURNS tidbigcode
 LANGUAGE plpgsql
AS $function$ 
begin
    return nextval(p_seq_name);
end;

$function$
