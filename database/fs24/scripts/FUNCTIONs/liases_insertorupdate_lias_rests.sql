CREATE OR REPLACE FUNCTION liases_insertorupdate_lias_rests(
	p_lias_id tidcode,
	p_rest_date tdate,
	p_rest tmoney)
    RETURNS void
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE 
AS $BODY$

 
begin 

INSERT INTO liasrests (lias_id, rest_type, rest_date, rest) 
	VALUES (p_lias_id, 1, p_rest_date, p_rest)
    on
	conflict(lias_id, rest_type, rest_date) do update
	set rest = EXCLUDED.rest;
end 

$BODY$;