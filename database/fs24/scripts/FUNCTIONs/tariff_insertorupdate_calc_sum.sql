-- FUNCTION: ee_dev.tariff_insertorupdate_tariffplan(tidbigcode, tidcode, tstr100, tstr100, tdate, tdate)

DROP FUNCTION tariff_insertorupdate_calc_sum;

create
	or replace function tariff_insertorupdate_calc_sum(
		p_tariff_calc_id tidcode,
		p_tariff_calc_date tdate,
		p_tariff_summ tmoney
	) returns void language plpgsql as $function$ 

begin 

    INSERT INTO tariffcalcsum (tariff_calc_id, tariff_calc_date, tariff_summ) 
	VALUES (p_tariff_calc_id, p_tariff_calc_date, p_tariff_summ)
    on conflict(tariff_calc_id, tariff_calc_date) do 
        update
            set
		tariff_summ = EXCLUDED.tariff_summ;

end $function$

