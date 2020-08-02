-- FUNCTION: ee_dev.tariff_insertorupdate_tariffplan(tidbigcode, tidcode, tstr100, tstr100, tdate, tdate)

DROP FUNCTION tariff_insertorupdate_TariffRates;

create
	or replace function tariff_insertorupdate_tariffrates(
		p_rate_id tidcode,
		p_tariff_plan_id tidbigcode,
		p_tariff_serv_id tidcode,
		p_tariff_kind_id tidcode,
		p_tariff_scheme_id tidcode,
		p_rate_name tstr128,
		p_actual_date tdate,
		p_close_date tdate
	) returns tidcode language plpgsql as $function$ declare v_rate_id tidcode;

begin 
    if(	p_rate_id is null) or (	p_rate_id = 0) then 
        v_rate_id = nextval('seq_TariffRates');
    else 
        v_rate_id = p_rate_id;
    end if;

insert
	into
		tariffrates(
			rate_id,
			tariff_plan_id,
			tariff_serv_id,
			tariff_kind_id,
			tariff_scheme_id,
			rate_name,
			actual_date,
			close_date)
	values(
		v_rate_id,
		p_tariff_plan_id,
		p_tariff_serv_id,
		p_tariff_kind_id,
		p_tariff_scheme_id,
		p_rate_name,
		p_actual_date,
		p_close_date) on conflict(rate_id) do 
        update
            set
		tariff_plan_id = EXCLUDED.tariff_plan_id,
		tariff_serv_id = EXCLUDED.tariff_serv_id,
		tariff_kind_id = EXCLUDED.tariff_kind_id,
		tariff_scheme_id = EXCLUDED.tariff_scheme_id,
		rate_name = EXCLUDED.rate_name,
		actual_date = EXCLUDED.actual_date,
		close_date = EXCLUDED.close_date;

return v_rate_id;
end $function$

