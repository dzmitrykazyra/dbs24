CREATE OR REPLACE FUNCTION documents_insertorupdate_document_attrs(
    p_doc_id tidbigcode, 
    p_doc_attr_id tidcode, 
    p_doc_attr_value tstr2000)
 RETURNS void
 LANGUAGE plpgsql
AS $function$ 
begin 

INSERT INTO docattrs (doc_id, doc_attr_id, doc_attr_value) 
	VALUES (p_doc_id, p_doc_attr_id, p_doc_attr_value)
       on
		conflict(doc_id, doc_attr_id) do update
		set
		    doc_attr_value = EXCLUDED.doc_attr_value;
end $function$ 