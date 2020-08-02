CREATE OR REPLACE FUNCTION tariffs_insertorupdate_tariffkind(p_tariff_kind_id tidcode, p_tariff_serv_id tidcode, p_tariff_kind_name tstr100)
 RETURNS void
 LANGUAGE plpgsql
AS $function$

begin 
    
INSERT INTO tariffkindsref (tariff_kind_id, tariff_serv_id, tariff_kind_name) 
	VALUES (p_tariff_kind_id, p_tariff_serv_id, p_tariff_kind_name)
      ON CONFLICT (tariff_kind_id) DO
    UPDATE
      SET tariff_serv_id = EXCLUDED.tariff_serv_id,
        tariff_kind_name = EXCLUDED.tariff_kind_name;

end

$function$
