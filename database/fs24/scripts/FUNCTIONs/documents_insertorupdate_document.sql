DROP FUNCTION documents_insertorupdate_document;
CREATE OR REPLACE FUNCTION documents_insertorupdate_document(p_doc_id tidbigcode, 
    p_parent_doc_id tidbigcode, 
    p_doc_template_id tidbigcode, 
    p_doc_status_id tidcode, 
    p_entity_id tidbigcode, 
    p_doc_date tdate, 
    p_doc_close_date tdate, 
    p_user_id tidbigcode)
 RETURNS tidbigcode
 LANGUAGE plpgsql
AS $function$ 
declare v_doc_id tidbigcode;
begin 

if p_doc_id = 0 then 
  v_doc_id = nextval('seq_doc_id');
else 
  v_doc_id = p_doc_id;
end if;

INSERT INTO documents (doc_id, parent_doc_id, doc_template_id, doc_status_id, entity_id, doc_date, doc_server_date, doc_close_date, user_id) 
	VALUES (v_doc_id, p_parent_doc_id, p_doc_template_id, p_doc_status_id, p_entity_id, p_doc_date, current_timestamp, p_doc_close_date, p_user_id)
       on
		conflict(doc_id) do update
		set
                    parent_doc_id = EXCLUDED.parent_doc_id,
		    doc_template_id = EXCLUDED.doc_template_id,
                    doc_status_id = EXCLUDED.doc_status_id,
                    entity_id = EXCLUDED.entity_id,
                    doc_date = EXCLUDED.doc_date,
                    doc_server_date = current_timestamp,
                    doc_close_date = EXCLUDED.doc_close_date,
                    user_id = EXCLUDED.user_id;

  return v_doc_id;
end $function$ 