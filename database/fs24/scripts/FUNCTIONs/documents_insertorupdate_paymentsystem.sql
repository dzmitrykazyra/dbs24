create
	or replace function documents_insertorupdate_paymentsystem(
		p_pmt_sys_id tidcode,
		p_pmt_sys_code tstr20,
		p_pmt_sys_name tstr100,
		p_pmt_open_date tdate,
		p_pmt_close_date tdate
	) returns void language plpgsql as $function$ begin insert
		into
			PaymentSystemsRef(
				pmt_sys_id,
				pmt_sys_code,
				pmt_sys_name,
				pmt_open_date,
				pmt_close_date
			)
		values(
			p_pmt_sys_id,
			p_pmt_sys_code,
			p_pmt_sys_name,
			p_pmt_open_date,
			p_pmt_close_date
		) on
		conflict(pmt_sys_id) do update
		set
			pmt_sys_code = EXCLUDED.pmt_sys_code,
			pmt_sys_name = EXCLUDED.pmt_sys_name,
			pmt_open_date = EXCLUDED.pmt_open_date,
			pmt_close_date = EXCLUDED.pmt_close_date;
end $function$
