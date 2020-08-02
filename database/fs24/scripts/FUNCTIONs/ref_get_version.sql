DROP FUNCTION ref_get_version;
CREATE OR REPLACE FUNCTION ref_get_version(
	p_ref_name tstr100)
    RETURNS tidbigcode
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $function$
  declare v_ref_hc tidBigCode;
  begin

    SELECT r.ref_hashcode into v_ref_hc 
      FROM referencesVersions r 
     WHERE r.ref_name =  p_ref_name;

      if not found or(v_ref_hc is null) then 
	v_ref_hc = 0;
      end if;

    return(v_ref_hc);
  end $function$
