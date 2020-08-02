-- FUNCTION: ee_dev.tariff_insertorupdate_tariffplan(tidbigcode, tidcode, tstr100, tstr100, tdate, tdate)

DROP FUNCTION tariff_insertorupdate_tariff_rates_1;

CREATE OR REPLACE FUNCTION tariff_insertorupdate_tariff_rates_1(
	p_rate_id tidcode,
	p_rate_date tdate,
	p_currency_id tidcode,
	p_rate_value tpercrate)
    RETURNS void
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE 
AS $BODY$

begin 
    
INSERT INTO tariffrates_1 (rate_id, rate_date, currency_id, rate_value) 
	VALUES (p_rate_id, p_rate_date, p_currency_id, p_rate_value)
      ON CONFLICT (rate_id, rate_date, currency_id) DO
    UPDATE
      SET rate_value = EXCLUDED.rate_value;

end

$BODY$;

