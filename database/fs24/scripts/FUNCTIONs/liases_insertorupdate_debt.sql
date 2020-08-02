drop function liases_insertorupdate_debt;
create
	or replace function liases_insertorupdate_debt(
		p_debt_id tidcode,
		p_counterparty_id tidbigcode,
		p_currency_id tidcode,
		p_contract_id TIdBigCode,
		p_debt_state_id tidcode,
		p_lias_kind_id tidcode,
		p_lias_type_id tidcode,
		p_base_asset_type_id tidcode,
		p_debt_start_date tdate,
		p_debt_final_date tdate
	) returns tidcode language plpgsql as $function$ declare v_debt_id tidcode;

begin if p_debt_id = 0 then v_debt_id = nextval('seq_debt_id');
else v_debt_id = p_debt_id;
end if;

insert
	into
		liasdebts(
			debt_id,
			counterparty_id,
			currency_id,
			contract_id,
			debt_state_id,
			lias_kind_id,
			lias_type_id,
			base_asset_type_id,
			debt_start_date,
			debt_final_date
		)
	values(
		v_debt_id,
		p_counterparty_id,
		p_currency_id,
		p_contract_id,
		p_debt_state_id,
		p_lias_kind_id,
		p_lias_type_id,
		p_base_asset_type_id,
		p_debt_start_date,
		p_debt_final_date
	) on
	conflict(debt_id) do update
	set
		counterparty_id = EXCLUDED.counterparty_id,
		currency_id = EXCLUDED.currency_id,
		contract_id = EXCLUDED.contract_id,
		debt_state_id = EXCLUDED.debt_state_id,
		lias_kind_id = EXCLUDED.lias_kind_id,
		lias_type_id = EXCLUDED.lias_type_id,
		base_asset_type_id = EXCLUDED.base_asset_type_id,
		debt_start_date = EXCLUDED.debt_start_date,
		debt_final_date = EXCLUDED.debt_final_date;

return v_debt_id;
end $function$
