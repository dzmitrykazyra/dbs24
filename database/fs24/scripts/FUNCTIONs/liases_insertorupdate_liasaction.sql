drop function liases_insertorupdate_liasaction;
create
	or replace function liases_insertorupdate_liasaction(
		p_doc_id tidbigcode,
		p_lias_action_id tidbigcode,
		p_action_type_id tidcode,
		p_lias_id tidcode,
		p_fin_oper_code tidcode,
		p_lias_date tdate,
		p_oper_date tdate,
		p_fin_oper_status_id tidcode,
		p_lias_sum tmoney
	) returns tidbigcode language plpgsql as $function$ declare v_lias_action_id TIdBigCode;

begin if p_lias_action_id = 0 then v_lias_action_id = nextval('seq_lias_action_id');
else v_lias_action_id = p_lias_action_id;
end if;

insert
	into
		liasactions(
			doc_id,
			lias_action_id,
			action_type_id,
			lias_id,
			fin_oper_code,
			lias_date,
			oper_date,
			server_date,
			fin_oper_status_id,
			lias_sum
		)
	values(
		p_doc_id,
		v_lias_action_id,
		p_action_type_id,
		p_lias_id,
		p_fin_oper_code,
		p_lias_date,
		p_oper_date,
		current_timestamp,
		p_fin_oper_status_id,
		p_lias_sum
	) on
	conflict(lias_action_id) do update
	set
		doc_id = EXCLUDED.doc_id,
		action_type_id = EXCLUDED.action_type_id,
		lias_id = EXCLUDED.lias_id,
		fin_oper_code = EXCLUDED.fin_oper_code,
		lias_date = EXCLUDED.lias_date,
		oper_date = EXCLUDED.oper_date,
		fin_oper_status_id = EXCLUDED.fin_oper_status_id,
		lias_sum = EXCLUDED.lias_sum;

return v_lias_action_id;
end $function$
