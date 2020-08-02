drop function documents_insertorupdate_doctemplateattr;

create
	or replace function documents_insertorupdate_doctemplateattr(
		p_doc_template_id tidcode,
		p_doc_attr_id tidcode
	) returns void language plpgsql as $function$ begin insert
		into
			doctemplateattrsref(
				doc_template_id,
				doc_attr_id
			)
		values(
			p_doc_template_id,
			p_doc_attr_id
		 ) on
		conflict(
			doc_template_id,
			doc_attr_id
		) do update
		set
                  doc_attr_id = EXCLUDED.doc_attr_id;
end $function$
