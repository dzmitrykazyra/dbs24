CREATE OR REPLACE FUNCTION documents_insertorupdate_doctype(p_doc_type_id tidcode, p_doc_type_name tstr100)
 RETURNS void
 LANGUAGE plpgsql
AS $function$ begin insert
		into
			DocTypesref(
				doc_type_id,
				doc_type_name
			)
		values(
			p_doc_type_id,
			p_doc_type_name
		) on
		conflict(doc_type_id) do update
		set
			doc_type_name = EXCLUDED.doc_type_name;
end $function$
