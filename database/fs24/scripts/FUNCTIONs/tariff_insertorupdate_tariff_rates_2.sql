

DROP FUNCTION tariff_insertorupdate_tariff_rates_2;

CREATE OR REPLACE FUNCTION tariff_insertorupdate_tariff_rates_2(
	p_rate_id tidcode,
	p_rate_date tdate,
	p_currency_id tidcode,
	p_rate_value tpercrate,
        p_min_sum tmoney,
        p_max_sum tmoney)
    RETURNS void
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE 
AS $BODY$

begin 
    
INSERT INTO tariffrates_2 (rate_id, rate_date, currency_id, min_sum, max_sum, rate_value) 
	VALUES (p_rate_id, p_rate_date, p_currency_id, p_min_sum, p_max_sum, p_rate_value)
      ON CONFLICT (rate_id, rate_date, currency_id, min_sum) DO
    UPDATE
      SET max_sum = EXCLUDED.max_sum,
        rate_value = EXCLUDED.rate_value;

end

$BODY$;

