create
	or replace function documents_insertorupdate_docstatus(
		p_doc_status_id tidcode,
		p_doc_status_name tstr100
	) returns void language plpgsql as $function$ begin insert
		into
			docstatusesref(
				doc_status_id,
				doc_status_name
			)
		values(
			p_doc_status_id,
			p_doc_status_name
		) on
		conflict(doc_status_id) do update
		set
			doc_status_name = EXCLUDED.doc_status_name;
end $function$
