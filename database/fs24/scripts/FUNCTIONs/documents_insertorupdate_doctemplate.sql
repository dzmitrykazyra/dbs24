create
	or replace function documents_insertorupdate_doctemplate(
		p_doc_template_id tidcode,
		p_doc_template_group_id tidcode,
		p_doc_template_code tstr30,
		p_doc_template_name tstr100,
		p_pmt_sys_id tidcode,
		p_doc_type_id tidcode
	) returns void language plpgsql as $function$ begin insert
		into
			doctemplatesref(
				doc_template_id,
				doc_template_group_id,
				doc_template_code,
				doc_template_name,
				pmt_sys_id,
				doc_type_id
			)
		values(
			p_doc_template_id,
			p_doc_template_group_id,
			p_doc_template_code,
			p_doc_template_name,
			p_pmt_sys_id,
			p_doc_type_id
		) on
		conflict(doc_template_id) do update
		set
			doc_template_group_id = EXCLUDED.doc_template_group_id,
			doc_template_code = EXCLUDED.doc_template_code,
			doc_template_name = EXCLUDED.doc_template_name,
			pmt_sys_id = EXCLUDED.pmt_sys_id,
			doc_type_id = EXCLUDED.doc_type_id;
end $function$
