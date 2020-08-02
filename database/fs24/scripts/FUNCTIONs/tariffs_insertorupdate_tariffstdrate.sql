CREATE OR REPLACE FUNCTION tariffs_insertorupdate_tariffstdrate(p_tariff_std_rate_id tidcode, p_tariff_std_group_id tidcode, p_tariff_std_rate_name tstr100)
 RETURNS void
 LANGUAGE plpgsql
AS $function$

begin 
    
INSERT INTO tariffstdratesref (tariff_std_rate_id, tariff_std_rate_name, tariff_std_group_id) 
	VALUES (p_tariff_std_rate_id, p_tariff_std_rate_name, p_tariff_std_group_id)
      ON CONFLICT (tariff_std_rate_id) DO
    UPDATE
      SET tariff_std_group_id = EXCLUDED.tariff_std_group_id,
         tariff_std_rate_name = EXCLUDED.tariff_std_rate_name;

end

$function$
