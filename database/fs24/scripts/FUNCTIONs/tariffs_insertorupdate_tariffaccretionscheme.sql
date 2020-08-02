CREATE OR REPLACE FUNCTION tariffs_insertorupdate_tariffaccretionscheme(p_tariff_scheme_id tidcode, p_tariff_scheme_name tstr100)
 RETURNS void
 LANGUAGE plpgsql
AS $function$

begin 
    
INSERT INTO tariffaccretionschemeref (tariff_scheme_id, tariff_scheme_name) 
	VALUES (p_tariff_scheme_id, p_tariff_scheme_name)
      ON CONFLICT (tariff_scheme_id) DO
    UPDATE
      SET tariff_scheme_name = EXCLUDED.tariff_scheme_name;

end

$function$
