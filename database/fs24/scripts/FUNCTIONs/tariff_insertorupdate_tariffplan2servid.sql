-- FUNCTION: ee_dev.tariff_insertorupdate_tariffplan2servid(tidbigcode, tidcode, tidcode, tdate, tdate)

-- DROP FUNCTION ee_dev.tariff_insertorupdate_tariffplan2servid(tidbigcode, tidcode, tidcode, tdate, tdate);

CREATE OR REPLACE FUNCTION tariff_insertorupdate_tariffplan2servid(
	p_tariff_plan_id tidbigcode,
	p_tariff_serv_id tidcode,
	p_tariff_kind_id tidcode,
	p_actual_date tdate,
	p_close_date tdate)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

begin 
    
INSERT INTO tariffplan2servid (tariff_plan_id, tariff_serv_id, tariff_kind_id, actual_date, close_date) 
	VALUES (p_tariff_plan_id, p_tariff_serv_id, p_tariff_kind_id, p_actual_date, p_close_date)
      ON CONFLICT (tariff_plan_id, tariff_serv_id) DO
    UPDATE
      SET tariff_kind_id = EXCLUDED.tariff_kind_id,
        actual_date = EXCLUDED.actual_date,
        close_date = EXCLUDED.close_date;

end

$BODY$;
