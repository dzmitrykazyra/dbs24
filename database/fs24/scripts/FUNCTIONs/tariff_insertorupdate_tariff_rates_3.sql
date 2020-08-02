

DROP FUNCTION tariff_insertorupdate_tariff_rates_3;

CREATE OR REPLACE FUNCTION tariff_insertorupdate_tariff_rates_3(
	p_rate_id tidcode,
	p_rate_date tdate,
	p_currency_id tidcode,
	p_rate_value tpercrate,
        p_fix_sum tmoney)
    RETURNS void
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE 
AS $BODY$

begin 
    
INSERT INTO tariffrates_3 (rate_id, rate_date, currency_id, rate_value, fix_sum) 
	VALUES (p_rate_id, p_rate_date, p_currency_id, p_rate_value, p_fix_sum)
      ON CONFLICT (rate_id, rate_date, currency_id) DO
    UPDATE
      SET fix_sum = EXCLUDED.fix_sum,
        rate_value = EXCLUDED.rate_value;

end

$BODY$;

