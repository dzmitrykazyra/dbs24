CREATE OR REPLACE FUNCTION documents_insertorupdate_docattr(p_doc_attr_id tidCode, p_doc_attr_code tstr30, p_doc_attr_name tstr128)
 RETURNS void
 LANGUAGE plpgsql
AS $function$ begin insert
		into
			DocAttrsRef(
                                doc_attr_id,
				doc_attr_code,
				doc_attr_name
			)
		values( p_doc_attr_id,
			p_doc_attr_code,
			p_doc_attr_name
		) on
		conflict(doc_attr_id) do update
		set
                    doc_attr_code = EXCLUDED.doc_attr_code,
		    doc_attr_name = EXCLUDED.doc_attr_name;
end $function$
