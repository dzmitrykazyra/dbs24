 DROP FUNCTION log_insert_log_requests;

CREATE OR REPLACE FUNCTION log_insert_log_requests(
	p_req_type tstr20,
	p_address tstr100,
        p_message_date tdatetime,
	p_req_body ttext,
        p_req_answer tstr128,
	p_exception_message tstr2000,
        p_duration tintcounter)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

 begin 
		INSERT INTO log_requests (id, req_type, address, req_body, req_answer, message_date, exception_message, duration) 
	VALUES (nextval('seq_log_requests'), p_req_type, p_address, p_req_body, p_req_answer, p_message_date, p_exception_message, p_duration);
end 

$BODY$;