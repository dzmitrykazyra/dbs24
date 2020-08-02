CREATE OR REPLACE FUNCTION documents_insertorupdate_doctemplategroup(p_doc_template_group_id tidcode, p_doc_template_group_name tstr100)
 RETURNS void
 LANGUAGE plpgsql
AS $function$ begin insert
		into
			DocTemplateGroupsRef(
				doc_template_group_id,
				doc_template_group_name
			)
		values(
			p_doc_template_group_id,
			p_doc_template_group_name
		) on
		conflict(doc_template_group_id) do update
		set
			doc_template_group_name = EXCLUDED.doc_template_group_name;
end $function$
