CREATE OR REPLACE FUNCTION tariffs_insertorupdate_tariffserv(p_tariff_serv_id tidcode, p_tariff_group_id tidcode, p_tariff_serv_name tstr100, p_client_pay tboolean)
 RETURNS void
 LANGUAGE plpgsql
AS $function$

begin 
    
INSERT INTO tariffservsref (tariff_serv_id, tariff_group_id, tariff_serv_name, client_pay) 
	VALUES (p_tariff_serv_id, p_tariff_group_id, p_tariff_serv_name, p_client_pay)
      ON CONFLICT (tariff_serv_id) DO
    UPDATE
      SET tariff_serv_name = EXCLUDED.tariff_serv_name,
        tariff_group_id = EXCLUDED.tariff_group_id,
        client_pay = EXCLUDED.client_pay;

end

$function$
