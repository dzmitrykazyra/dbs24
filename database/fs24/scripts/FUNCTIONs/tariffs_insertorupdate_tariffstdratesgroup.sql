CREATE OR REPLACE FUNCTION tariffs_insertorupdate_tariffstdratesgroup(p_tariff_std_group_id tidcode, p_tariff_std_group_name tstr100)
 RETURNS void
 LANGUAGE plpgsql
AS $function$

begin 
    
INSERT INTO tariffstdratesgroupsref (tariff_std_group_id, tariff_std_group_name) 
	VALUES (p_tariff_std_group_id, p_tariff_std_group_name)
      ON CONFLICT (tariff_std_group_id) DO
    UPDATE
      SET tariff_std_group_name = EXCLUDED.tariff_std_group_name;

end

$function$
