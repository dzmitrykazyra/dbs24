drop function liases_insertorupdate_lias;
create
	or replace function liases_insertorupdate_lias(
		p_lias_id tidcode,
		p_debt_id tidcode,
		p_start_date tdate,
		p_allow_date tdate,
		p_final_date tdate,
		p_legal_date tdate,
		p_inactive_date tdate,
		p_is_canceled tboolean
	) returns tidcode language plpgsql as $function$ declare v_lias_id tidcode;

begin if p_lias_id = 0 then v_lias_id = nextval('seq_lias_id');
else v_lias_id = p_lias_id;
end if;

insert
	into
		liases(
			lias_id,
			debt_id,
			start_date,
			allow_date,
			final_date,
			legal_date,
			server_date,
			inactive_date,
			is_canceled
		)
	values(
		v_lias_id,
		p_debt_id,
		p_start_date,
		p_allow_date,
		p_final_date,
		p_legal_date,
		current_timestamp,
		p_inactive_date,
		p_is_canceled
	) on
	conflict(lias_id) do update
	set
		lias_id = EXCLUDED.lias_id,
		debt_id = EXCLUDED.debt_id,
		start_date = EXCLUDED.start_date,
		allow_date = EXCLUDED.allow_date,
		final_date = EXCLUDED.final_date,
		legal_date = EXCLUDED.legal_date,
		server_date = EXCLUDED.server_date,
		inactive_date = EXCLUDED.inactive_date,
		is_canceled = EXCLUDED.is_canceled;

return v_lias_id;
end $function$
