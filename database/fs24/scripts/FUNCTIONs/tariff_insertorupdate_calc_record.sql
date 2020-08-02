-- FUNCTION: ee_dev.tariff_insertorupdate_tariffplan(tidbigcode, tidcode, tstr100, tstr100, tdate, tdate)

DROP FUNCTION tariff_insertorupdate_calc_record;

create
	or replace function tariff_insertorupdate_calc_record(
		p_tariff_calc_id tidcode,
		p_rate_id tidcode,
		p_entity_id tidbigcode
	) returns tidcode language plpgsql as $function$ 
declare v_tariff_calc_id tidcode;

begin 
    if(	p_tariff_calc_id is null) or (	p_tariff_calc_id = 0) then 
        v_tariff_calc_id = nextval('seq_TariffPlanAccretions');
    else 
        v_tariff_calc_id = p_tariff_calc_id;
    end if;

    INSERT INTO tariffcalcrecords (tariff_calc_id, rate_id, entity_id) 
	VALUES (v_tariff_calc_id, p_rate_id, p_entity_id)
    on conflict(tariff_calc_id) do 
        update
            set
		rate_id = EXCLUDED.rate_id,
		entity_id = EXCLUDED.entity_id;

return v_tariff_calc_id;
end $function$

