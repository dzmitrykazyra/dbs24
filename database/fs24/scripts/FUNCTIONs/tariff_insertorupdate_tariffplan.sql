-- FUNCTION: ee_dev.tariff_insertorupdate_tariffplan(tidbigcode, tidcode, tstr100, tstr100, tdate, tdate)

DROP FUNCTION tariff_insertorupdate_tariffplan;

CREATE OR REPLACE FUNCTION tariff_insertorupdate_tariffplan(
	p_tariff_plan_id tidbigcode,
	p_tariff_plan_kind_id tidcode,
	p_tariff_plan_code tstr100,
	p_tariff_plan_name tstr100,
	p_actual_date tdate,
	p_finish_date tdate)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

begin 
    
INSERT INTO tariffplans (tariff_plan_id, tariff_plan_kind_id, tariff_plan_code, tariff_plan_name, actual_date, finish_date) 
	VALUES (p_tariff_plan_id, p_tariff_plan_kind_id, p_tariff_plan_code, p_tariff_plan_name, p_actual_date, p_finish_date)
      ON CONFLICT (tariff_plan_id) DO
    UPDATE
      SET tariff_plan_kind_id = EXCLUDED.tariff_plan_kind_id,
        tariff_plan_code = EXCLUDED.tariff_plan_code,
        tariff_plan_name = EXCLUDED.tariff_plan_name,
        actual_date = EXCLUDED.actual_date,
        finish_date = EXCLUDED.finish_date;

end

$BODY$;

