drop function loans_insertorupdate_loansource;
create
	or replace function rlc_insertorupdate_loansource(
		p_loan_source_id tidcode,
		p_loan_source_name tstr100
	) returns void language plpgsql as $function$ begin insert
		into
			rlc_loansourcesref(
				loan_source_id,
				loan_source_name
			)
		values(
			p_loan_source_id,
			p_loan_source_name
		) on
		conflict(loan_source_id) do update
		set
			loan_source_name = EXCLUDED.loan_source_name;
end $function$